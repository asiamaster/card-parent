package com.dili.card.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.dili.card.common.constant.Constant;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.CardManageRpcResolver;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IReturnCardService;
import com.dili.card.service.ISerialService;
import com.dili.card.service.tcc.ChangeCardTccTransactionManager;
import com.dili.card.type.CardStatus;
import com.dili.card.type.FundItem;
import com.dili.card.type.OperateType;
import com.dili.card.type.TradeChannel;
import com.dili.card.type.TradeType;
import com.dili.card.validator.AccountValidator;
import com.dili.ss.constant.ResultCode;
import com.dili.tcc.core.TccContextHolder;

@Service("returnCardService")
public class ReturnCardServiceImpl implements IReturnCardService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ChangeCardTccTransactionManager.class);

	@Autowired
	private CardManageRpcResolver cardManageRpcResolver;
	@Resource
	private ISerialService serialService;
	@Resource
	private PayRpcResolver payRpcResolver;
	@Resource
	protected IAccountQueryService accountQueryService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void handle(CardRequestDto cardParam) {
		// 校验卡状态
		UserAccountCardResponseDto accountCard = accountQueryService.getByAccountId(cardParam.getAccountId());
		AccountValidator.validateMatchAccount(cardParam, accountCard);
		if (!Integer.valueOf(CardStatus.NORMAL.getCode()).equals(accountCard.getCardState())) {
			throw new CardAppBizException("",
					String.format("该卡为%s状态,不能进行退卡", CardStatus.getName(accountCard.getCardState())));
		}
		// 余额校验
		Long fee = 0L;
		if (accountCard.getMaster()) {
			BalanceResponseDto balanceResponseDto = payRpcResolver
					.findBalanceByFundAccountId(accountCard.getFundAccountId());
			if (balanceResponseDto.getFrozenAmount() > 0L) {
				throw new CardAppBizException(ResultCode.DATA_ERROR, "卡冻结金额不为0,不能退卡");
			}
			if (balanceResponseDto.getBalance() >= 100L) {
				throw new CardAppBizException(ResultCode.DATA_ERROR, "卡余额大于1元,不能退卡");
			}
			// 缴费金额就是当前卡余额
			fee = balanceResponseDto.getBalance();
		}
		cardParam.setServiceFee(fee);
		// 构建记录
		BusinessRecordDo businessRecord = serialService.createBusinessRecord(cardParam, accountCard, record -> {
			record.setType(OperateType.REFUND_CARD.getCode());
			if (accountCard.getMaster() && cardParam.getServiceFee() != 0L) {
				record.setAmount(cardParam.getServiceFee());
				record.setTradeType(TradeType.FEE.getCode());
				record.setTradeChannel(TradeChannel.BALANCE.getCode());
			}
		});
		// 如果金额等于0不进行交易号创建 直接退卡 注销账号
		String tradeId = null;
		if (accountCard.getMaster() && fee != 0L) {
			CreateTradeRequestDto tradeRequest = CreateTradeRequestDto.createTrade(TradeType.FEE.getCode(),
					accountCard.getAccountId(), accountCard.getFundAccountId(), fee, businessRecord.getSerialNo(),
					String.valueOf(businessRecord.getCycleNo()));
			// 创建交易
			tradeId = payRpcResolver.prePay(tradeRequest).getTradeId();
		}
		businessRecord.setTradeNo(tradeId);
		serialService.saveBusinessRecord(businessRecord);
		// 保证退卡成功
		cardManageRpcResolver.returnCard(cardParam);
		
        TccContextHolder.get().addAttr(Constant.TRADE_ID_KEY, tradeId);
        TccContextHolder.get().addAttr(Constant.BUSINESS_RECORD_KEY, businessRecord);
        TccContextHolder.get().addAttr(Constant.USER_ACCOUNT, accountCard);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void afterCompletion(CardRequestDto cardParam) {
		BusinessRecordDo businessRecord = TccContextHolder.get().getAttr(Constant.BUSINESS_RECORD_KEY, BusinessRecordDo.class);
	    String tradeNo = TccContextHolder.get().getAttr(Constant.TRADE_ID_KEY, String.class);
	    UserAccountCardResponseDto userAccount = TccContextHolder.get().getAttr(Constant.USER_ACCOUNT, UserAccountCardResponseDto.class);
		if (userAccount.getMaster()) {
			if (cardParam.getServiceFee() != 0L) {
				// 执行实际转账操作
				TradeRequestDto tradeRequestDto = TradeRequestDto.createTrade(userAccount, tradeNo, TradeChannel.CASH.getCode(),
						cardParam.getLoginPwd());
				tradeRequestDto.addServiceFeeItem(cardParam.getServiceFee(), FundItem.RETURN_CARD_CHANGE);
				payRpcResolver.trade(tradeRequestDto);
			}
			// 账号注销
			CreateTradeRequestDto createTradeRequestDto = null;
			try {
				createTradeRequestDto = CreateTradeRequestDto.createCommon(userAccount.getFundAccountId(), userAccount.getAccountId());
				payRpcResolver.unregister(createTradeRequestDto);
			}catch (Exception e) {
				LOGGER.error("退卡注销账户失败：" + JSON.toJSONString(createTradeRequestDto), e);
			}
		}
		try {
			SerialDto serialDto = serialService.createAccountSerial(businessRecord, null);
			serialService.handleSuccess(serialDto, true);
		} catch (Exception e) {
			LOGGER.error("退卡操作记录失败", e);
		}
	}

}

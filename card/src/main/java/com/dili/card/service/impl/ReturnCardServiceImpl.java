package com.dili.card.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.dili.card.type.CardStatus;
import com.dili.card.type.DisableState;
import com.dili.card.type.FundItem;
import com.dili.card.type.OperateType;
import com.dili.card.type.TradeChannel;
import com.dili.card.type.TradeType;
import com.dili.card.validator.AccountValidator;
import com.dili.ss.constant.ResultCode;

@Service("returnCardService")
public class ReturnCardServiceImpl implements IReturnCardService {
	
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
		if (Integer.valueOf(DisableState.DISABLED.getCode()).equals(accountCard.getAccountState())) {
			throw new CardAppBizException("该卡账户为禁用状态,不能进行退卡");
		}
		// 余额校验
		Long fee = 0L;
		//是否主卡
		boolean master = accountCard.getMaster();
		if (master) {
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
		//是主卡 同时卡余额存在并且小于1元需要进行流水
		boolean hasTradeSerial = master && fee != 0L;
		// 构建记录
		BusinessRecordDo businessRecord = serialService.createBusinessRecord(cardParam, accountCard, record -> {
			record.setType(OperateType.REFUND_CARD.getCode());
			if (hasTradeSerial) {
				record.setAmount(cardParam.getServiceFee());
				record.setTradeType(TradeType.FEE.getCode());
				record.setTradeChannel(TradeChannel.BALANCE.getCode());
			}
		});
		// 如果金额等于0不进行交易号创建 直接退卡 注销账号
		String tradeId = null;
		if (master && fee != 0L) {
			CreateTradeRequestDto tradeRequest = CreateTradeRequestDto.createTrade(TradeType.FEE.getCode(),
					accountCard.getAccountId(), accountCard.getFundAccountId(), fee, businessRecord.getSerialNo(),
					String.valueOf(businessRecord.getCycleNo()));
			// 创建交易
			tradeId = payRpcResolver.prePay(tradeRequest).getTradeId();
		}
		businessRecord.setTradeNo(tradeId);
		serialService.saveBusinessRecord(businessRecord);
		// 退卡
		cardManageRpcResolver.returnCard(cardParam);
		if (master) {//主卡退卡注销账户 副卡不做此操作
			if (hasTradeSerial) {//存在余额在一元内需要进行缴费操作
				// 执行实际缴费操作
				TradeRequestDto tradeRequestDto = TradeRequestDto.createTrade(accountCard, tradeId, TradeChannel.CASH.getCode(),
						cardParam.getLoginPwd());
				tradeRequestDto.addServiceFeeItem(cardParam.getServiceFee(), FundItem.RETURN_CARD_CHANGE);
				payRpcResolver.trade(tradeRequestDto);
			}
			// 账号注销
			CreateTradeRequestDto createTradeRequestDto = CreateTradeRequestDto.createCommon(accountCard.getFundAccountId(), accountCard.getAccountId());
			payRpcResolver.unregister(createTradeRequestDto);
		}
		//流水建立
		SerialDto serialDto = serialService.createAccountSerial(businessRecord, null);
		serialService.handleSuccess(serialDto, hasTradeSerial);
	}

}

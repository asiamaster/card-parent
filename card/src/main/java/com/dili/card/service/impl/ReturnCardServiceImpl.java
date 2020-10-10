package com.dili.card.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.Constant;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.dto.pay.TradeResponseDto;
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
	public String handle(CardRequestDto cardParam) {
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
		// 是否主卡
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
		// 是主卡 同时卡余额存在并且小于1元需要进行流水
		boolean masterHasTradeSerial = master && fee > 0L;
		// 构建本地记录
		BusinessRecordDo businessRecord = serialService.createBusinessRecord(cardParam, accountCard, record -> {
			record.setType(OperateType.REFUND_CARD.getCode());
			Map<String, Object> attach = new HashMap<String, Object>();
			attach.put(Constant.BUSINESS_RECORD_ATTACH_CARDTYPE, accountCard.getCardType());
			record.setAttach(JSONObject.toJSONString(attach));
			record.setNotes(master ? "退主卡" :"退副卡" );
			if (masterHasTradeSerial) {
				record.setAmount(cardParam.getServiceFee());
				record.setTradeType(TradeType.FEE.getCode());
				record.setTradeChannel(TradeChannel.BALANCE.getCode());
			}
		});
		
		// 退卡
		cardManageRpcResolver.returnCard(cardParam);
		
		TradeResponseDto tradeResponseDto = null;
		SerialDto serialDto = null;
		
		//是主卡 同时卡余额存在并且小于1元需要进行流水
		if (masterHasTradeSerial) {
			//转出零钱
			CreateTradeRequestDto tradeRequest = CreateTradeRequestDto.createTrade(TradeType.FEE.getCode(),
					accountCard.getAccountId(), accountCard.getFundAccountId(), fee, businessRecord.getSerialNo(),
					String.valueOf(businessRecord.getCycleNo()));
			// 创建交易
			String tradeId = payRpcResolver.prePay(tradeRequest).getTradeId();
			
			// 执行实际缴费操作
			TradeRequestDto tradeRequestDto = TradeRequestDto.createTrade(accountCard, tradeId,
					TradeChannel.BALANCE.getCode(), cardParam.getLoginPwd());
			tradeRequestDto.addServiceFeeItem(cardParam.getServiceFee(), FundItem.RETURN_CARD_CHANGE);
			tradeResponseDto =  payRpcResolver.trade(tradeRequestDto);	
			
			businessRecord.setTradeNo(tradeId);
			
			// 流水建立主卡退卡零钱流水
			serialDto = serialService.createAccountSerialWithFund(businessRecord, tradeResponseDto, (serialRecord, feeType) -> {
				serialRecord.setFundItem(FundItem.RETURN_CARD_CHANGE.getCode());
                serialRecord.setFundItemName(FundItem.RETURN_CARD_CHANGE.getName());
                serialRecord.setNotes("退卡：零钱转入市场收益账户");   
	        });			
		}else {
			// 流水建立副卡没有退卡零钱流水  主卡余额为0 也走这里
			serialDto = serialService.createAccountSerial(businessRecord, null);
		}
		
		// 主 副卡退卡注销资金账户
		CreateTradeRequestDto createTradeRequestDto = CreateTradeRequestDto
				.createCommon(accountCard.getFundAccountId(), accountCard.getAccountId());
		payRpcResolver.unregister(createTradeRequestDto);
		
		//保存本地记录
		serialService.saveBusinessRecord(businessRecord);
		//保存远程流水记录
		serialService.handleSuccess(serialDto, masterHasTradeSerial);
		
		//返回操作记录流水号 补打需要使用
		return businessRecord.getSerialNo();
	}

}

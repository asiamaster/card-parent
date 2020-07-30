package com.dili.card.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.dto.AccountWithAssociationResponseDto;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UnfreezeFundDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.FreezeFundRecordDto;
import com.dili.card.dto.pay.FreezeFundRecordParam;
import com.dili.card.dto.pay.FundOpResponseDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.AccountQueryRpc;
import com.dili.card.rpc.PayRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IFundService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.BizNoType;
import com.dili.card.type.FeeType;
import com.dili.card.type.FundItem;
import com.dili.card.type.OperateType;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.util.MoneyUtils;
import com.google.common.collect.Lists;

/**
 * 资金操作service实现类
 * 
 * @author xuliang
 */
@Service
public class FundServiceImpl implements IFundService {
	private static final Logger LOGGER = LoggerFactory.getLogger(FundServiceImpl.class);

	@Resource
	private ISerialService serialService;
	@Autowired
	private IAccountQueryService accountQueryService;
	@Autowired
	private PayRpcResolver payRpcResolver;
	@Resource
	private UidRpcResovler uidRpcResovler;
	@Resource
	private PayRpc payRpc;
	@Resource
	private AccountQueryRpc accountQueryRpc;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void frozen(FundRequestDto requestDto) {
		UserAccountCardResponseDto accountCard = accountQueryService.getByAccountId(requestDto);

		BalanceResponseDto balance = payRpcResolver.findBalanceByFundAccountId(accountCard.getFundAccountId());
		if (requestDto.getAmount() >= balance.getAvailableAmount()) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "可用余额不足");
		}

		BusinessRecordDo businessRecord = serialService.createBusinessRecord(requestDto, accountCard, record -> {
			record.setType(OperateType.FROZEN_FUND.getCode());
			record.setAmount(requestDto.getAmount());
			record.setNotes(requestDto.getMark());
		});
        CreateTradeRequestDto createTradeRequestDto = CreateTradeRequestDto.createFrozenAmount(
                accountCard.getFundAccountId(),
                accountCard.getAccountId(),
                requestDto.getAmount());
        FundOpResponseDto fundOpResponseDto = payRpcResolver.postFrozenFund(createTradeRequestDto);
        businessRecord.setTradeNo(String.valueOf(fundOpResponseDto.getFrozenId()));
        serialService.saveBusinessRecord(businessRecord);
        try {
            TradeResponseDto transaction = fundOpResponseDto.getTransaction();
            transaction.addVirtualPrincipalFundItem(-requestDto.getAmount());
            SerialDto serialDto = serialService.createAccountSerialWithFund(businessRecord, transaction, (serialRecord, feeType) -> {
                if (Integer.valueOf(FeeType.ACCOUNT.getCode()).equals(feeType)) {
                    serialRecord.setFundItem(FundItem.TRADE_FREEZE.getCode());
                    serialRecord.setFundItemName(FundItem.TRADE_FREEZE.getName());
                }
                serialRecord.setNotes("手动冻结资金");
            }, true);
            serialService.handleSuccess(serialDto);
        } catch (Exception e) {
            LOGGER.error("冻结处理流水失败", e);
        }
    }

	@Override
	public void unfrozen(UnfreezeFundDto unfreezeFundDto) {
		AccountWithAssociationResponseDto accountInfo = accountQueryService
				.getAssociationByAccountId(unfreezeFundDto.getAccountId());
		for (Long tradeNo : unfreezeFundDto.getTradeNos()) {
			// 对应支付的frozenId
			UnfreezeFundDto dto = new UnfreezeFundDto();
			dto.setFrozenId(tradeNo);
			GenericRpcResolver.resolver(payRpc.unfrozenFund(dto), "pay-service");
			// 保存操作记录
			System.out.println("****************解冻成功");
		}

//		buildBusinessRecordDo(accountInfo, unfreezeFundDto);

		// 保存全局操作记录
		SerialRecordDo serialRecord = buildSerialRecord(accountInfo, unfreezeFundDto);
//		serialService.copyCommonFields(serialRecord, buildBusinessRecordDo);
		SerialDto serialDto = new SerialDto();
		serialDto.setSerialRecordList(Lists.newArrayList(serialRecord));
		serialService.saveSerialRecord(serialDto);
	}

	@Override
	public PageOutput<List<FreezeFundRecordDto>> frozenRecord(FreezeFundRecordParam queryParam) {
		// 从支付查询 默认查询从当日起一年内的未解冻记录
		PageOutput<List<FreezeFundRecordDto>> pageOutPut = GenericRpcResolver.resolver(payRpc.listFrozenRecord(queryParam),
				"pay-service");
		for (FreezeFundRecordDto record : pageOutPut.getData()) {
			if (StringUtils.isNoneBlank(record.getExtension())) {
				// 在冻结资金时会以json格式存入当时的操作人及其编号
				JSONObject jsonObject = JSONObject.parseObject(record.getExtension());
				record.setOpNo(jsonObject.getString("opNo"));
				record.setOpName(jsonObject.getString("opName"));
				record.setAmountText(MoneyUtils.centToYuan(record.getAmount()));
			} else {
				LOGGER.info("该记录冻结时并未存入操作人信息,金额[{}]", record.getAmount());
			}
		}
		return pageOutPut;
	}

	/**
	 * 生成全局日志
	 *
	 * @param accountInfo
	 * @param unfreezeFundDto
	 */
	private SerialRecordDo buildSerialRecord(AccountWithAssociationResponseDto accountInfo,
			UnfreezeFundDto unfreezeFundDto) {
		SerialRecordDo record = new SerialRecordDo();
		record.setAccountId(accountInfo.getPrimary().getAccountId());
		record.setCardNo(accountInfo.getPrimary().getCardNo());
		record.setCustomerId(accountInfo.getPrimary().getCustomerId());
		record.setCustomerName(accountInfo.getPrimary().getCustomerName());
		record.setCustomerNo(accountInfo.getPrimary().getCustomerCode());
		record.setFirmId(unfreezeFundDto.getFirmId());
		record.setSerialNo(uidRpcResovler.bizNumber(BizNoType.OPERATE_SERIAL_NO.getCode()));
		record.setNotes("开卡");
		record.setOperatorId(unfreezeFundDto.getOpId());
		record.setOperatorName(unfreezeFundDto.getOpName());
		record.setOperatorNo(unfreezeFundDto.getOpNo());
		record.setTradeType(OperateType.ACCOUNT_TRANSACT.getCode());
		record.setType(OperateType.ACCOUNT_TRANSACT.getCode());
		record.setOperateTime(LocalDateTime.now());
		return record;
	}

//	private BusinessRecordDo buildBusinessRecordDo(AccountWithAssociationResponseDto accountInfo,UnfreezeFundDto unfreezeFundDto) {
//		BusinessRecordDo serial = new BusinessRecordDo();
//		serial.setAccountId(accountInfo.getPrimary().getAccountId());
//		serial.setCardNo(accountInfo.getPrimary().getCardNo());
//		serial.setCustomerId(accountInfo.getPrimary().getCustomerId());
//		serial.setCustomerName(accountInfo.getPrimary().getCustomerName());
//		serial.setCustomerNo(accountInfo.getPrimary().getCustomerCode());
//		AccountCycleDo cycleDo = accountCycleService.findActiveCycleByUserId(unfreezeFundDto.getOpId(),
//				unfreezeFundDto.getOpName(), unfreezeFundDto.getOpNo());
//		serial.setCycleNo(cycleDo.getCycleNo());
//		serial.setFirmId(unfreezeFundDto.getFirmId());
//		serial.setOperatorId(unfreezeFundDto.getOpId());
//		serial.setOperatorName(unfreezeFundDto.getOpName());
//		serial.setOperatorNo(unfreezeFundDto.getOpNo());
//		serial.setOperateTime(LocalDateTime.now());
//		serial.setNotes("解冻资金");
//		serial.setSerialNo(uidRpcResovler.bizNumber(BizNoType.OPERATE_SERIAL_NO.getCode()));
//		serial.setType(OperateType.UNFROZEN_FUND.getCode());
//		return serial;
//	}
}

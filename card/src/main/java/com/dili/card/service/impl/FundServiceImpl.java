package com.dili.card.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.card.dto.AccountWithAssociationResponseDto;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.FundUnfrozenRecordDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UnfreezeFundDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.BalanceRequestDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.FeeItemDto;
import com.dili.card.dto.pay.FundOpResponseDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.entity.AccountCycleDo;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.AccountQueryRpc;
import com.dili.card.rpc.PayRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IFundService;
import com.dili.card.service.IPayService;
import com.dili.card.service.ISerialService;
import com.dili.card.service.recharge.AbstractRechargeManager;
import com.dili.card.service.recharge.RechargeFactory;
import com.dili.card.type.ActionType;
import com.dili.card.type.BizNoType;
import com.dili.card.type.CardStatus;
import com.dili.card.type.FeeType;
import com.dili.card.type.FundItem;
import com.dili.card.type.OperateType;
import com.dili.card.type.TradeChannel;
import com.dili.card.type.TradeType;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.PageOutput;
import com.google.common.collect.Lists;

import cn.hutool.core.collection.CollUtil;

/**
 * 资金操作service实现类
 * @author xuliang
 */
@Service
public class FundServiceImpl implements IFundService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FundServiceImpl.class);

    @Autowired
    private RechargeFactory rechargeFactory;
    @Resource
    private ISerialService serialService;
    @Resource
    private IPayService payService;
    @Autowired
    private IAccountQueryService accountQueryService;
    @Resource
    private IAccountCycleService accountCycleService;
    @Autowired
    private PayRpcResolver payRpcResolver;
    @Resource
	private UidRpcResovler uidRpcResovler;
    @Resource
	private PayRpc payRpc;
    @Resource
	private AccountQueryRpc accountQueryRpc;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void withdraw(FundRequestDto fundRequestDto) {
        UserAccountCardResponseDto accountCard = accountQueryService.getByAccountId(fundRequestDto);
        if (!Integer.valueOf(CardStatus.NORMAL.getCode()).equals(accountCard.getCardState())) {
            throw new CardAppBizException("", String.format("该卡为%s状态,不能进行提现", CardStatus.getName(accountCard.getCardState())));
        }
        BalanceResponseDto balance = payService.queryBalance(new BalanceRequestDto(accountCard.getFundAccountId()));
        long totalAmount = fundRequestDto.getAmount() + (fundRequestDto.getServiceCost() != null ? fundRequestDto.getServiceCost() : 0L);
        if (totalAmount > balance.getAvailableAmount()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "可用余额不足");
        }
        BusinessRecordDo businessRecord = new BusinessRecordDo();
        serialService.buildCommonInfo(fundRequestDto, businessRecord);
        //构建创建交易参数
        CreateTradeRequestDto createTradeRequest = new CreateTradeRequestDto();
        createTradeRequest.setType(TradeType.WITHDRAW.getCode());
        createTradeRequest.setAccountId(accountCard.getFundAccountId());
        createTradeRequest.setAmount(fundRequestDto.getAmount());
        createTradeRequest.setSerialNo(businessRecord.getSerialNo());
        createTradeRequest.setCycleNo(String.valueOf(businessRecord.getCycleNo()));
        createTradeRequest.setDescription("");
        createTradeRequest.setBusinessId(accountCard.getAccountId());
        //创建交易
        String tradeNo = payService.createTrade(createTradeRequest);
        //保存业务办理记录
        businessRecord.setTradeNo(tradeNo);
        businessRecord.setType(OperateType.ACCOUNT_WITHDRAW.getCode());
        businessRecord.setAmount(fundRequestDto.getAmount());
        businessRecord.setTradeType(TradeType.WITHDRAW.getCode());
        businessRecord.setTradeChannel(fundRequestDto.getTradeChannel());
        businessRecord.setServiceCost(fundRequestDto.getServiceCost());
        businessRecord.setContractNo(fundRequestDto.getContractNo());
        businessRecord.setConsignorId(fundRequestDto.getConsignorId());
        //根据操作交易流水梳理原型，提现不加备注--2020-07-17
        //businessRecord.setNotes(fundRequestDto.getServiceCost() == null ? null : String.format("手续费%s元", CurrencyUtils.toYuanWithStripTrailingZeros(fundRequestDto.getServiceCost())));
        serialService.saveBusinessRecord(businessRecord);
        if (Integer.valueOf(TradeChannel.CASH.getCode()).equals(fundRequestDto.getTradeChannel())) {
            accountCycleService.decreaseeCashBox(businessRecord.getCycleNo(), fundRequestDto.getAmount());
        }
        //提现提交
        TradeRequestDto withdrawRequest = new TradeRequestDto();
        withdrawRequest.setTradeId(tradeNo);
        withdrawRequest.setAccountId(accountCard.getFundAccountId());
        withdrawRequest.setChannelId(fundRequestDto.getTradeChannel());
        withdrawRequest.setPassword(fundRequestDto.getTradePwd());
        withdrawRequest.setBusinessId(accountCard.getAccountId());
        if (Integer.valueOf(TradeChannel.E_BANK.getCode()).equals(fundRequestDto.getTradeChannel()) && fundRequestDto.getServiceCost() > 0L) {
            List<FeeItemDto> fees = new ArrayList<>();
            FeeItemDto feeItem = new FeeItemDto();
            feeItem.setAmount(fundRequestDto.getServiceCost());
            feeItem.setType(FeeType.SERVICE.getCode());
            feeItem.setTypeName(FeeType.SERVICE.getName());
            fees.add(feeItem);
            withdrawRequest.setFees(fees);
        }
        TradeResponseDto withdrawResponse = payService.commitWithdraw(withdrawRequest);
        try {
            SerialDto serialDto = buildWithdrawSerial(fundRequestDto, businessRecord, withdrawResponse);
            serialService.handleSuccess(serialDto);
        } catch (Exception e) {
            LOGGER.error("withdraw", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void frozen(FundRequestDto requestDto) {
        UserAccountCardResponseDto accountCard = accountQueryService.getByAccountId(requestDto);

        BalanceResponseDto balance = payService.queryBalance(new BalanceRequestDto(accountCard.getFundAccountId()));
        if (requestDto.getAmount() >= balance.getAvailableAmount()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "可用余额不足");
        }

        BusinessRecordDo businessRecord = new BusinessRecordDo();
        serialService.buildCommonInfo(requestDto, businessRecord);

        CreateTradeRequestDto createTradeRequestDto = CreateTradeRequestDto.createFrozenAmount(
                accountCard.getFundAccountId(),
                accountCard.getAccountId(),
                requestDto.getAmount());
        FundOpResponseDto fundOpResponseDto = payRpcResolver.postFrozenFund(createTradeRequestDto);

        businessRecord.setType(OperateType.FROZEN_FUND.getCode());
        businessRecord.setAmount(requestDto.getAmount());
        businessRecord.setNotes(requestDto.getMark());
        businessRecord.setTradeNo(String.valueOf(fundOpResponseDto.getFrozenId()));
        serialService.saveBusinessRecord(businessRecord);

        SerialDto serialDto = this.buildFrozenSerial(requestDto, businessRecord, balance);
        serialService.handleSuccess(serialDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recharge(FundRequestDto fundRequestDto) {
        AbstractRechargeManager rechargeManager = rechargeFactory.getRechargeManager(fundRequestDto.getTradeChannel());
        rechargeManager.doRecharge(fundRequestDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRecharge(FundRequestDto requestDto) {
        AbstractRechargeManager rechargeManager = rechargeFactory.getRechargeManager(requestDto.getTradeChannel());
        rechargeManager.doPreRecharge(requestDto);
    }


    /**
     * 构建提现流水 后期根据各业务代码调整优化
     * @param fundRequestDto
     * @return
     */
    private SerialDto buildWithdrawSerial(FundRequestDto fundRequestDto, BusinessRecordDo businessRecord, TradeResponseDto withdrawResponse) {
        SerialDto serialDto = new SerialDto();
        serialDto.setSerialNo(businessRecord.getSerialNo());
        serialDto.setStartBalance(withdrawResponse.getBalance());
        serialDto.setEndBalance(withdrawResponse.getBalance() + withdrawResponse.getAmount());
        if (!CollUtil.isEmpty(withdrawResponse.getStreams())) {
            List<FeeItemDto> feeItemList = withdrawResponse.getStreams();
            List<SerialRecordDo> serialRecordList = new ArrayList<>(feeItemList.size());
            for (FeeItemDto feeItem : feeItemList) {
                SerialRecordDo serialRecord = new SerialRecordDo();
                serialService.copyCommonFields(serialRecord, businessRecord);
                serialRecord.setAction(feeItem.getAmount() < 0L ? ActionType.EXPENSE.getCode() : ActionType.INCOME.getCode());
                serialRecord.setStartBalance(feeItem.getBalance());
                serialRecord.setAmount(Math.abs(feeItem.getAmount()));//由于是通过标记位表示收入或支出，固取绝对值
                serialRecord.setEndBalance(feeItem.getBalance() + feeItem.getAmount());
                if (Integer.valueOf(FeeType.ACCOUNT.getCode()).equals(feeItem.getType())) {//账户资金
                    if (Integer.valueOf(TradeChannel.CASH.getCode()).equals(fundRequestDto.getTradeChannel())) {
                        serialRecord.setFundItem(FundItem.CASH_WITHDRAW.getCode());
                        serialRecord.setFundItemName(FundItem.CASH_WITHDRAW.getName());
                    }
                    if (Integer.valueOf(TradeChannel.E_BANK.getCode()).equals(fundRequestDto.getTradeChannel())) {
                        serialRecord.setFundItem(FundItem.EBANK_WITHDRAW.getCode());
                        serialRecord.setFundItemName(FundItem.EBANK_WITHDRAW.getName());
                    }
                }
                if (Integer.valueOf(FeeType.SERVICE.getCode()).equals(feeItem.getType())) {//手续费
                    if (Integer.valueOf(TradeChannel.E_BANK.getCode()).equals(fundRequestDto.getTradeChannel())) {
                        serialRecord.setFundItem(FundItem.EBANK_SERVICE.getCode());
                        serialRecord.setFundItemName(FundItem.EBANK_SERVICE.getName());
                    }
                }
                /** 操作时间-与支付系统保持一致 */
                serialRecord.setOperateTime(withdrawResponse.getWhen());
                //serialRecord.setNotes();
                serialRecordList.add(serialRecord);
            }
            serialDto.setSerialRecordList(serialRecordList);
        }
        return serialDto;
    }

    private SerialDto buildFrozenSerial(FundRequestDto requestDto, BusinessRecordDo businessRecord, BalanceResponseDto balance) {
        SerialDto serialDto = new SerialDto();
        serialDto.setSerialNo(businessRecord.getSerialNo());

        List<SerialRecordDo> serialRecordList = new ArrayList<>();

        SerialRecordDo serialRecord = new SerialRecordDo();
        serialService.copyCommonFields(serialRecord, businessRecord);
        serialRecord.setAction(ActionType.EXPENSE.getCode());
        serialRecord.setStartBalance(balance.getAvailableAmount());
        serialRecord.setAmount(Math.abs(requestDto.getAmount()));
        serialRecord.setEndBalance(balance.getAvailableAmount() - serialRecord.getAmount());
        serialRecord.setOperateTime(LocalDateTime.now());
        serialRecord.setNotes("手工冻结资金");
        serialRecordList.add(serialRecord);

        serialDto.setSerialRecordList(serialRecordList);

        return serialDto;
    }

    @Override
	public void unfrozen(UnfreezeFundDto unfreezeFundDto) {
		AccountWithAssociationResponseDto accountInfo = GenericRpcResolver.resolver(accountQueryRpc.findAssociation(unfreezeFundDto.getAccountId()),"account-service");
		for (Long tradeNo : unfreezeFundDto.getTradeNos()) {
			//对应支付的frozenId
			UnfreezeFundDto dto = new UnfreezeFundDto();
			dto.setFrozenId(tradeNo);
			GenericRpcResolver.resolver(payRpc.unfrozenFund(dto), "pay-service");
			//保存操作记录
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
	
	
	/**
	 * 生成全局日志
	 * 
	 * @param openCardInfo
	 * @param accountId
	 */
	private SerialRecordDo buildSerialRecord(AccountWithAssociationResponseDto accountInfo,UnfreezeFundDto unfreezeFundDto) {
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

	@Override
	public PageOutput<FundUnfrozenRecordDto> unfrozenRecord(UnfreezeFundDto unfreezeFundDto) {
		// 从支付查询  默认查询从当日起一年内的未解冻记录
		
		return null;
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

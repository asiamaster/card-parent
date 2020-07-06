package com.dili.card.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.BalanceRequestDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.FeeItemDto;
import com.dili.card.dto.pay.WithdrawRequestDto;
import com.dili.card.dto.pay.WithdrawResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.*;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IFundService;
import com.dili.card.service.IPayService;
import com.dili.card.service.ISerialService;
import com.dili.card.service.recharge.AbstractRechargeManager;
import com.dili.card.service.recharge.RechargeFactory;
import com.dili.card.service.recharge.TradeContextHolder;
import com.dili.card.type.ActionType;
import com.dili.card.type.CardStatus;
import com.dili.card.type.FeeType;
import com.dili.card.type.FundItem;
import com.dili.card.type.OperateType;
import com.dili.card.type.TradeChannel;
import com.dili.card.type.TradeType;
import com.dili.card.util.CurrencyUtils;
import com.dili.ss.constant.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
        businessRecord.setNotes(fundRequestDto.getServiceCost() == null ? null : String.format("手续费%s元", CurrencyUtils.toYuanWithStripTrailingZeros(fundRequestDto.getServiceCost())));
        serialService.saveBusinessRecord(businessRecord);
        if (Integer.valueOf(TradeChannel.CASH.getCode()).equals(fundRequestDto.getTradeChannel())) {
            accountCycleService.decreaseeCashBox(businessRecord.getCycleNo(), fundRequestDto.getAmount());
        }
        //提现提交
        WithdrawRequestDto withdrawRequest = new WithdrawRequestDto();
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
        WithdrawResponseDto withdrawResponse = payService.commitWithdraw(withdrawRequest);
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

        businessRecord.setType(OperateType.FROZEN_FUND.getCode());
        businessRecord.setAmount(requestDto.getAmount());
        businessRecord.setNotes(requestDto.getMark());
        serialService.saveBusinessRecord(businessRecord);

        payService.frozenFund(accountCard.getFundAccountId(), requestDto.getAmount());

        SerialDto serialDto = new SerialDto();
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
    private SerialDto buildWithdrawSerial(FundRequestDto fundRequestDto, BusinessRecordDo businessRecord, WithdrawResponseDto withdrawResponse) {
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
}

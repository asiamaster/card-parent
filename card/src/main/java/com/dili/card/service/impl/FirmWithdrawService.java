package com.dili.card.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.dto.FirmWithdrawAuthRequestDto;
import com.dili.card.dto.FirmWithdrawInitResponseDto;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.PipelineRecordQueryDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.FeeItemDto;
import com.dili.card.dto.pay.MerAccountResponseDto;
import com.dili.card.dto.pay.PipelineRecordParam;
import com.dili.card.dto.pay.PipelineRecordResponseDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.entity.bo.MessageBo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.exception.ErrorCode;
import com.dili.card.rpc.PayRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.service.IBindBankCardService;
import com.dili.card.service.IFirmWithdrawService;
import com.dili.card.service.IPayService;
import com.dili.card.service.ISerialService;
import com.dili.card.service.withdraw.BankWithdrawServiceImpl;
import com.dili.card.type.ActionType;
import com.dili.card.type.BankWithdrawState;
import com.dili.card.type.BizNoType;
import com.dili.card.type.OperateState;
import com.dili.card.type.OperateType;
import com.dili.card.type.PayPipelineType;
import com.dili.card.type.PaySubject;
import com.dili.card.type.TradeChannel;
import com.dili.card.type.TradeType;
import com.dili.card.util.CurrencyUtils;
import com.dili.ss.domain.PageOutput;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.rpc.FirmRpc;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/1/22 16:00
 * @Description:
 */
@Service
public class FirmWithdrawService implements IFirmWithdrawService {
    @Autowired
    private IBindBankCardService bindBankCardService;
    @Autowired
    private PayRpcResolver payRpcResolver;
    @Autowired
    private FirmRpc firmRpc;
    @Autowired
    private PayRpc payRpc;
    @Autowired
    private BankWithdrawServiceImpl bankWithdrawService;
    @Autowired
    private UidRpcResovler uidRpcResovler;
    @Autowired
    private IPayService payService;
    @Autowired
    private ISerialService serialService;

    @Override
    public FirmWithdrawInitResponseDto init(Long firmId) {
        // 查询市场信息
        Firm firm = GenericRpcResolver.resolver(firmRpc.getById(firmId),
                ServiceName.UAP);
        // 查询商户信息
        MerAccountResponseDto merInfo = this.getMerInfo(firmId);
        //市场余额信息
        BalanceResponseDto balanceInfo = payRpcResolver.findBalanceByFundAccountId(merInfo.getVouchAccount());
        FirmWithdrawInitResponseDto result = new FirmWithdrawInitResponseDto();
        result.setMerInfo(merInfo);
        result.setFirm(firm);
        result.setBalanceInfo(balanceInfo);
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessageBo<String> doMerWithdraw(FundRequestDto requestDto) {
        //参数校验
        bankWithdrawService.validateSpecial(requestDto);
        MerAccountResponseDto merInfo = this.getMerInfo(requestDto.getFirmId());
        Long accountId = merInfo.getVouchAccount();
        BalanceResponseDto balance = payRpcResolver.findBalanceByFundAccountId(accountId);
        long totalAmount = requestDto.getAmount();
        if (totalAmount > balance.getAvailableAmount()) {
            throw new CardAppBizException("可用余额不足");
        }
        BusinessRecordDo businessRecord = this.createFirmRecord(merInfo, requestDto);
        //构建创建交易参数
        CreateTradeRequestDto createTradeRequest = CreateTradeRequestDto.createTrade(
                TradeType.BANK_WITHDRAW.getCode(),
                accountId, accountId,
                requestDto.getAmount(),
                businessRecord.getSerialNo(),
                String.valueOf(businessRecord.getCycleNo()));
        createTradeRequest.setDescription(PaySubject.WITHDRAW.getName());
        //创建交易
        String tradeNo = payService.createTrade(createTradeRequest);
        businessRecord.setTradeNo(tradeNo);
        //先异步保存一条记录，防止被事务回滚
        ThreadUtil.execute(() -> {
            //保存业务办理记录
            serialService.saveBusinessRecord(businessRecord);
        });
        //提现提交
        UserAccountCardResponseDto accountCard = new UserAccountCardResponseDto();
        accountCard.setAccountId(accountId);
        accountCard.setFundAccountId(accountId);
        TradeRequestDto withdrawRequest = TradeRequestDto.createTrade(accountCard,
                tradeNo, requestDto.getChannelAccount().getToBankType(), requestDto.getTradePwd());
        withdrawRequest.setChannelAccount(requestDto.getChannelAccount());
        TradeResponseDto withdrawResponse = payService.commitWithdraw(withdrawRequest);

        int payState = NumberUtils.toInt(withdrawResponse.getState() + "", BankWithdrawState.SUCCESS.getCode());
        if (payState == BankWithdrawState.SUCCESS.getCode()) {
            return MessageBo.success(businessRecord.getSerialNo());
        }
        return MessageBo.fail(ErrorCode.GENERAL_CODE, withdrawResponse.getMessage(), businessRecord.getSerialNo());
    }

    @Override
    public void checkAuth(FirmWithdrawAuthRequestDto requestDto) {
        GenericRpcResolver.resolver(payRpc.checkTradePwd(requestDto), ServiceName.PAY);
    }

    @Override
    public PageOutput<List<PipelineRecordResponseDto>> bankWithdrawPage(PipelineRecordQueryDto param) {
        PipelineRecordParam query = new PipelineRecordParam();
        query.setType(PayPipelineType.BANK_WITHDRAW.getCode());
        query.setState(BankWithdrawState.SUCCESS.getCode());
        query.setAccountId(param.getAccountId());
        Date now = new Date();
        DateTime start = DateUtil.offsetMonth(now, -36);
        query.setStartDate(start.toString("yyyy-MM-dd"));
        query.setEndDate(DateUtil.format(now, "yyyy-MM-dd"));
        query.setPageNo(param.getPage());
        query.setPageSize(param.getRows());
        PageOutput<List<PipelineRecordResponseDto>> result = GenericRpcResolver.resolver(payRpc.pipelineList(query), ServiceName.PAY);
        List<PipelineRecordResponseDto> data = result.getData();
        for (PipelineRecordResponseDto dto : data) {
            dto.setOperatorId(param.getOpId());
            dto.setOperatorName(param.getOpName());
        }
        return result;
    }

    private MerAccountResponseDto getMerInfo(Long firmId) {
        JSONObject params = new JSONObject();
        params.put("mchId", firmId);
        MerAccountResponseDto merInfo = GenericRpcResolver.resolver(payRpc.getMerAccount(params),
                ServiceName.PAY);
        if (merInfo == null) {
            throw new CardAppBizException("市场商户信息不存在");
        }
        return merInfo;
    }

    /**
     *  由于市场商户没有account相关信息，这里需要特殊构建
     * @author miaoguoxin
     * @date 2021/1/27
     */
    private BusinessRecordDo createFirmRecord(MerAccountResponseDto merInfo, FundRequestDto requestDto) {
        BusinessRecordDo businessRecord = new BusinessRecordDo();
        //编号、卡号、账户id
        businessRecord.setSerialNo(uidRpcResovler.bizNumber(BizNoType.OPERATE_SERIAL_NO.getCode()));
        businessRecord.setAccountId(merInfo.getVouchAccount());
        businessRecord.setCardNo("0");
        businessRecord.setCustomerId(merInfo.getMchId());
        businessRecord.setCustomerNo(merInfo.getCode());
        businessRecord.setCustomerName(merInfo.getName());
        businessRecord.setCycleNo(merInfo.getVouchAccount());
        //操作员信息
        businessRecord.setOperatorId(requestDto.getOpId());
        businessRecord.setOperatorNo(requestDto.getOpNo());
        businessRecord.setOperatorName(requestDto.getOpName());
        businessRecord.setFirmId(requestDto.getFirmId());

        LocalDateTime localDateTime = LocalDateTime.now();
        businessRecord.setState(OperateState.PROCESSING.getCode());
        businessRecord.setOperateTime(localDateTime);
        businessRecord.setModifyTime(localDateTime);
        businessRecord.setVersion(1);

        businessRecord.setType(OperateType.ACCOUNT_WITHDRAW.getCode());
        businessRecord.setAmount(requestDto.getAmount());
        businessRecord.setTradeType(TradeType.WITHDRAW.getCode());
        businessRecord.setTradeChannel(TradeChannel.BANK.getCode());
        businessRecord.setServiceCost(requestDto.getServiceCost());
        businessRecord.setNotes(String.format("圈提取款，手续费%s元", CurrencyUtils.toYuanWithStripTrailingZeros(requestDto.getServiceCost())));
        return businessRecord;
    }


    private SerialDto createSerialRecord(BusinessRecordDo businessRecord, TradeResponseDto tradeResponseDto) {
        Long totalFrozenAmount = tradeResponseDto.countTotalFrozenAmount();
        SerialDto serialDto = new SerialDto();
        serialDto.setSerialNo(businessRecord.getSerialNo());
        serialDto.setStartBalance(NumberUtil.sub(tradeResponseDto.getBalance(), totalFrozenAmount).longValue());
        serialDto.setEndBalance(NumberUtil.sub(serialDto.getStartBalance(), businessRecord.getAmount()).longValue());
        serialDto.setTotalBalance(NumberUtil.sub(tradeResponseDto.getBalance(), businessRecord.getAmount()).longValue());

        if (!CollUtil.isEmpty(tradeResponseDto.getStreams())) {
            List<FeeItemDto> feeItemList = tradeResponseDto.getStreams();
            List<SerialRecordDo> serialRecordList = new ArrayList<>(feeItemList.size());
            for (FeeItemDto feeItem : feeItemList) {
                SerialRecordDo serialRecord = new SerialRecordDo();
                serialService.copyCommonFields(serialRecord, businessRecord);
                serialRecord.setAction(ActionType.EXPENSE.getCode());
                serialRecord.setAmount(Math.abs(feeItem.getAmount()));
                serialRecord.setStartBalance(NumberUtil.sub(feeItem.getBalance(), totalFrozenAmount).longValue());
                //支付返回的流水有正负号
                serialRecord.setEndBalance(NumberUtil.add(serialRecord.getStartBalance(), feeItem.getAmount()).longValue());
                // 操作时间-与支付系统保持一致
                serialRecord.setOperateTime(tradeResponseDto.getWhen());
                serialRecordList.add(serialRecord);
            }
            serialDto.setSerialRecordList(serialRecordList);
        }

        return serialDto;
    }
}

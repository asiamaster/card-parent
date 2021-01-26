package com.dili.card.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.dto.BindBankCardDto;
import com.dili.card.dto.FirmWithdrawInitResponseDto;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.PipelineRecordQueryDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.MerAccountResponseDto;
import com.dili.card.dto.pay.PipelineRecordParam;
import com.dili.card.dto.pay.PipelineRecordResponseDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.PayRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.service.IBindBankCardService;
import com.dili.card.service.IFirmWithdrawService;
import com.dili.card.service.IPayService;
import com.dili.card.service.withdraw.BankWithdrawServiceImpl;
import com.dili.card.type.BankWithdrawState;
import com.dili.card.type.BizNoType;
import com.dili.card.type.PayPipelineType;
import com.dili.card.type.TradeType;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.PageOutput;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.rpc.FirmRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void doMerWithdraw(FundRequestDto requestDto) {
        //参数校验
        bankWithdrawService.validateSpecial(requestDto);
        MerAccountResponseDto merInfo = this.getMerInfo(requestDto.getFirmId());
        Long accountId = merInfo.getVouchAccount();
        BalanceResponseDto balance = payRpcResolver.findBalanceByFundAccountId(accountId);
        long totalAmount = requestDto.getAmount();
        if (totalAmount > balance.getAvailableAmount()) {
            throw new CardAppBizException("可用余额不足");
        }
        //构建创建交易参数
        CreateTradeRequestDto createTradeRequest = CreateTradeRequestDto.createTrade(
                TradeType.BANK_WITHDRAW.getCode(),
                accountId, accountId,
                requestDto.getAmount(),
                uidRpcResovler.bizNumber(BizNoType.OPERATE_SERIAL_NO.getCode()),
                null);
        //创建交易
        String tradeNo = payService.createTrade(createTradeRequest);
        //提现提交
        UserAccountCardResponseDto accountCard = new UserAccountCardResponseDto();
        accountCard.setAccountId(accountId);
        accountCard.setFundAccountId(accountId);
        TradeRequestDto withdrawRequest = TradeRequestDto.createTrade(accountCard,
                tradeNo, requestDto.getChannelAccount().getToBankType(), requestDto.getTradePwd());
        withdrawRequest.setChannelAccount(requestDto.getChannelAccount());
        payService.commitWithdraw(withdrawRequest);
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
        if (merInfo == null){
            throw new CardAppBizException("市场商户信息不存在");
        }
        return merInfo;
    }
}

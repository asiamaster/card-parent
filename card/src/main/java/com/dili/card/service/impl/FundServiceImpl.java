package com.dili.card.service.impl;

import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.FeeItemDto;
import com.dili.card.dto.pay.WithdrawRequestDto;
import com.dili.card.dto.pay.WithdrawResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.IFundService;
import com.dili.card.service.IPayService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.FeeType;
import com.dili.card.type.OperateType;
import com.dili.card.type.TradeType;
import com.dili.card.util.CurrencyUtils;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Resource
    private ISerialService serialService;
    @Resource
    private IPayService payService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void withdraw(FundRequestDto fundRequestDto) {
        BusinessRecordDo businessRecord = new BusinessRecordDo();
        serialService.buildCommonInfo(fundRequestDto, businessRecord);
        //构建创建交易参数
        CreateTradeRequestDto createTradeRequest = new CreateTradeRequestDto();
        createTradeRequest.setType(TradeType.WITHDRAW.getCode());
        createTradeRequest.setAccountId(fundRequestDto.getAccountId());
        createTradeRequest.setAmount(fundRequestDto.getAmount());
        createTradeRequest.setSerialNo(businessRecord.getSerialNo());
        createTradeRequest.setCycleNo(String.valueOf(businessRecord.getCycleNo()));
        createTradeRequest.setDescription("");
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
        //提现提交
        WithdrawRequestDto withdrawRequest = new WithdrawRequestDto();
        withdrawRequest.setTradeId(tradeNo);
        withdrawRequest.setAccountId(fundRequestDto.getAccountId());
        withdrawRequest.setChannelId(fundRequestDto.getTradeChannel());
        withdrawRequest.setPassword(fundRequestDto.getTradePwd());
        if (fundRequestDto.getServiceCost() > 0) {
            List<FeeItemDto> fees = new ArrayList<>();
            FeeItemDto feeItem = new FeeItemDto();
            feeItem.setAmount(fundRequestDto.getServiceCost());
            feeItem.setType(FeeType.SERVICE.getCode());
            feeItem.setTypeName(FeeType.SERVICE.getName());
            fees.add(feeItem);
            withdrawRequest.setFees(fees);
        }
        BaseOutput<WithdrawResponseDto> baseOutput = payService.commitWithdraw(withdrawRequest);
        if (!baseOutput.isSuccess()) {
            throw new CardAppBizException("", baseOutput.getMessage());
        }
        WithdrawResponseDto withdrawResponse = baseOutput.getData();
        try {
            SerialDto serialDto = buildWithdrawSerial(fundRequestDto, businessRecord, withdrawResponse);
            serialService.handleSuccess(serialDto);
        } catch (Exception e) {
            LOGGER.error("withdraw", e);
        }
    }

    /**
     * 构建提现流水 后期根据各业务代码调整优化
     * @param fundRequestDto
     * @return
     */
    private SerialDto buildWithdrawSerial(FundRequestDto fundRequestDto, BusinessRecordDo businessRecord, WithdrawResponseDto withdrawResponse) {
        SerialDto serialDto = new SerialDto();
        //TODO 待完善流水数据封装
        return serialDto;
    }
}

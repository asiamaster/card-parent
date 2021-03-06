package com.dili.card.service.withdraw;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.CacheKey;
import com.dili.card.common.constant.ReqParamExtra;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.ChannelAccountRequestDto;
import com.dili.card.dto.pay.FeeItemDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.bo.MessageBo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.exception.ErrorCode;
import com.dili.card.type.BankWithdrawState;
import com.dili.card.type.FeeType;
import com.dili.card.type.FundItem;
import com.dili.card.type.OperateType;
import com.dili.card.type.TradeChannel;
import com.dili.card.type.TradeType;
import com.dili.card.util.AssertUtils;
import com.dili.card.util.CurrencyUtils;
import com.dili.ss.redis.service.RedisUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 银行圈提
 */
@Service
public class BankWithdrawServiceImpl extends WithdrawServiceImpl {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void validateSpecial(FundRequestDto fundRequestDto) {
        fundRequestDto.setServiceCost(fundRequestDto.getServiceCost() == null ? 0L : fundRequestDto.getServiceCost());
        if (fundRequestDto.getServiceCost() < 0L) {
            throw new CardAppBizException("", "请正确输入手续费");
        }
        ChannelAccountRequestDto channelAccount = fundRequestDto.getChannelAccount();
        if (channelAccount == null) {
            throw new CardAppBizException("缺少提款银行卡相关参数");
        }
        AssertUtils.notNull(channelAccount.getToType(), "提款银行卡账户类型缺失");
        AssertUtils.notNull(channelAccount.getToBankType(), "提款银行卡类型缺失");
        AssertUtils.notEmpty(channelAccount.getToName(), "提款银行卡所属人缺失");
        AssertUtils.notEmpty(channelAccount.getToAccount(), "提款银行卡所属人id缺失");
    }


    @Override
    public BusinessRecordDo createBusinessRecord(FundRequestDto fundRequestDto, UserAccountCardResponseDto accountCard) {
        return serialService.createBusinessRecord(fundRequestDto, accountCard, temp -> {
            temp.setType(OperateType.ACCOUNT_WITHDRAW.getCode());
            temp.setAmount(fundRequestDto.getAmount());
            temp.setTradeType(TradeType.WITHDRAW.getCode());
            temp.setTradeChannel(fundRequestDto.getTradeChannel());
            temp.setServiceCost(fundRequestDto.getServiceCost());
            temp.setNotes(String.format("圈提取款，手续费%s元", CurrencyUtils.toYuanWithStripTrailingZeros(fundRequestDto.getServiceCost())));
        });
    }

    @Override
    protected Integer getChannelId(FundRequestDto fundRequestDto) {
        return fundRequestDto.getChannelAccount().getToBankType();
    }

    @Override
    public SerialDto createAccountSerial(FundRequestDto fundRequestDto, BusinessRecordDo businessRecord, TradeResponseDto withdrawResponse) {
        if (fundRequestDto.getServiceCost() == 0L) {//特殊处理为0时记录
            FeeItemDto feeItem = new FeeItemDto();
            feeItem.setType(FeeType.SERVICE.getCode());
            List<FeeItemDto> streams = withdrawResponse.getStreams();
            if (CollectionUtil.isEmpty(streams)) {
                withdrawResponse.setStreams(new ArrayList<>());
            }
            withdrawResponse.getStreams().add(feeItem);
        }
        return serialService.createAccountSerialWithFund(businessRecord, withdrawResponse, (serialRecord, feeType) -> {
            if (Integer.valueOf(FeeType.ACCOUNT.getCode()).equals(feeType)) {
                serialRecord.setFundItem(FundItem.BANK_WITHDRAW.getCode());
                serialRecord.setFundItemName(FundItem.BANK_WITHDRAW.getName());
            }
            if (Integer.valueOf(FeeType.SERVICE.getCode()).equals(feeType)) {
                serialRecord.setFundItem(FundItem.BANK_SERVICE.getCode());
                serialRecord.setFundItemName(FundItem.BANK_SERVICE.getName());
            }
        });
    }

    @Override
    public MessageBo<String> handleSerialAfterCommitWithdraw(FundRequestDto fundRequestDto, BusinessRecordDo businessRecord, TradeResponseDto withdrawResponse) {
        //以防空异常，默认成功
        int payState = NumberUtils.toInt(withdrawResponse.getState() + "", BankWithdrawState.SUCCESS.getCode());
        if (payState == BankWithdrawState.SUCCESS.getCode()) {
            return super.handleSerialAfterCommitWithdraw(fundRequestDto, businessRecord, withdrawResponse);
        }
        if (payState == BankWithdrawState.HANDING.getCode()) {
            //处理中的情况没有streams，无法构建流水，所以把请求参数缓存起来，等回调的时候重新构建流水数据
            SerialDto serialDto = this.createAccountSerial(fundRequestDto, businessRecord, withdrawResponse);
            String json = this.generateExtra(fundRequestDto, businessRecord);
            serialDto.setAttach(json);
            serialService.handleProcessing(serialDto);
            //缓存处理中的圈提，用于后续回调
            redisUtil.set(CacheKey.BANK_WITHDRAW_PROCESSING_SERIAL_PREFIX + serialDto.getSerialNo(),
                    json, 30L, TimeUnit.DAYS);
        }
        if (payState == BankWithdrawState.FAILED.getCode()) {
            SerialDto serialDto = this.createAccountSerial(fundRequestDto, businessRecord, withdrawResponse);
            serialService.handleFailure(serialDto);
        }
        return MessageBo.fail(ErrorCode.GENERAL_CODE, withdrawResponse.getMessage(), businessRecord.getSerialNo());
    }

    @Override
    public Integer support() {
        return TradeChannel.BANK.getCode();
    }

    /**
    * @author miaoguoxin
    * @date 2021/3/16
    */
    private String generateExtra(FundRequestDto requestDto, BusinessRecordDo recordDo) {
        JSONObject jObj = new JSONObject();
        jObj.put(ReqParamExtra.FUND_REQUEST, requestDto);
        jObj.put(ReqParamExtra.BUSINESS_RECORD, recordDo);
        return jObj.toJSONString();
    }
}

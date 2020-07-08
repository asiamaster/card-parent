package com.dili.card.service.recharge;

import cn.hutool.core.util.NumberUtil;
import com.dili.card.common.annotation.TradeChannelMark;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.type.TradeChannel;
import com.dili.ss.constant.ResultCode;
import org.springframework.stereotype.Component;

/**
 * pos鸡充值
 * @Auther: miaoguoxin
 * @Date: 2020/7/2 10:31
 */
@Component
@TradeChannelMark(TradeChannel.POS)
public class PosRechargeService extends AbstractRechargeManager {

    @Override
    public Long getRechargeAmount(FundRequestDto requestDto) {
        long amount = NumberUtil.sub(requestDto.getAmount(), requestDto.getServiceCost()).longValue();
        if (amount <= 0) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "非法的充值金额");
        }
        return amount;
    }

    @Override
    public TradeRequestDto recharge(FundRequestDto requestDto) {
        TradeRequestDto rechargeRequestDto = super.createRechargeRequestDto(requestDto);
        rechargeRequestDto.addServiceFeeItem(requestDto.getServiceCost());
        return rechargeRequestDto;
    }
}

package com.dili.card.service.recharge;

import com.dili.card.common.annotation.TradeChannelMark;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.type.TradeChannel;
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
    public void recharge(FundRequestDto requestDto) {

    }
}

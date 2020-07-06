package com.dili.card.service.recharge;

import cn.hutool.core.util.NumberUtil;
import com.dili.card.common.annotation.TradeChannelMark;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.pay.RechargeRequestDto;
import com.dili.card.type.TradeChannel;
import org.springframework.stereotype.Service;

/**
 * 网银充值（提现失败的时候进行逆向操作）
 * @Auther: miaoguoxin
 * @Date: 2020/7/2 10:33
 */
@Service
@TradeChannelMark(TradeChannel.E_BANK)
public class EBankRechargeService extends AbstractRechargeManager {

    @Override
    public Long getRechargeAmount(FundRequestDto requestDto) {
        return NumberUtil.add(requestDto.getAmount(), requestDto.getServiceCost()).longValue();
    }

    @Override
    public RechargeRequestDto recharge(FundRequestDto requestDto) {
        RechargeRequestDto rechargeRequestDto = super.createRechargeRequestDto(requestDto);
        rechargeRequestDto.addServiceFeeItem(requestDto.getServiceCost());
        return rechargeRequestDto;
    }
}

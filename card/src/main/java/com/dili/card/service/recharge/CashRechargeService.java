package com.dili.card.service.recharge;

import com.dili.card.common.annotation.TradeChannelMark;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.type.TradeChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 现金充值
 * @Auther: miaoguoxin
 * @Date: 2020/7/2 10:29
 */
@Component
@TradeChannelMark(TradeChannel.CASH)
public class CashRechargeService extends AbstractRechargeManager {

    @Autowired
    private IAccountCycleService accountCycleService;

    @Override
    public Long getRechargeAmount(FundRequestDto requestDto) {
        return requestDto.getAmount();
    }

    @Override
    public TradeRequestDto recharge(FundRequestDto requestDto) {
        BusinessRecordDo businessRecordDo = TradeContextHolder.getVal(TradeContextHolder.BUSINESS_RECORD_KEY, BusinessRecordDo.class);
        //添加现金资金池
        accountCycleService.increaseCashBox(businessRecordDo.getCycleNo(), requestDto.getAmount());
        return super.createRechargeRequestDto(requestDto);
    }

}

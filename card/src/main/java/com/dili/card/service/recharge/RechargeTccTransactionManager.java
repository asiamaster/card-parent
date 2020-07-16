package com.dili.card.service.recharge;

import com.dili.card.dto.FundRequestDto;
import com.dili.tcc.AbstractTccTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 充值tcc
 * @Auther: miaoguoxin
 * @Date: 2020/7/15 16:43
 */
@Service
public class RechargeTccTransactionManager extends AbstractTccTransactionManager<Boolean, FundRequestDto> {
    @Autowired
    private RechargeFactory rechargeFactory;

    @Override
    public void prepare(FundRequestDto requestDto) {
        AbstractRechargeManager rechargeManager = rechargeFactory.getRechargeManager(requestDto.getTradeChannel());
        rechargeManager.doPreRecharge(requestDto);
    }

    @Override
    public Boolean confirm(FundRequestDto requestDto) {
        AbstractRechargeManager rechargeManager = rechargeFactory.getRechargeManager(requestDto.getTradeChannel());
        rechargeManager.doRecharge(requestDto);
        return true;
    }

    @Override
    public void cancel(FundRequestDto requestDto) {

    }
}

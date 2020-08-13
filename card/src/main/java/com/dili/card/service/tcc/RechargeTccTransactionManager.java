package com.dili.card.service.tcc;

import com.dili.card.dto.FundRequestDto;
import com.dili.card.service.recharge.AbstractRechargeManager;
import com.dili.card.service.recharge.RechargeFactory;
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


    @Override
    public void prepare(FundRequestDto requestDto) {

    }

    @Override
    public Boolean confirm(FundRequestDto requestDto) {
        return true;
    }

    @Override
    public void cancel(FundRequestDto requestDto) {

    }
}

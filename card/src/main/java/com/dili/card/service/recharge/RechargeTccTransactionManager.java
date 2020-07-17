package com.dili.card.service.recharge;

import com.dili.card.common.constant.Constant;
import com.dili.card.dto.BaseDto;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.service.IAccountQueryService;
import com.dili.tcc.AbstractTccTransactionManager;
import com.dili.tcc.core.TccContextHolder;
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
    @Autowired
    private IAccountQueryService accountQueryService;

    @Override
    public void prepare(FundRequestDto requestDto) {
        UserAccountCardResponseDto userAccount = accountQueryService.getByAccountIdForRecharge(requestDto);
        TccContextHolder.get().addAttr(Constant.USER_ACCOUNT, userAccount);
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

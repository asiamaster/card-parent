package com.dili.card.service.recharge;

import com.dili.card.dto.FundRequestDto;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/2 10:22
 */
public abstract class AbstractRechargeManager implements IRechargeManager {

    /**
     * 充值操作入口封装
     * @author miaoguoxin
     * @date 2020/7/2
     */
    public void doRecharge(FundRequestDto requestDto) {
        this.recharge(requestDto);
        //记录日志
    }
}

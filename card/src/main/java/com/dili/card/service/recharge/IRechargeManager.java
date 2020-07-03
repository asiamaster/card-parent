package com.dili.card.service.recharge;

import com.dili.card.dto.FundRequestDto;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/2 10:20
 */
public interface IRechargeManager {
    /**
    * 充值顶层方法定义
    * @author miaoguoxin
    * @date 2020/7/2
    */
    void recharge(FundRequestDto requestDto);
}

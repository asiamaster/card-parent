package com.dili.card.service.recharge;

import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.pay.TradeRequestDto;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/2 10:20
 */
public interface IRechargeManager {
    /**
    * 预创建
    * @author miaoguoxin
    * @date 2020/7/6
    */
    Long getRechargeAmount(FundRequestDto requestDto);
    /**
    * 充值顶层方法定义
    * @author miaoguoxin
    * @date 2020/7/2
    */
    TradeRequestDto recharge(FundRequestDto requestDto);
}

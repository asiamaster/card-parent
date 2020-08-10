package com.dili.card.common.annotation;

import com.dili.card.type.TradeChannel;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 用于标记交易渠道类型 {@link com.dili.card.service.recharge.RechargeFactory}
 * @Auther: miaoguoxin
 * @Date: 2020/6/23 13:31
 */
@Target({TYPE})
@Retention(RUNTIME)
@Documented
public @interface TradeChannelMark {
    /**
    * 类型
    * @author miaoguoxin
    * @date 2020/7/2
    */
    TradeChannel value();
}

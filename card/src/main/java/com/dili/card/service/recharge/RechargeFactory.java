package com.dili.card.service.recharge;

import com.dili.card.common.annotation.TradeChannelMark;
import com.dili.card.exception.CardAppBizException;
import com.dili.ss.constant.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/2 10:20
 */
@Component
public class RechargeFactory implements InitializingBean {
    private static Logger LOGGER = LoggerFactory.getLogger(RechargeFactory.class);

    private Map<Integer, AbstractRechargeManager> RECHARGE_MAP = new ConcurrentHashMap<>();
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 获取充值策略
     * @param
     * @return
     * @author miaoguoxin
     * @date 2020/7/2
     */
    public AbstractRechargeManager getRechargeManager(Integer tradeChannel) {
        AbstractRechargeManager manager = RECHARGE_MAP.get(tradeChannel);
        if (manager == null) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "没有对应的充值策略");
        }
        return manager;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, AbstractRechargeManager> allRechargeBean = applicationContext.getBeansOfType(AbstractRechargeManager.class);
        for (AbstractRechargeManager value : allRechargeBean.values()) {
            TradeChannelMark mark = value.getClass().getAnnotation(TradeChannelMark.class);
            if (mark == null) {
                continue;
            }
            AbstractRechargeManager old = RECHARGE_MAP.putIfAbsent(mark.value().getCode(), value);
            if (old != null) {
                LOGGER.warn("the key [{}] has loaded which bean named [{}],but the new one [{}] putting again ",
                        mark.value().getCode(), old.getClass().getSimpleName(), value.getClass().getSimpleName());
            }
        }
    }
}

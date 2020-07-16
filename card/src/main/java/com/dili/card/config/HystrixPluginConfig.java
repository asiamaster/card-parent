package com.dili.card.config;

import com.dili.card.common.hystrix.ContextHystrixConcurrencyStrategy;
import com.dili.card.common.hystrix.HystrixCallableWrapper;
import com.dili.card.common.hystrix.SessionCallableWrapper;
import com.netflix.hystrix.strategy.HystrixPlugins;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于Hystrix的插件机制配置，{@link com.netflix.hystrix.strategy.HystrixPlugins}
 * @Auther: miaoguoxin
 * @Date: 2020/7/10 09:22
 */
@Configuration
@ConditionalOnProperty(name = "feign.hystrix.enabled", havingValue = "true")
public class HystrixPluginConfig {

    @Bean
    public HystrixCallableWrapper sessionCallableWrapper() {
        return new SessionCallableWrapper();
    }

    @Autowired(required = false)
    private List<HystrixCallableWrapper> wrappers = new ArrayList<>();


    @PostConstruct
    public void init() {
        HystrixPlugins.getInstance().registerConcurrencyStrategy(new ContextHystrixConcurrencyStrategy(wrappers));
    }
}

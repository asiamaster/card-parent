package com.dili.card.config;

import com.dili.card.common.hystrix.SessionCallableWrapper;
import com.dili.tcc.config.TccSpringCloudAutoConfiguration;
import com.dili.tcc.springcloud.ContextHystrixConcurrencyStrategy;
import com.dili.tcc.springcloud.HystrixCallableWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于Hystrix的插件机制配置，{@link com.netflix.hystrix.strategy.HystrixPlugins}
 * @Auther: miaoguoxin
 * @Date: 2020/7/10 09:22
 */
@Configuration
@ConditionalOnProperty(name = "feign.hystrix.enabled", havingValue = "true")
@AutoConfigureBefore(TccSpringCloudAutoConfiguration.HystrixTccConfig.class)
public class HystrixPluginConfig {

    @Bean
    public HystrixCallableWrapper sessionCallableWrapper() {
        return new SessionCallableWrapper();
    }

}

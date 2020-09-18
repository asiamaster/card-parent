package com.dili.tcc.config;

import java.util.List;

import com.dili.feign.hystrix.HystrixCallableWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dili.tcc.bean.TccTransaction;
import com.dili.tcc.core.EmptyTccResultInterceptor;
import com.dili.tcc.core.TccResultInterceptor;
import com.dili.tcc.disruptor.DisruptorBootstrap;
import com.dili.tcc.repository.RedisTccTransactionRepository;
import com.dili.tcc.repository.TccTransactionRepository;
import com.dili.tcc.serializer.KryoSerializer;
import com.dili.tcc.serializer.ObjectSerializer;
import com.dili.tcc.springcloud.TccFeignBeanPostProcessor;
import com.dili.tcc.util.SpringContext;

import feign.Feign;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/7 15:57
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Feign.class)
@EnableConfigurationProperties(TccProperties.class)
public class TccSpringCloudAutoConfiguration {


    /**
     *
     * @author miaoguoxin
     * @date 2020/7/7
     */
    @Bean
    public SpringContext springContext() {
        return new SpringContext();
    }

    /**
     * @author miaoguoxin
     * @date 2020/7/15
     */
    @Bean
    @ConditionalOnMissingBean
    public TccResultInterceptor defaultTccResultInterceptor() {
        return new EmptyTccResultInterceptor();
    }

    /**
     *
     * @author miaoguoxin
     * @date 2020/7/14
     */
    //@Bean
    public TccFeignBeanPostProcessor tccBeanPostProcessor() {
        return new TccFeignBeanPostProcessor();
    }


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(RedisAutoConfiguration.class)
    public TccTransactionRepository redisTccTransactionRepository() {
        return new RedisTccTransactionRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectSerializer kryoSerializer() {
        return new KryoSerializer();
    }

    @Bean
    public DisruptorBootstrap<TccTransaction> disruptorProvider(TccProperties tccProperties){
        return new DisruptorBootstrap<>(tccProperties);
    }
}

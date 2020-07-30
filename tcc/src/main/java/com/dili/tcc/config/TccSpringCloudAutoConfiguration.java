package com.dili.tcc.config;

import com.dili.tcc.core.EmptyTccResultInterceptor;
import com.dili.tcc.core.ITransactionRepository;
import com.dili.tcc.core.MemoryTransactionRepository;
import com.dili.tcc.core.TccResultInterceptor;
import com.dili.tcc.repository.RedisTccTransactionRepository;
import com.dili.tcc.repository.TccTransactionRepository;
import com.dili.tcc.springcloud.ContextHystrixConcurrencyStrategy;
import com.dili.tcc.springcloud.HystrixCallableWrapper;
import com.dili.tcc.springcloud.HystrixTccContextCallableWrapper;
import com.dili.tcc.springcloud.TccFeignBeanPostProcessor;
import com.dili.tcc.util.SpringContext;
import feign.Feign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/7 15:57
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Feign.class)
public class TccSpringCloudAutoConfiguration {


    /**
     * 存储远程执行信息
     * @author miaoguoxin
     * @date 2020/7/7
     */
    @Bean
    @ConditionalOnMissingBean
    public ITransactionRepository memoryTransactionRepository() {
        return new MemoryTransactionRepository();
    }

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
    @Bean
    // @ConditionalOnProperty(value = "feign.hystrix.enabled", havingValue = "false", matchIfMissing = true)
    public TccFeignBeanPostProcessor tccBeanPostProcessor() {
        return new TccFeignBeanPostProcessor();
    }


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedisAutoConfiguration.class)
    public TccTransactionRepository redisTccTransactionRepository(){
        return new RedisTccTransactionRepository();
    }

    @Configuration
    @ConditionalOnProperty(name = "feign.hystrix.enabled", havingValue = "true")
    public class HystrixTccConfig {

        @Bean
        public HystrixTccContextCallableWrapper hystrixTccContextCallableWrapper() {
            return new HystrixTccContextCallableWrapper();
        }

        @Bean
        public ContextHystrixConcurrencyStrategy contextHystrixConcurrencyStrategy(@Autowired(required = false) List<HystrixCallableWrapper> wrappers) {
            return new ContextHystrixConcurrencyStrategy(wrappers);
        }
    }

}

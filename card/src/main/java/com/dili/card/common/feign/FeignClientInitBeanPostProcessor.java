package com.dili.card.common.feign;

import cn.hutool.core.util.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/8/31 10:16
 * @Description:
 */
@Component
@ConditionalOnProperty(name = "ribbon.eager-load.enabled", havingValue = "true")
public class FeignClientInitBeanPostProcessor implements BeanPostProcessor {
    //TODO sentinel
    private static final String FEIGN_CLAZZ_NAME = "feign.ReflectiveFeign$FeignInvocationHandler";
    private static final String HYSTRIX_CLAZZ_NAME = "feign.hystrix.HystrixInvocationHandler";

    private static Logger LOGGER = LoggerFactory.getLogger(FeignClientInitBeanPostProcessor.class);
    @Autowired
    private SpringClientFactory springClientFactory;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!Proxy.isProxyClass(bean.getClass())) {
            return bean;
        }

        InvocationHandler handler = Proxy.getInvocationHandler(bean);
        if (!FEIGN_CLAZZ_NAME.equals(handler.getClass().getName())
                && !HYSTRIX_CLAZZ_NAME.equals(handler.getClass().getName())) {
            return bean;
        }

        try {
            FeignClient annotation = Class.forName(beanName).getAnnotation(FeignClient.class);
            if (annotation == null) {
                return bean;
            }
            String value = getServiceName(annotation);
            Method getContext = ReflectUtil.getMethod(springClientFactory.getClass(), "getContext", String.class);
            getContext.setAccessible(true);
            getContext.invoke(springClientFactory, value);
            LOGGER.info("执行feign client name:{} 初始化加载", value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("invoke springClientFactory failed", e);
        } catch (Exception e) {
            LOGGER.error("get originalBean failed", e);
        }
        return bean;
    }

    private static String getServiceName(FeignClient feignClient) {
        String name = feignClient.name();
        String value = feignClient.value();
        return StringUtils.isNoneBlank(name) ? name : value;
    }
}

package com.dili.tcc.springcloud;

import com.dili.tcc.springcloud.TccFeignInvocationHandler;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.openfeign.FeignClient;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 重新替换feign的invocationHandler
 * @Auther: miaoguoxin
 * @Date: 2020/7/13 20:57
 */
public class TccFeignBeanPostProcessor implements BeanPostProcessor, InitializingBean {
    private static final String FEIGN_CLAZZ_NAME = "feign.ReflectiveFeign$FeignInvocationHandler";

    private Field singletonTargetSourceTargetField = null;

    public void afterPropertiesSet() throws Exception {
        Field field = SingletonTargetSource.class.getDeclaredField("target");
        field.setAccessible(true);
        this.singletonTargetSourceTargetField = field;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!Proxy.isProxyClass(bean.getClass())) {
            return bean;
        }

        TargetSource targetSource = null;
        Object object = bean;
        if (bean instanceof Advised) {
            Advised advised = (Advised) bean;

            Class<?>[] interfaces = advised.getProxiedInterfaces();
            for (Class<?> intf : interfaces) {
                if (intf.getAnnotation(FeignClient.class) != null) {
                    targetSource = advised.getTargetSource();
                    try {
                        object = targetSource.getTarget();
                        break;
                    } catch (Exception error) {
                        throw new IllegalStateException();
                    }
                }
            }
        }
        InvocationHandler handler = Proxy.getInvocationHandler(object);

        if (targetSource == null
                && !FEIGN_CLAZZ_NAME.equals(handler.getClass().getName())) {
            return bean;
        }

        Object proxied = this.createProxiedObject(object);
        if (targetSource == null) {
            return proxied;
        }

        if (targetSource instanceof SingletonTargetSource) {
            try {
                this.singletonTargetSourceTargetField.set(targetSource, proxied);
            } catch (IllegalArgumentException | IllegalAccessException error) {
                throw new IllegalStateException("Error occurred!");
            }
        } else {
            throw new IllegalStateException("Not supported yet!");
        }
        return bean;
    }


    private Object createProxiedObject(Object origin) {
        InvocationHandler handler = Proxy.getInvocationHandler(origin);

        TccFeignInvocationHandler feignHandler = new TccFeignInvocationHandler(handler);

        Class<?> clazz = origin.getClass();
        Class<?>[] interfaces = clazz.getInterfaces();
        ClassLoader loader = clazz.getClassLoader();
        return Proxy.newProxyInstance(loader, interfaces, feignHandler);
    }
}

package com.dili.card.common.hystrix;

import java.util.concurrent.Callable;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.dili.tcc.springcloud.HystrixCallableWrapper;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/10 09:25
 * @Description:
 */
public class SessionCallableWrapper implements HystrixCallableWrapper {

    @Override
    public <T> Callable<T> wrap(Callable<T> callable) {
        // LOGGER.info("当前线程:{}",Thread.currentThread());
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return () -> {
            try {
                if (requestAttributes != null) {
                    RequestContextHolder.setRequestAttributes(requestAttributes);
                }
                return callable.call();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        };
    }
}

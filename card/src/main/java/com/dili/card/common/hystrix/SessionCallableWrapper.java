package com.dili.card.common.hystrix;

import com.dili.feign.hystrix.HystrixCallableWrapper;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Callable;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/10 09:25
 * @Description: 用于传递Session
 */
public class SessionCallableWrapper implements HystrixCallableWrapper {

    @Override
    public <T> Callable<T> wrap(Callable<T> callable) {
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

package com.dili.card.common.hystrix;

import com.dili.card.common.constant.Constant;
import com.dili.tcc.springcloud.HystrixCallableWrapper;
import com.dili.uap.sdk.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Callable;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/10 09:25
 * @Description:
 */
public class SessionCallableWrapper implements HystrixCallableWrapper {
    private static Logger LOGGER = LoggerFactory.getLogger(SessionCallableWrapper.class);

    @Override
    public <T> Callable<T> wrap(Callable<T> callable) {
       // LOGGER.info("当前线程:{}",Thread.currentThread());
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            requestAttributes.setAttribute(Constant.SESSION, SessionContext.getSessionContext(), RequestAttributes.SCOPE_REQUEST);
        }
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

package com.dili.card.common.hystrix;

import com.dili.card.common.constant.Constant;
import com.dili.tcc.springcloud.HystrixCallableWrapper;
import com.dili.uap.sdk.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/10 09:25
 * @Description:
 */
public class SessionCallableWrapper implements HystrixCallableWrapper {
    private static final String USER_TICKET = "userTicket";
    private static Logger LOGGER = LoggerFactory.getLogger(SessionCallableWrapper.class);

    @Override
    public <T> Callable<T> wrap(Callable<T> callable) {
        // LOGGER.info("当前线程:{}",Thread.currentThread());
        SessionContext sessionContext = SessionContext.getSessionContext();
        RequestAttributes requestAttributes = null;
        try {
            requestAttributes = RequestContextHolder.getRequestAttributes();
            Field userTicketField = sessionContext.getClass().getDeclaredField(USER_TICKET);
            userTicketField.setAccessible(true);
            if (userTicketField.get(sessionContext) != null && requestAttributes != null) {
                requestAttributes.setAttribute(Constant.SESSION, sessionContext, RequestAttributes.SCOPE_REQUEST);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        RequestAttributes finalRequestAttributes = requestAttributes;
        return () -> {
            try {
                if (finalRequestAttributes != null) {
                    RequestContextHolder.setRequestAttributes(finalRequestAttributes);
                }
                return callable.call();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        };
    }
}

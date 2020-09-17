package com.dili.card.common.hystrix;

import com.dili.card.common.constant.Constant;
import com.dili.tcc.springcloud.HystrixCallableWrapper;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Callable;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/10 09:25
 * @Description:
 */
public class SessionCallableWrapper implements HystrixCallableWrapper {

    @Override
    public <T> Callable<T> wrap(Callable<T> callable) {
        // LOGGER.info("当前线程:{}",Thread.currentThread());
        //SessionContext没有set方法，所以这里要用request重新包装一下
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            requestAttributes.setAttribute(Constant.SESSION_TICKET, userTicket, RequestAttributes.SCOPE_REQUEST);
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

package com.dili.tcc.springcloud;

import com.dili.tcc.common.Tcc;
import com.dili.tcc.common.TccRemoteInfo;
import com.dili.tcc.core.TccContext;
import com.dili.tcc.core.TccContextHolder;
import com.dili.tcc.core.TccResultInterceptor;
import com.dili.tcc.util.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/6 15:57
 */
public class TccFeignInvocationHandler implements InvocationHandler {
    protected static Logger LOGGER = LoggerFactory.getLogger(TccFeignInvocationHandler.class);
    /**feign的代理类，FeignInvocationHandler 或者 HystrixInvocationHandler*/
    private final InvocationHandler delegate;

    public TccFeignInvocationHandler(InvocationHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        //方法上标记TCC操作，因为并不是每个rpc都需要重试，比如读操作
        Tcc isTcc = method.getAnnotation(Tcc.class);
        if (isTcc == null) {
            return delegate.invoke(proxy, method, args);
        }

        TccContext tccContext = TccContextHolder.get();
        if (tccContext == null) {
            return delegate.invoke(proxy, method, args);
        }

        Object result;
        //跳过已执行成功的方法
        TccRemoteInfo tccRemoteInfo = new TccRemoteInfo(this, method, args, null);
        TccRemoteInfo remote = tccContext.getRemote(tccRemoteInfo);
        if (remote != null) {
            result = remote.getResult();
        } else {
//                String cid  = UUID.randomUUID().toString();
//                tccContext.getTransactionId().addCid(cid);
            result = delegate.invoke(proxy, method, args);
            TccResultInterceptor interceptor = SpringContext.getBean(TccResultInterceptor.class);
            //这里需要判断一下，只有正确返回的结果才添加到remote列表中
            if (interceptor.doExtra(result)) {
                LOGGER.info("当前远程分支执行成功:{}", tccRemoteInfo.getMethod().getName());
                tccContext.addRemote(tccRemoteInfo);
            }
        }
        return result;
    }
}

package com.dili.tcc.common;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/7 15:07
 * @Description:
 */
public class TccRemoteInfo {
    /**代理Handler*/
    private InvocationHandler handler;
    /**执行的方法*/
    private Method method;
    /**执行参数*/
    private Object[] args;
    /**执行结果*/
    private Object result;
    /**是否报错*/
    private boolean isError;

    public TccRemoteInfo(InvocationHandler handler, Method method, Object[] args, Object result) {
        this.handler = handler;
        this.method = method;
        this.args = args;
        this.result = result;
        if (result instanceof Throwable) {
            this.isError = true;
        }
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getResult() {
        return result;
    }

    public boolean isError() {
        return isError;
    }

    public InvocationHandler getHandler() {
        return handler;
    }
}

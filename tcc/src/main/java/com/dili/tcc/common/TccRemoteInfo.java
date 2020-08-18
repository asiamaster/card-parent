package com.dili.tcc.common;

import java.lang.reflect.Method;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/7 15:07
 * @Description:
 */
public class TccRemoteInfo {
    /**执行的方法*/
    private Method method;
    /**执行参数*/
    private Object[] args;
    /**执行结果*/
    private Object result;
    /**是否报错*/
    private boolean isSuccess;

    public TccRemoteInfo(Method method, Object[] args) {
        this.method = method;
        this.args = args;
    }

    public TccRemoteInfo(Method method, Object[] args, Object result) {
        this.method = method;
        this.args = args;
        this.result = result;
        if (!(result instanceof Throwable)) {
            this.isSuccess = true;
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

    public boolean isSuccess() {
        return isSuccess;
    }

}

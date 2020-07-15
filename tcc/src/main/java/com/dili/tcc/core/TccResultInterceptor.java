package com.dili.tcc.core;

/**
 * 用于拦截tcc执行的结果
 * @Auther: miaoguoxin
 * @Date: 2020/7/15 10:48
 */
public interface TccResultInterceptor {
    /**
    * 判断执行结果
    * @author miaoguoxin
    * @date 2020/7/15
    */
    boolean doExtra(Object result);
}

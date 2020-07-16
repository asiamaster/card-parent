package com.dili.tcc.core;

/**
 * 空实现，防止getBean抛错，简单处理一下
 * @Auther: miaoguoxin
 * @Date: 2020/7/15 10:49
 */
public class EmptyTccResultInterceptor implements TccResultInterceptor {
    @Override
    public boolean doExtra(Object result) {
        return true;
    }
}

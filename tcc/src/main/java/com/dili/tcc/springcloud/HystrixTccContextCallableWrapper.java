package com.dili.tcc.springcloud;

import com.dili.tcc.core.TccContext;
import com.dili.tcc.core.TccContextHolder;

import java.util.concurrent.Callable;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/17 09:14
 */
public class HystrixTccContextCallableWrapper implements HystrixCallableWrapper {

    @Override
    public <T> Callable<T> wrap(Callable<T> callable) {
        TccContext tccContext = TccContextHolder.get();

        return () -> {
            try {
                TccContextHolder.set(tccContext);
                return callable.call();
            } finally {
                TccContextHolder.remove();
            }
        };
    }
}

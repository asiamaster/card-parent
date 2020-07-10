package com.dili.card.common.hystrix;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;

/**
 * 用来包装多个ConcurrencyStrategy，用于传递threadLocal的上下文
 * @Auther: miaoguoxin
 * @Date: 2020/7/10 09:15
 */
public class ContextHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

    private final Collection<HystrixCallableWrapper> wrappers;

    public ContextHystrixConcurrencyStrategy(Collection<HystrixCallableWrapper> wrappers) {
        this.wrappers = wrappers;
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        return new CallableWrapperChain<>(callable, wrappers.iterator()).wrapCallable();
    }

    private static class CallableWrapperChain<T> {

        private final Callable<T> callable;

        private final Iterator<HystrixCallableWrapper> wrappers;

        CallableWrapperChain(Callable<T> callable, Iterator<HystrixCallableWrapper> wrappers) {
            this.callable = callable;
            this.wrappers = wrappers;
        }

        Callable<T> wrapCallable() {
            Callable<T> delegate = callable;
            while (wrappers.hasNext()) {
                delegate = wrappers.next().wrap(delegate);
            }
            return delegate;
        }
    }

}

package com.dili.tcc.springcloud;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 用来包装多个ConcurrencyStrategy，用于传递threadLocal的上下文
 * @Auther: miaoguoxin
 * @Date: 2020/7/10 09:15
 */
public class ContextHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

    private final Collection<HystrixCallableWrapper> wrappers;

    private HystrixConcurrencyStrategy delegate;

    public ContextHystrixConcurrencyStrategy(List<HystrixCallableWrapper> wrappers) {
        if (CollectionUtils.isEmpty(wrappers)) {
            this.wrappers = new ArrayList<>();
        } else {
            this.wrappers = wrappers;
        }
        this.delegate = HystrixPlugins.getInstance().getConcurrencyStrategy();

        //以下代码是为了兼容性
        if (this.delegate instanceof ContextHystrixConcurrencyStrategy) {
            return;
        }
        HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins
                .getInstance().getCommandExecutionHook();
        HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance()
                .getEventNotifier();
        HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance()
                .getMetricsPublisher();
        HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance()
                .getPropertiesStrategy();
        //重置一下，否则注册不了
        HystrixPlugins.reset();
        HystrixPlugins.getInstance().registerConcurrencyStrategy(this);
        HystrixPlugins.getInstance()
                .registerCommandExecutionHook(commandExecutionHook);
        HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
        HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
        HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        return new CallableWrapperChain<>(callable, this.delegate, wrappers.iterator()).wrapCallable();
    }

    private static class CallableWrapperChain<T> {

        private final Callable<T> callable;

        private HystrixConcurrencyStrategy strategy;

        private final Iterator<HystrixCallableWrapper> wrappers;

        CallableWrapperChain(Callable<T> callable, HystrixConcurrencyStrategy delegate, Iterator<HystrixCallableWrapper> wrappers) {
            this.callable = callable;
            this.wrappers = wrappers;
            this.strategy = delegate;
        }

        Callable<T> wrapCallable() {
            Callable<T> delegate = this.strategy.wrapCallable(callable);
            while (wrappers.hasNext()) {
                delegate = wrappers.next().wrap(delegate);
            }
            return delegate;
        }
    }

}

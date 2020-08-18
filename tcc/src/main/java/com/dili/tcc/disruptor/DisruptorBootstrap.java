package com.dili.tcc.disruptor;

import com.dili.tcc.config.TccProperties;
import com.dili.tcc.disruptor.event.DataEvent;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/8/1 15:13
 * @Description:
 */
public class DisruptorBootstrap<T> {

    private final TccProperties tccProperties;

    private Disruptor<DataEvent<T>> disruptor;

    private DisruptorProvider<T> provider;

    public DisruptorBootstrap(TccProperties tccProperties) {
        this.tccProperties = tccProperties;
    }

    @SuppressWarnings("unchecked")
	@PostConstruct
    public void startup() {
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        EventFactory<DataEvent<T>> eventFactory = new DisruptorEventFactory<>();
        disruptor = new Disruptor<>(eventFactory,
                tccProperties.getBufferSize(),
                threadFactory, ProducerType.MULTI,
                new BlockingWaitStrategy());
        //固定一个消费者，防止乱序问题
        DisruptorConsumerBus<T>[] consumerBus = new DisruptorConsumerBus[1];
        for (int i = 0; i < consumerBus.length; i++) {
            consumerBus[i] = new DisruptorConsumerBus<>();
        }
        disruptor.handleEventsWithWorkerPool(consumerBus);
        disruptor.setDefaultExceptionHandler(new IgnoreExceptionHandler());
        disruptor.start();
        provider = new DisruptorProvider<>(disruptor.getRingBuffer());
    }


    @PreDestroy
    public void destroy() {
        disruptor.shutdown();
    }

    public DisruptorProvider<T> getProvider() {
        return provider;
    }
}

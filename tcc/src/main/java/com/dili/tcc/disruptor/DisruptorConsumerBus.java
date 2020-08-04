package com.dili.tcc.disruptor;

import com.dili.tcc.disruptor.consumer.AbstractConsumer;
import com.dili.tcc.disruptor.consumer.TccTransactionConsumer;
import com.dili.tcc.disruptor.event.DataEvent;
import com.dili.tcc.util.SpringContext;
import com.lmax.disruptor.WorkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 消费总线入口
 * @author miaoguoxin
 * @date 2020/8/1
 */
public class DisruptorConsumerBus<T> implements WorkHandler<DataEvent<T>> {
    private static Logger LOGGER = LoggerFactory.getLogger(DisruptorConsumerBus.class);

    private static final Map<DisruptorEventType, Class<? extends AbstractConsumer<?>>> CONSUMER_MAP = new HashMap<>();

    static {
        CONSUMER_MAP.put(DisruptorEventType.TCC_DURABLE, TccTransactionConsumer.class);
    }

    @Override
    public void onEvent(DataEvent<T> event) throws Exception {
        DisruptorEventType eventType = event.getEventType();
        AbstractConsumer consumer = this.doGetConsumer(eventType);
        if (consumer == null) {
            LOGGER.error("找不到disruptor consumer:{}", eventType);
            return;
        }
        consumer.handle(event.getT());
    }

    private AbstractConsumer<?> doGetConsumer(DisruptorEventType eventType) {
        Class<? extends AbstractConsumer<?>> clazz = CONSUMER_MAP.get(eventType);
        if (clazz == null) {
            return null;
        }
        return SpringContext.getBean(clazz);
    }
}

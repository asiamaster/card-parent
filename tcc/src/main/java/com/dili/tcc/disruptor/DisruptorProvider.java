
package com.dili.tcc.disruptor;

import com.dili.tcc.disruptor.event.DataEvent;
import com.lmax.disruptor.RingBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisruptorProvider<T> {

    private Logger logger = LoggerFactory.getLogger(DisruptorProvider.class);

    private final RingBuffer<DataEvent<T>> ringBuffer;

    public DisruptorProvider(final RingBuffer<DataEvent<T>> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * push data to disruptor queue.
     *
     * @param t the t
     */
    public void publish(final T t, DisruptorEventType eventType) {
        long position = ringBuffer.next();
        try {
            DataEvent<T> de = ringBuffer.get(position);
            de.setT(t);
            de.setEventType(eventType);
        } catch (Exception ex) {
            logger.error("push data error:", ex);
        } finally {
            ringBuffer.publish(position);
        }

    }
}

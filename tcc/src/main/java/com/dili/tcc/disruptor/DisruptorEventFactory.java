
package com.dili.tcc.disruptor;

import com.dili.tcc.disruptor.event.DataEvent;
import com.lmax.disruptor.EventFactory;

public class DisruptorEventFactory<T> implements EventFactory<DataEvent<T>> {
    @Override
    public DataEvent<T> newInstance() {
        return new DataEvent<>();
    }
}

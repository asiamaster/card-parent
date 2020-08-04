package com.dili.tcc.disruptor.consumer;

public abstract class AbstractConsumer<T> {

    protected abstract void handleEvent(T body);

    public final void handle(T entity) {
        this.handleEvent(entity);
    }
}

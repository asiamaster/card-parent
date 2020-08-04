package com.dili.tcc.disruptor.event;

import com.dili.tcc.bean.TccTransaction;
import com.dili.tcc.disruptor.TccEventType;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/8/1 16:00
 * @Description:
 */
public class TccTransactionEvent {

    private TccEventType type;

    private TccTransaction tccTransaction;

    public TccTransactionEvent(TccEventType type, TccTransaction tccTransaction) {
        this.type = type;
        this.tccTransaction = tccTransaction;
    }

    public TccTransaction getTccTransaction() {
        return tccTransaction;
    }

    public TccEventType getType() {
        return type;
    }
}

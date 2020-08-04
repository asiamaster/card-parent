package com.dili.tcc.disruptor.consumer;

import com.dili.tcc.disruptor.event.TccTransactionEvent;
import com.dili.tcc.repository.TccTransactionRepository;
import com.dili.tcc.util.SpringContext;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/8/1 16:12
 * @Description:
 */
public class TccTransactionConsumer extends AbstractConsumer<TccTransactionEvent> {

    @Override
    protected void handleEvent(TccTransactionEvent event) {
        TccTransactionRepository repository = SpringContext.getBean(TccTransactionRepository.class);
        switch (event.getType()){
            case SAVE:
                repository.create(event.getTccTransaction());
                break;
            case UPDATE:
                repository.update(event.getTccTransaction());
                break;
            case REMOVE:
                repository.remove(event.getTccTransaction().getTransId());
                break;
            default:
                break;
        }
    }
}

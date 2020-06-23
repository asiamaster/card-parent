package com.dili.card.common.provider;


import com.dili.card.type.CardStatus;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 城市提供者
 * @author asiamaster
 */
@Component
public class CardStateProvider implements ValueProvider {

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        List<ValuePair<?>> buffer = new ArrayList<>();
        CardStatus.getAll()
                .forEach(c -> buffer.add(new ValuePairImpl(c.getName(), c.getCode())));
        return buffer;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }

}

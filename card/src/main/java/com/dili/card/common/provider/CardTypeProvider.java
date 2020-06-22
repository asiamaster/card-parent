package com.dili.card.common.provider;


import com.dili.card.type.CardType;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 城市提供者
 * @author asiamaster
 */
@Component
public class CardTypeProvider implements ValueProvider {

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        List<ValuePair<?>> buffer = new ArrayList<>();
        CardType.getAll().stream()
                .filter(cardCategory -> cardCategory == CardType.MASTER
                        || cardCategory == CardType.SLAVE)
                .collect(Collectors.toList())
                .forEach(c -> buffer.add(new ValuePairImpl(c.getName(), c.getCode())));
        return buffer;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }
}

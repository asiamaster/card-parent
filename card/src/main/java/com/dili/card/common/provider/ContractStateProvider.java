package com.dili.card.common.provider;


import com.dili.card.type.CardStatus;
import com.dili.card.type.ContractState;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 合同状态提供者
 */
@Component
public class ContractStateProvider implements ValueProvider {
    private static final List<ValuePair<?>> BUFFER = new ArrayList<>();

    static {
        ContractState.getAll()
                .forEach(c -> BUFFER.add(new ValuePairImpl(c.getName(), c.getCode())));
    }

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        return BUFFER;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if (obj == null) {
            return null;
        }
        Integer state = (Integer) obj;
        return CardStatus.getName(state);
    }

}

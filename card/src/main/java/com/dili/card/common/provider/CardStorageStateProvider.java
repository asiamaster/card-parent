package com.dili.card.common.provider;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.dili.card.type.CardStorageState;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;

/**
 * @description： 
 *          卡片库存状态提供者
 * @author ：WangBo
 * @time ：2020年7月16日下午4:29:29
 */
@Component
public class CardStorageStateProvider implements ValueProvider {
    private static final List<ValuePair<?>> BUFFER = new ArrayList<>();

    static {
        CardStorageState.list().stream()
                .collect(Collectors.toList())
                .forEach(c -> BUFFER.add(new ValuePairImpl<Integer>(c.getName(), c.getCode())));
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
        Integer cardStorageState = (Integer) obj;
        return CardStorageState.getName(cardStorageState);
    }
}

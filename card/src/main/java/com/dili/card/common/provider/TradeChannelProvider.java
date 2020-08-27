package com.dili.card.common.provider;


import com.dili.card.type.TradeChannel;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 交易渠道提供者
 */
@Component
public class TradeChannelProvider implements ValueProvider {
    private static final List<ValuePair<?>> BUFFER = new ArrayList<>();

    static {
        BUFFER.addAll(Stream.of(TradeChannel.values()).map(temp -> new ValuePairImpl<>(temp.getName(), temp.getCode())).collect(Collectors.toList()));
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
        Integer code = (Integer) obj;
        return TradeChannel.getNameByCode(code);
    }

}

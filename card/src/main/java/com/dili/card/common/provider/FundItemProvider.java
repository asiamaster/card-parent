package com.dili.card.common.provider;

import com.dili.assets.sdk.rpc.ConfigRpc;
import com.dili.card.type.FundItem;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 资金项目 provider
 */
@Component
public class FundItemProvider implements ValueProvider {
    private static final List<ValuePair<?>> BUFFER = new ArrayList<>();
    @Autowired
    private ConfigRpc configRpc;

    static {
        BUFFER.addAll(Stream.of(FundItem.values()).map(temp -> new ValuePairImpl<>(temp.getName(), temp.getCode())).collect(Collectors.toList()));
    }
    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        return BUFFER;
    }

    @Override
    public String getDisplayText(Object val, Map metaMap, FieldMeta fieldMeta) {
        ValuePair<?> valuePair = BUFFER.stream().filter(temp -> temp.getValue().equals(val)).findFirst().orElse(null);
        if (valuePair != null) {
            return valuePair.getText();
        }
        return null;
    }
}

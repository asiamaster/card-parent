package com.dili.card.common.provider;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.collection.CollectionUtil;
import com.dili.assets.sdk.dto.ConfigDto;
import com.dili.assets.sdk.dto.ConfigQuery;
import com.dili.assets.sdk.rpc.ConfigRpc;
import com.dili.commons.bstable.TableResult;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 资金项目 provider
 */
@Component
public class FundItemProvider implements ValueProvider {

    private static final Logger log = LoggerFactory.getLogger(FundItemProvider.class);

    private static final List<ValuePair<?>> BUFFER = new ArrayList<>();
    @Autowired
    private ConfigRpc configRpc;

    private TimedCache<String, List<ConfigDto>> cache = CacheUtil.newTimedCache(1000 * 60 * 10);

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        final String key = "items";
        List<ConfigDto> configs = cache.get(key);
        if (CollectionUtil.isEmpty(configs)) {
            BUFFER.clear();
            ConfigQuery query = new ConfigQuery();
            query.setType("fundItem");
            TableResult<ConfigDto> fundItemList = configRpc.query(query);
            List<ConfigDto> rows = fundItemList.getRows();
            cache.put(key, rows);
            BUFFER.addAll(rows.stream().map(temp -> new ValuePairImpl<>(
                    temp.getName(), temp.getId())).collect(Collectors.toList()));
        }
        if (val != null){
            String conditionVal = (String) val;
            return BUFFER.stream()
                    .filter(valuePair -> valuePair.getText().contains(conditionVal))
                    .collect(Collectors.toList());
        }
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

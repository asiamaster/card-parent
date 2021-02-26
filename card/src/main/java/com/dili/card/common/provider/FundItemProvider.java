package com.dili.card.common.provider;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.collection.CollectionUtil;

import com.alibaba.fastjson.JSONObject;
import com.dili.assets.sdk.dto.ConfigDto;
import com.dili.assets.sdk.dto.ConfigQuery;
import com.dili.assets.sdk.dto.FunditemDto;
import com.dili.assets.sdk.dto.FunditemQuery;
import com.dili.assets.sdk.rpc.ConfigRpc;
import com.dili.assets.sdk.rpc.FunditemRpc;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.commons.bstable.TableResult;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.sdk.session.SessionContext;

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
    private FunditemRpc funditemRpc;


    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        BUFFER.clear();
        Long firmId = SessionContext.getSessionContext().getUserTicket().getFirmId();
        FunditemQuery query =new FunditemQuery();
        query.setMarketId(firmId);
        List<FunditemDto> fundItemList = GenericRpcResolver.resolver(funditemRpc.queryAll(query), ServiceName.ASSETS);
        log.info("市场{}资金项目列表{}",firmId,JSONObject.toJSONString(fundItemList));
        BUFFER.addAll(fundItemList.stream().map(temp -> new ValuePairImpl<>(
              temp.getName(), temp.getId())).collect(Collectors.toList()));
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

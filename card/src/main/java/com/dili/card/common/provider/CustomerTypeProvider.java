package com.dili.card.common.provider;

import com.dili.card.schedule.CustomerTypeScheduleHandler;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 */
@Component
public class CustomerTypeProvider implements ValueProvider {

    private static final List<ValuePair<?>> BUFFER = new ArrayList<>();


    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        return BUFFER;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if (obj == null) {
            return null;
        }
        return CustomerTypeScheduleHandler.getCustomerType((String) obj);
    }
}

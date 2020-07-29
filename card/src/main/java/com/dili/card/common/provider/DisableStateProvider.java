package com.dili.card.common.provider;

import com.dili.card.type.DisableState;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;

import java.util.List;
import java.util.Map;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/29 09:54
 * @Description:
 */
public class DisableStateProvider implements ValueProvider {

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if (obj == null) {
            return null;
        }
        Integer state = (Integer) obj;
        return DisableState.getName(state);
    }
}

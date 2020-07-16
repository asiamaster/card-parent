package com.dili.card.common.provider;

import com.dili.card.type.AccountStatus;
import com.dili.card.type.CardStatus;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;

import java.util.List;
import java.util.Map;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/28 17:05
 * @Description:
 */
public class AccountStateProvider implements ValueProvider {
    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }

    @Override
    public String getDisplayText(Object val, Map metaMap, FieldMeta fieldMeta) {
        if (val == null) {
            return null;
        }
        Integer state = (Integer) val;
        return AccountStatus.getName(state);
    }
}

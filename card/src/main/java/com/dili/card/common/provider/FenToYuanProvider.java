package com.dili.card.common.provider;

import com.dili.card.util.CurrencyUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;

import java.util.List;
import java.util.Map;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/28 17:12
 * @Description:
 */
public class FenToYuanProvider implements ValueProvider {
    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }

    @Override
    public String getDisplayText(Object val, Map metaMap, FieldMeta fieldMeta) {
        if (val==null){
            return null;
        }
        Long fen = (Long)val;
        return CurrencyUtils.toYuanWithStripTrailingZeros(fen);
    }
}

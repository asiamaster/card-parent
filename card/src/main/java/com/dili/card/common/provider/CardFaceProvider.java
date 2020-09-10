package com.dili.card.common.provider;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.dili.card.type.CardFaceType;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;

/**
 * @description： 
 *          卡面提供者
 * @author ：WangBo
 * @time ：2020年9月9日下午4:55:39
 */
@Component
public class CardFaceProvider implements ValueProvider {
	private static final List<ValuePair<?>> BUFFER = new ArrayList<>();

    static {
        CardFaceType.getAll()
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
        String type = (String) obj;
        return CardFaceType.getTypeName(type);
    }
}

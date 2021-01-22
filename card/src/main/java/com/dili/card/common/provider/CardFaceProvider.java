package com.dili.card.common.provider;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.type.CardFaceType;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.sdk.session.SessionContext;

/**
 * @description： 
 *          卡面提供者
 * @author ：WangBo
 * @time ：2020年9月9日下午4:55:39
 */
@Component
public class CardFaceProvider implements ValueProvider {
	private static final List<ValuePair<?>> BUFFER = new ArrayList<>();

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
    	BUFFER.clear();
    	Long firmId = SessionContext.getSessionContext().getUserTicket().getFirmId();
//    	Object queryParamsObj = metaMap.get(ValueProvider.QUERY_PARAMS_KEY);
//    	//获取查询参数
//		JSONObject queryParams = JSONObject.parseObject(queryParamsObj.toString());
//    	//获取自定义空值显示内容
//		String customEmptyText = queryParams.getString(ValueProvider.EMPTY_ITEM_TEXT_KEY);
//		BUFFER.add(0, new ValuePairImpl<String>(customEmptyText, ""));
    	CardFaceType.getAll(firmId)
        .forEach(c -> BUFFER.add(new ValuePairImpl(c.getName(), c.getCode())));
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

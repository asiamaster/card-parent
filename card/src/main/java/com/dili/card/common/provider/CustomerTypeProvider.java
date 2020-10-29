package com.dili.card.common.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dili.card.common.constant.Constant;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import com.dili.ss.util.SpringUtil;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;

/**
 */
@Component
public class CustomerTypeProvider implements ValueProvider {

	private static final List<ValuePair<?>> BUFFER = new ArrayList<>();

	@Autowired
	private DataDictionaryRpc dataDictionaryRpc;
	@Override
	public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
		return BUFFER;
	}

	@Override
	public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
		DataDictionaryValue ddv = DTOUtils.newInstance(DataDictionaryValue.class);
		ddv.setDdCode(Constant.CUS_CUSTOMER_TYPE);
		ddv.setCode((String)obj);
		if(dataDictionaryRpc == null) {
			// TODO 当前包下无法注入dataDictionaryRpc
			dataDictionaryRpc = SpringUtil.getBean(DataDictionaryRpc.class);  
		}
		List<DataDictionaryValue> resolver = GenericRpcResolver.resolver(dataDictionaryRpc.listDataDictionaryValue(ddv), "DataDictionaryRpc");
        if (resolver == null || resolver.size() == 0) {
            return "";
        }
		return resolver.get(0).getName();
	}
}

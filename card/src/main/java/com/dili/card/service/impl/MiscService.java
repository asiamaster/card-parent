package com.dili.card.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.IMiscService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/2/22 09:57
 * @Description:
 */
@Service
public class MiscService implements IMiscService {
    @Autowired
    private DataDictionaryRpc dataDictionaryRpc;

    @Override
    public String getSingleDictVal(String code, Long firmId) {
        return this.getSingleDictVal(code, firmId, null);
    }

    @Override
    public String getSingleDictVal(String code, Long firmId, String defaultVal) {
        BaseOutput<List<DataDictionaryValue>> listBaseOutput = dataDictionaryRpc.listDataDictionaryValueByDdCode(code);
        List<DataDictionaryValue> resolver = GenericRpcResolver.resolver(listBaseOutput, ServiceName.UAP);
        List<DataDictionaryValue> collect = resolver.stream().filter(d -> {
            boolean a = d.getState() != null && d.getState() == 1;
            boolean b = firmId.equals(d.getFirmId());
            return a && b;
        }).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(collect)){
            return defaultVal;
        }
        return collect.get(0).getCode();
    }
}

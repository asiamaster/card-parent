package com.dili.card.schedule;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.card.common.constant.Constant;
import com.dili.card.entity.bo.DictBo;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.type.CustomerType;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/10/30 13:17
 * @Description:
 */
@Component
public class CustomerTypeScheduleHandler {
    @Autowired
    private DataDictionaryRpc dataDictionaryRpc;

    protected static Logger LOGGER = LoggerFactory.getLogger(CustomerTypeScheduleHandler.class);

    private static final Map<String, DictBo> CUSTOMER_TYPE_MAP = new ConcurrentHashMap<>();

    static {
        CustomerType.getAll()
                .stream()
                .map(customerType -> {
                    DictBo dictBo = new DictBo();
                    dictBo.setCode(customerType.getCode());
                    dictBo.setName(customerType.getName());
                    return dictBo;
                })
                .collect(Collectors.toList())
                .forEach(dictBo -> CUSTOMER_TYPE_MAP.putIfAbsent(dictBo.getCode(), dictBo));

    }

    /**
     *  获取所有客户类型
     * @author miaoguoxin
     * @date 2020/10/30
     */
    //@Scheduled(fixedRate = 30 * 1000)
    public void getAllCustomerType() {
        LOGGER.info("加载字典");
        DataDictionaryValue ddv = DTOUtils.newInstance(DataDictionaryValue.class);
        ddv.setDdCode(Constant.CUS_CUSTOMER_TYPE);
        ddv.setState(1);
        List<DataDictionaryValue> result = GenericRpcResolver.resolver(dataDictionaryRpc.listDataDictionaryValue(ddv), "DataDictionaryRpc");
        if (CollectionUtil.isEmpty(result)) {
            return;
        }
        for (DataDictionaryValue value : result) {
            DictBo dictBo = new DictBo();
            dictBo.setCode(value.getCode());
            dictBo.setName(value.getName());
            CUSTOMER_TYPE_MAP.put(value.getCode(), dictBo);
        }
    }

    /**
     * 获取客户类型名称
     * @author miaoguoxin
     * @date 2020/10/30
     */
    public static String getCustomerType(String type) {
        DictBo dictBo = CUSTOMER_TYPE_MAP.get(type);
        if (dictBo == null) {
            return "";
        }
        return dictBo.getName();
    }
}

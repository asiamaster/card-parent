package com.dili.card.common.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.service.IFirmWithdrawService;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.session.SessionContext;

/**
 * 当前市场及子市场列表
 */
@Component
public class FirmProvider implements ValueProvider {

	private static final Logger log = LoggerFactory.getLogger(FirmProvider.class);

    private static final List<ValuePair<?>> BUFFER = new ArrayList<>();
    @Autowired
    private IFirmWithdrawService firmWithdrawService;

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        //防止少有的并发情况产生重复数据
        synchronized (BUFFER){
            BUFFER.clear();
            Long firmId = SessionContext.getSessionContext().getUserTicket().getFirmId();
            List<Firm> firmList = firmWithdrawService.getFirmList(firmId);
            log.info("市场及子市场返回>{}",JSONObject.toJSONString(firmList));
            BUFFER.addAll(firmList.stream().map(temp -> new ValuePairImpl<>(
                    temp.getName(), temp.getId())).collect(Collectors.toList()));
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

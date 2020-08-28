package com.dili.card.service.print;

import cn.hutool.core.collection.CollUtil;
import com.dili.card.dto.PrintDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 打印相关分发器
 */
@Component
public class PrintDispatcher {

    @Autowired
    private List<IPrintService> printServiceList;

    private Map<Integer, IPrintService> printServiceMap = new ConcurrentHashMap<>();

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        if (CollUtil.isEmpty(printServiceList)) {
            return;
        }

        for (IPrintService service : printServiceList) {
            printServiceMap.put(service.support(), service);
        }
    }

    /**
     * 构建打印数据
     * @param recordDo
     * @param reprint
     * @return
     */
    public Map<String, Object> create(BusinessRecordDo recordDo, boolean reprint) {
        IPrintService printService = printServiceMap.get(recordDo.getType());
        if (printService == null) {
            throw new CardAppBizException("", "不支持该业务");
        }
        return printService.create(recordDo, reprint);
    }
}

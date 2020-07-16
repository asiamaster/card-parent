package com.dili.card.rpc;

import com.dili.card.dto.SerialDto;
import com.dili.card.entity.SerialRecordDo;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 操作流水相关rpc
 */
@FeignClient(name = "account-service", contextId = "serialRecordRpc"/*, url = "http://127.0.0.1:8386"*/)
public interface SerialRecordRpc {

    /**
     * 批量存储操作流水
     * @param serialDto
     * @return
     */
    @RequestMapping(value = "/api/serial/batchSave", method = RequestMethod.POST)
    BaseOutput<?> batchSave(SerialDto serialDto);

    /**
     * 分页查询操作流水记录
     * @param serialDto
     * @return
     */
    @RequestMapping(value = "/api/serial/listPage", method = RequestMethod.POST)
    PageOutput<List<SerialRecordDo>> listPage(SerialDto serialDto);
}

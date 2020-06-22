package com.dili.card.rpc;

import com.dili.card.dto.SerialRequestDto;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 操作流水相关rpc
 */
@FeignClient(name = "account-service", contextId = "serialRecordRpc")
public interface SerialRecordRpc {

    /**
     * 批量存储操作流水
     * @param serialRequestDto
     * @return
     */
    @RequestMapping(value = "/batchSave", method = RequestMethod.POST)
    public BaseOutput<?> batchSave(SerialRequestDto serialRequestDto);
}

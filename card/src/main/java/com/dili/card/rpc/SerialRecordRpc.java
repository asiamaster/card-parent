package com.dili.card.rpc;

import com.dili.card.dto.SerialQueryDto;
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
@FeignClient(name = "account-service", contextId = "serialRecordRpc" , url = "${accountService.url:}")
public interface SerialRecordRpc {

    /**
     * 批量存储操作流水
     * @param serialRecordDoList
     * @return
     */
    @RequestMapping(value = "/api/serial/batchSave", method = RequestMethod.POST)
    BaseOutput<?> batchSave(List<SerialRecordDo> serialRecordDoList);

    /**
     * 分页查询操作流水记录
     * @param serialQueryDto
     * @return
     */
    @RequestMapping(value = "/api/serial/listPage", method = RequestMethod.POST)
    PageOutput<List<SerialRecordDo>> listPage(SerialQueryDto serialQueryDto);

    /**
     * 条件查询操作流水记录
     * @param serialQueryDto
     * @return
     */
    @RequestMapping(value = "/api/serial/list", method = RequestMethod.POST)
    BaseOutput<List<SerialRecordDo>> list(SerialQueryDto serialQueryDto);

    /**
     * 根据列表查询条件统计资金情况
     * @param serialQueryDto
     * @return
     */
    @RequestMapping(value = "/api/serial/countOperateAmount", method = RequestMethod.POST)
    BaseOutput<Long> countOperateAmount(SerialQueryDto serialQueryDto);
}

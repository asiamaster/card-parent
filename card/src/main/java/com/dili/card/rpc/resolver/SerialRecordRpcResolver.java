package com.dili.card.rpc.resolver;

import com.dili.card.dto.SerialDto;
import com.dili.card.rpc.SerialRecordRpc;
import com.dili.ss.domain.BaseOutput;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 操作流水rpc解析类
 * @author xuliang
 */
@Component
public class SerialRecordRpcResolver {

    @Resource
    private SerialRecordRpc serialRecordRpc;

    /**
     * 批量存储
     * @param serialDto
     * @return
     */
    public BaseOutput<?> batchSave(SerialDto serialDto) {
        return serialRecordRpc.batchSave(serialDto);
    }
}

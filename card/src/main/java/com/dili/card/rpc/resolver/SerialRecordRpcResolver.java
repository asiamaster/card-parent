package com.dili.card.rpc.resolver;

import com.dili.card.dto.SerialDto;
import com.dili.card.exception.CardAppBizException;
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
    public void batchSave(SerialDto serialDto) {
        BaseOutput<?> baseOutput = serialRecordRpc.batchSave(serialDto);
        if (!baseOutput.isSuccess()) {
            throw new CardAppBizException("保存操作流水失败");
        }
    }
}

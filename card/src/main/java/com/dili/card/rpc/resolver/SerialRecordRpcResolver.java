package com.dili.card.rpc.resolver;

import com.dili.card.dto.SerialDto;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.SerialRecordRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

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

    /**
     * 分页查询操作流水记录
     * @param serialDto
     * @return
     */
    public PageOutput<List<SerialRecordDo>> listPage(SerialDto serialDto) {
        return serialRecordRpc.listPage(serialDto);
    }
}

package com.dili.card.rpc.resolver;

import com.dili.card.common.constant.ServiceName;
import com.dili.card.dto.SerialQueryDto;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.SerialRecordRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 操作流水rpc解析类
 * @author xuliang
 */
@Component
public class SerialRecordRpcResolver {
    private static Logger log = LoggerFactory.getLogger(SerialRecordRpcResolver.class);

    @Resource
    private SerialRecordRpc serialRecordRpc;

    /**
     * 批量存储
     * @param serialRecordDoList
     * @return
     */
    public void batchSave(List<SerialRecordDo> serialRecordDoList) {
        BaseOutput<?> baseOutput = serialRecordRpc.batchSave(serialRecordDoList);
        if (!baseOutput.isSuccess()) {
            throw new CardAppBizException("保存操作流水失败");
        }
    }

    /**
     * 分页查询操作流水记录
     * @param serialQueryDto
     * @return
     */
    public PageOutput<List<SerialRecordDo>> listPage(SerialQueryDto serialQueryDto) {
        return GenericRpcResolver.resolver(serialRecordRpc.listPage(serialQueryDto), ServiceName.ACCOUNT);
    }

    /**
     * 查询操作流水记录
     * @param serialQueryDto
     * @return
     */
    public List<SerialRecordDo> getList(SerialQueryDto serialQueryDto) {
        BaseOutput<List<SerialRecordDo>> list = serialRecordRpc.list(serialQueryDto);
        return GenericRpcResolver.resolver(list, ServiceName.ACCOUNT);
    }

    /**
     * 根据列表查询条件统计资金情况
     * @param serialQueryDto
     * @return
     */
    public Long countOperateAmount(SerialQueryDto serialQueryDto) {
        BaseOutput<Long> baseOutput = serialRecordRpc.countOperateAmount(serialQueryDto);
        if (!baseOutput.isSuccess()) {
            throw new CardAppBizException("统计操作金额数据失败");
        }
        return baseOutput.getData();
    }
}

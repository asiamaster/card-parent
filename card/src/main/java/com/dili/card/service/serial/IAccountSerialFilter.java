package com.dili.card.service.serial;

import com.dili.card.entity.SerialRecordDo;

/**
 * 流水记录
 */
public interface IAccountSerialFilter {

    /**
     * 用于各业务重新处理流水记录
     * @param serialRecord 流水记录
     * @param feeType 费用类型
     */
    void handle(SerialRecordDo serialRecord, Integer feeType);
}

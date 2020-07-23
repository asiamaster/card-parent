package com.dili.card.service.serial;

import com.dili.card.entity.BusinessRecordDo;

/**
 * 业务记录
 */
public interface IBusinessRecordFilter {

    /**
     * 用于各业务重新处理业务办理记录
     * @param businessRecordDo
     */
    void handle(BusinessRecordDo businessRecordDo);
}

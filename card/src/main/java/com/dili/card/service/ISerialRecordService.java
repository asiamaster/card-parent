package com.dili.card.service;

import com.dili.card.dto.CardRequestDto;
import com.dili.card.entity.BusinessRecordDo;

/**
 * 操作流水记录service接口
 * @author xuliang
 */
public interface ISerialRecordService {

    /**
     * 组装公共数据 包括流水号、客户信息、账务周期、操作员信息、默认状态等
     * @param cardParam
     * @param businessRecord
     */
    void buildCommonInfo(CardRequestDto cardParam, BusinessRecordDo businessRecord);

    /**
     * 保存业务办理记录
     * @param businessRecord
     */
    void saveBusinessRecord(BusinessRecordDo businessRecord);
}

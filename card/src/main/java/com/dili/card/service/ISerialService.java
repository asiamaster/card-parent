package com.dili.card.service;

import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.SerialRecordDo;

import java.util.List;

/**
 * 操作流水记录service接口
 * @author xuliang
 */
public interface ISerialService {

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

    /**
     * 拷贝公共字段
     * @param serialRecord
     * @param businessRecord
     */
    void copyCommonFields(SerialRecordDo serialRecord, BusinessRecordDo businessRecord);

    /**
     * 失败处理 改状态 需新开事务
     * @param serialDto
     */
    void handleFailure(SerialDto serialDto);

    /**
     * 成功处理 改状态、记操作流水 需重开事务
     * @param serialDto
     */
    void handleSuccess(SerialDto serialDto);

    /**
     * 获取操作员当前账期内用于补打的操作记录
     * @param serialDto
     * @return
     */
    List<BusinessRecordDo> cycleReprintList(SerialDto serialDto);
}

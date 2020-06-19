package com.dili.card.service;

import com.dili.card.entity.UserLogDo;

/**
 * 用户日志相关service
 */
public interface IUserLogService {
    /**
     * 设置操作流水号
     * @param userLog
     */
    void setSerialNo(UserLogDo userLog);

    /**
     * 设置客户数据
     * @param customerId
     * @param firmId
     * @param userLog
     */
    void setCustomerInfo(Long customerId, Long firmId, UserLogDo userLog);

    /**
     * 设置账期数据
     * @param opId
     * @param userLog
     */
    void setAccountCycleInfo(Long opId, UserLogDo userLog);

    /**
     * 保存日志
     * @param userLog
     */
    void saveUserLog(UserLogDo userLog);
}

package com.dili.card.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.time.LocalDateTime;

/**
 * @description： 银行存取款查询参数
 *
 * @author ：WangBo
 * @time ：2020年4月26日下午4:30:03
 */
public class BankCounterQuery extends BaseDto {
    private static final long serialVersionUID = 1L;
    /** 结束时间 */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    /** 开始时间 */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    /**业务类型 {@link com.dili.card.type.BankCounterAction}*/
    private Integer action;

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }
}

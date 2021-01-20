package com.dili.card.dto;

import java.time.LocalDateTime;

/**
 * 冲正记录查询dto
 * @Auther: miaoguoxin
 * @Date: 2020/11/24 11:25
 */
public class ReverseRecordQueryDto extends BaseDto{
    /**账务周期号*/
    private Long cycleNo;
    /**对应业务号*/
    private String bizSerialNo;
    /**对应业务类型 {@link com.dili.card.type.OperateType}*/
    private Integer bizType;
    /**开始时间*/
    private LocalDateTime startDate;
    /**结束时间*/
    private LocalDateTime endDate;

    public Long getCycleNo() {
        return cycleNo;
    }

    public void setCycleNo(Long cycleNo) {
        this.cycleNo = cycleNo;
    }

    public String getBizSerialNo() {
        return bizSerialNo;
    }

    public void setBizSerialNo(String bizSerialNo) {
        this.bizSerialNo = bizSerialNo;
    }

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

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
}

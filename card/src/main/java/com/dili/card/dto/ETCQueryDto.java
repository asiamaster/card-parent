package com.dili.card.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.time.LocalDateTime;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/4/27 13:50
 * @Description:
 */
public class ETCQueryDto extends BaseDto {
    /**卡账户id*/
    private Long accountId;
    /**客户Id*/
    private Long customerId;
    /**车牌号*/
    private String plateNo;
    /** 结束时间 */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    /** 开始时间 */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    /**关联卡号*/
    private String cardNo;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}

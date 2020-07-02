package com.dili.card.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.time.LocalDateTime;

/**
 * 卡申领查询Dto
 * @Auther: miaoguoxin
 * @Date: 2020/7/1 16:40
 */
public class ApplyRecordQueryDto extends BaseDto {
    /**卡号*/
    private String cardNo;
    /**领取人id*/
    private Long applyUserId;
    /**结束时间*/
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    /**开始时间*/
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    public Long getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(Long applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
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

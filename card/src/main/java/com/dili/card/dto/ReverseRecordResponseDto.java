package com.dili.card.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.card.common.annotation.TextDisplay;
import com.dili.card.common.provider.FenToYuanProvider;
import com.dili.card.common.provider.TradeTypeProvider;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 冲正记录响应dto
 * @Auther: miaoguoxin
 * @Date: 2020/11/24 14:13
 */
public class ReverseRecordResponseDto extends BaseDto{
    /**冲正业务id*/
    private Long reverseId;
    /**关联的流水号*/
    private String serialNo;
    /**对应的业务流水号*/
    private String bizSerialNo;

    /**对应的业务类型，见枚举TradeType*/
    @TextDisplay(TradeTypeProvider.class)
    private Integer bizTradeType;
    /**冲正金额（区分正负），单位：分*/
    @TextDisplay(FenToYuanProvider.class)
    private Long amount;
    /**园区收益变动金额（区分正负），单位：分*/
    @TextDisplay(FenToYuanProvider.class)
    private Long inAccChangeAmount;
    /**创建时间*/
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    public Long getReverseId() {
        return reverseId;
    }

    public void setReverseId(Long reverseId) {
        this.reverseId = reverseId;
    }

    public Long getInAccChangeAmount() {
        return inAccChangeAmount;
    }

    public void setInAccChangeAmount(Long inAccChangeAmount) {
        this.inAccChangeAmount = inAccChangeAmount;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getBizSerialNo() {
        return bizSerialNo;
    }

    public void setBizSerialNo(String bizSerialNo) {
        this.bizSerialNo = bizSerialNo;
    }

    public Integer getBizTradeType() {
        return bizTradeType;
    }

    public void setBizTradeType(Integer bizTradeType) {
        this.bizTradeType = bizTradeType;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}

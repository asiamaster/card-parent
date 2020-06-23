package com.dili.card.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/22 13:39
 * @Description: 卡账户列表响应Dto
 */
public class AccountListResponseDto implements Serializable {
    /**账户业务主键*/
    private Long accountId;
    /**卡号*/
    private String cardNo;
    /**卡类别 {@link com.dili.card.type.CardType}*/
    private Integer cardType;
    /**客户资料信息*/
    private CustomerResponseDto customer;
    /**卡状态 {@link com.dili.card.type.CardStatus}*/
    private Integer cardState;
    /**开卡时间*/
    @JSONField(format = "yyyy-MM-dd HH:mm:dd")
    private LocalDateTime cardCreateTime;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public CustomerResponseDto getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerResponseDto customer) {
        this.customer = customer;
    }

    public Integer getCardState() {
        return cardState;
    }

    public void setCardState(Integer cardState) {
        this.cardState = cardState;
    }

    public LocalDateTime getCardCreateTime() {
        return cardCreateTime;
    }

    public void setCardCreateTime(LocalDateTime cardCreateTime) {
        this.cardCreateTime = cardCreateTime;
    }
}

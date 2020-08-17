package com.dili.card.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.card.common.annotation.TextDisplay;
import com.dili.card.common.provider.CardStateProvider;
import com.dili.card.common.provider.CardTypeProvider;
import com.dili.card.common.provider.CustomerTypeProvider;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/22 13:39
 * @Description: 卡账户列表响应Dto
 */
public class AccountListResponseDto extends BaseDto implements Serializable {
    /** */
	private static final long serialVersionUID = -1775032495865427107L;
	/**账户业务主键*/
    private Long accountId;
    /**卡号*/
    private String cardNo;
    /**卡类别 {@link com.dili.card.type.CardType}*/
    @TextDisplay(CardTypeProvider.class)
    private Integer cardType;
    /**客户id*/
    private Long customerId;
    /**客户名称*/
    private String customerName;
    /**客户编号*/
    private String customerCode;
    /**客户类型*/
    @TextDisplay(CustomerTypeProvider.class)
    private String customerMarketType;
    /**客户电话*/
    private String customerContactsPhone;
    /**卡状态 {@link com.dili.card.type.CardStatus}*/
    @TextDisplay(CardStateProvider.class)
    private Integer cardState;
    /**开卡时间*/
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }


    public String getCustomerContactsPhone() {
        return customerContactsPhone;
    }

    public void setCustomerContactsPhone(String customerContactsPhone) {
        this.customerContactsPhone = customerContactsPhone;
    }

    public String getCustomerMarketType() {
        return customerMarketType;
    }

    public void setCustomerMarketType(String customerMarketType) {
        this.customerMarketType = customerMarketType;
    }
}

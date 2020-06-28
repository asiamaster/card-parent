package com.dili.card.dto;

import java.io.Serializable;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/28 09:48
 * @Description: 卡账户详情dto
 */
public class AccountDetailResponseDto implements Serializable {
    /**卡账户资金*/
    private AccountFundResponseDto accountFund;
    /**包含卡关联的信息*/
    private AccountWithAssociationResponseDto cardAssociation;
    /**客户信息*/
    private CustomerResponseDto customer;

    public AccountFundResponseDto getAccountFund() {
        return accountFund;
    }

    public void setAccountFund(AccountFundResponseDto accountFund) {
        this.accountFund = accountFund;
    }

    public AccountWithAssociationResponseDto getCardAssociation() {
        return cardAssociation;
    }

    public void setCardAssociation(AccountWithAssociationResponseDto cardAssociation) {
        this.cardAssociation = cardAssociation;
    }

    public CustomerResponseDto getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerResponseDto customer) {
        this.customer = customer;
    }
}

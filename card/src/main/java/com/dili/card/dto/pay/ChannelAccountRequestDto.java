package com.dili.card.dto.pay;

import java.io.Serializable;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/1/13 09:39
 * @Description: 渠道账号
 */
public class ChannelAccountRequestDto implements Serializable {
    /**收款银行卡号*/
    private String toAccount;
    /**收款账户名称*/
    private String toName;
    /**账户类型*/
    private Integer toType;
    /**银行卡类型*/
    private Integer toBankType;
    /**银行联行行号-非必填*/
    private String bankNo;
    /**开户行名称-非必填*/
    private String bankName;

    public Integer getToBankType() {
        return toBankType;
    }

    public void setToBankType(Integer toBankType) {
        this.toBankType = toBankType;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public Integer getToType() {
        return toType;
    }

    public void setToType(Integer toType) {
        this.toType = toType;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}

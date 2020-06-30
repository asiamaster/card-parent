package com.dili.card.dto.pay;

import java.util.List;

/**
 * 提现
 * @author xuliang
 */
public class WithdrawRequestDto {

    /** 交易号*/
    private String tradeId;
    /** 账户ID*/
    private Long accountId;
    /** 交易渠道*/
    private Integer channelId;
    /** 密码*/
    private String password;
    /** 费用项*/
    private List<FeeItemDto> fees;

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<FeeItemDto> getFees() {
        return fees;
    }

    public void setFees(List<FeeItemDto> fees) {
        this.fees = fees;
    }
}
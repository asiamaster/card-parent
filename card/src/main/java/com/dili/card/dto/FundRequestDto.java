package com.dili.card.dto;

/**
 * 用于资金操作相关dto
 * @author xuliang
 */
public class FundRequestDto extends CardRequestDto{

    /** 交易渠道*/
    private Integer tradeChannel;
    /** 金额*/
    private Long amount;
    /** 交易密码*/
    private String tradePwd;
    /** 手续费*/
    private Long serviceCost;

    public Integer getTradeChannel() {
        return tradeChannel;
    }

    public void setTradeChannel(Integer tradeChannel) {
        this.tradeChannel = tradeChannel;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getTradePwd() {
        return tradePwd;
    }

    public void setTradePwd(String tradePwd) {
        this.tradePwd = tradePwd;
    }

    public Long getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(Long serviceCost) {
        this.serviceCost = serviceCost;
    }
}

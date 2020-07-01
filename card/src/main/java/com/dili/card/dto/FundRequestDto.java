package com.dili.card.dto;

import com.dili.card.validator.ConstantValidator;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 用于资金操作相关dto
 * @author xuliang
 */
public class FundRequestDto extends CardRequestDto {

    /** 交易渠道*/
    private Integer tradeChannel;
    /** 金额*/
    @NotNull(message = "金额不能为空", groups = ConstantValidator.Update.class)
    @Min(value = 1, message = "最少1分", groups = ConstantValidator.Update.class)
    private Long amount;
    /** 交易密码*/
    private String tradePwd;
    /** 手续费*/
    private Long serviceCost;
    /**备注*/
    private String mark;

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

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}

package com.dili.card.dto.pay;

import java.io.Serializable;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/5/8 09:33
 * @Description: 免密协议
 */
public class PwdFreeProtocolQueryDto implements Serializable {
    /**协议场景*/
    private Integer type;
    /**资金账户id*/
    private Long accountId;
    /**金额*/
    private Long amount;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}

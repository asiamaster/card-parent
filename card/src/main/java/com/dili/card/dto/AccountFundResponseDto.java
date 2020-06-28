package com.dili.card.dto;

import com.dili.card.common.annotation.TextDisplay;
import com.dili.card.common.provider.FenToYuanProvider;

import java.io.Serializable;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/28 10:25
 * @Description: 账号资金
 */
public class AccountFundResponseDto implements Serializable {
    /**资金id*/
    private Long accountId;
    /**余额*/
    @TextDisplay(FenToYuanProvider.class)
    private Long balance;
    /**冻结资金*/
    @TextDisplay(FenToYuanProvider.class)
    private Long frozenAmount;
    /**可用余额*/
    @TextDisplay(FenToYuanProvider.class)
    private Long availableAmount;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(Long frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public Long getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(Long availableAmount) {
        this.availableAmount = availableAmount;
    }
}

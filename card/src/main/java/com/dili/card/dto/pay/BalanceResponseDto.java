package com.dili.card.dto.pay;

import com.dili.card.common.annotation.TextDisplay;
import com.dili.card.common.provider.FenToYuanProvider;

/**
 * 余额查询返回dto
 * @author xuliang
 */
public class BalanceResponseDto {
    /** 账户ID*/
    private Long accountId;
    /** 余额*/
    @TextDisplay(FenToYuanProvider.class)
    private Long balance;
    /** 冻结余额*/
    @TextDisplay(FenToYuanProvider.class)
    private Long frozenAmount;
    /** 可用余额*/
    @TextDisplay(FenToYuanProvider.class)
    private Long availableAmount;

    /**交易冻结金额*/
    @TextDisplay(FenToYuanProvider.class)
    private Long tradeFrozen;
    /**人工冻结金额*/
    @TextDisplay(FenToYuanProvider.class)
    private Long manFrozen;

    public Long getTradeFrozen() {
        return tradeFrozen;
    }

    public void setTradeFrozen(Long tradeFrozen) {
        this.tradeFrozen = tradeFrozen;
    }

    public Long getManFrozen() {
        return manFrozen;
    }

    public void setManFrozen(Long manFrozen) {
        this.manFrozen = manFrozen;
    }

    /**
     *
     * @return
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     *
     * @param accountId
     */
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    /**
     *
     * @return
     */
    public Long getBalance() {
        return balance;
    }

    /**
     *
     * @param balance
     */
    public void setBalance(Long balance) {
        this.balance = balance;
    }

    /**
     *
     * @return
     */
    public Long getFrozenAmount() {
        return frozenAmount;
    }

    /**
     *
     * @param frozenAmount
     */
    public void setFrozenAmount(Long frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    /**
     *
     * @return
     */
    public Long getAvailableAmount() {
        return availableAmount;
    }

    /**
     *
     * @param availableAmount
     */
    public void setAvailableAmount(Long availableAmount) {
        this.availableAmount = availableAmount;
    }
}

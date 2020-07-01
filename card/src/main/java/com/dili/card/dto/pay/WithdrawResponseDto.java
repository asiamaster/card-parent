package com.dili.card.dto.pay;

/**
 * 提现返回值
 * @author xuliang
 */
public class WithdrawResponseDto {
    /** 账户ID*/
    private Long accountId;
    /** 余额*/
    private Long balance;
    /** 冻结余额*/
    private Long frozenAmount;
    /** 可用余额*/
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

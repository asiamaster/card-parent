package com.dili.card.dto.pay;

/**
 * 余额请求参数dto
 * @author xuliang
 */
public class BalanceRequestDto {

    /** 账户ID*/
    private Long accountId;

    public BalanceRequestDto() {
    }

    public BalanceRequestDto(Long accountId) {
        this.accountId = accountId;
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
}

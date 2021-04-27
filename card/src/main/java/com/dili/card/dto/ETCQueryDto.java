package com.dili.card.dto;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/4/27 13:50
 * @Description:
 */
public class ETCQueryDto extends BaseDto {
    /**卡账户id*/
    private Long accountId;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}

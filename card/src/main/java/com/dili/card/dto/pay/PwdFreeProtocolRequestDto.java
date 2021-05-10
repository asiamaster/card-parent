package com.dili.card.dto.pay;

import java.io.Serializable;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/5/8 12:21
 * @Description:
 */
public class PwdFreeProtocolRequestDto implements Serializable {
    private Integer type;
    private Long accountId;
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

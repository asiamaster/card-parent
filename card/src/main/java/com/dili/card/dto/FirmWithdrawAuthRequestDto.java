package com.dili.card.dto;

import com.dili.card.validator.ConstantValidator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/1/26 16:37
 * @Description:
 */
public class FirmWithdrawAuthRequestDto extends BaseDto {
    /**市场账号id*/
    @NotNull(message = "市场账号id不能为空",groups = ConstantValidator.Check.class)
    private Long accountId;
    /**密码*/
    @NotBlank(message = "密码不能为空",groups = ConstantValidator.Check.class)
    private String password;

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

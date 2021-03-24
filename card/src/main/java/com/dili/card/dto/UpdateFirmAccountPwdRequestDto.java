package com.dili.card.dto;

import com.dili.card.validator.ConstantValidator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/3/24 11:05
 * @Description:
 */
public class UpdateFirmAccountPwdRequestDto extends BaseDto{
    /**资金账户id*/
    @NotNull(message = "资金账户id不能为空",groups = ConstantValidator.Update.class)
    private Long fundAccountId;
    /**新密码*/
    @NotBlank(message = "新密码不能为空",groups = ConstantValidator.Update.class)
    private String newPassword;
    /**旧密码*/
    @NotBlank(message = "旧密码不能为空",groups = ConstantValidator.Update.class)
    private String oldPassword;

    public Long getFundAccountId() {
        return fundAccountId;
    }

    public void setFundAccountId(Long fundAccountId) {
        this.fundAccountId = fundAccountId;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}

package com.dili.card.dto;

import com.dili.card.dto.pay.AccountAllPermission;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 账户权限设置前台请求类
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/26 18:50
 */
public class AccountPermissionRequestDto {

    /**
     * 账户ID
     */
    @NotNull(message = "账户信息不能为空")
    private Long accountId;

    /**
     * 设置的权限信息
     * key-权限编码  value-权限名称
     */
    private List<AccountAllPermission> permission;

    public Long getAccountId() {
        return accountId;
    }
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    public List<AccountAllPermission>  getPermission() {
        return permission;
    }
    public void setPermission(List<AccountAllPermission> permission) {
        this.permission = permission;
    }
}
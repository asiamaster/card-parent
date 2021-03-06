package com.dili.card.dto.pay;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 卡账户权限信息
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/26 17:20
 */
public class AccountPermissionResponseDto {

    /**
     * 已有的权限值
     */
    private Set<Integer> permission;

    /**
     * 全量的权限数据
     */
    private List<AccountAllPermission> allPermission;

    /**
     * 提现权限限制明细
     */
    private AccountPermissionDetail withdraw;

    /**
     * 交易权限限制明细
     */
    private AccountPermissionDetail trade;

    public Set<Integer> getPermission() {
        return permission;
    }
    public void setPermission(Set<Integer> permission) {
        this.permission = permission;
    }
    public List<AccountAllPermission> getAllPermission() {
        return allPermission;
    }
    public void setAllPermission(List<AccountAllPermission> allPermission) {
        this.allPermission = allPermission;
    }
    public AccountPermissionDetail getWithdraw() {
        return withdraw;
    }
    public void setWithdraw(AccountPermissionDetail withdraw) {
        this.withdraw = withdraw;
    }
    public AccountPermissionDetail getTrade() {
        return trade;
    }
    public void setTrade(AccountPermissionDetail trade) {
        this.trade = trade;
    }
}

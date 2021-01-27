package com.dili.card.dto.pay;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/27 16:18
 */
public class AccountAllPermission {

    /**
     * 权限代码
     */
    private Integer code;
    /**
     * 权限名称
     */
    private String name;

    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

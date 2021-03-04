package com.dili.card.type;

/**
 * @description： 账户类型
 *
 * @author ：WangBo
 * @time ：2020年4月22日下午6:50:34
 */
public enum DictValue {
    PWD_BOX_ALLOW_INPUT("是否允许密码框手输", "pwd_box_allow_input", "1"),
    WITHDRAW_CASH_BOX_ALLOW_CHECK("提现允许校验现金柜余额", "withdraw_cash_box_allow_check", "0"),
    ;

    private String name;
    private String code;
    private String defaultVal;

    DictValue(String name, String code, String defaultVal) {
        this.name = name;
        this.code = code;
        this.defaultVal = defaultVal;
    }

    public static DictValue getAccountType(String code) {
        for (DictValue type : DictValue.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }


    public String getCode() {
        return code;
    }


    public String getDefaultVal() {
        return defaultVal;
    }

}

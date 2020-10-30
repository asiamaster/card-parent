package com.dili.card.type;

/**
 * @description：支付主题
 *
 * @author ：WangBo
 * @time ：2020年4月22日下午6:50:34
 */
public enum PaySubject {
    RECHARGE("充值", 1),

    WITHDRAW("提现", 2),
	
	RETURN_CARD("退卡", 3);
	
    private String name;
    private Integer code;

    PaySubject(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

    public static PaySubject getAccountType(Integer code) {
        for (PaySubject type : PaySubject.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        if (code == null || code == 0) {
            return "";
        }
        for (PaySubject type : PaySubject.values()) {
            if (type.getCode() == code) {
                return type.name;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String toString() {
        return name;
    }
}

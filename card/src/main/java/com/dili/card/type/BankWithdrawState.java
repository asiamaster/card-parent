package com.dili.card.type;

/**
 * 支付系统圈提状态枚举
 */
public enum BankWithdrawState {

    HANDING(0, "支付通道处理中"),
    SUCCESS(4,"处理成功"),
    FAILED(5,"处理失败"),
    ;
    private int code;
    private String name;

    BankWithdrawState(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static BankWithdrawState getByCode(int code) {
        for (BankWithdrawState actionType : BankWithdrawState.values()) {
            if (actionType.getCode() == code) {
                return actionType;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (BankWithdrawState actionType : BankWithdrawState.values()) {
            if (actionType.getCode() == code) {
                return actionType.name;
            }
        }
        return null;
    }
}

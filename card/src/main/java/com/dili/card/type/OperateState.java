package com.dili.card.type;

/**
 * 交易类型枚举
 */
public enum OperateState {

    PROCESSING(1, "处理中"),
    SUCCESS(2, "成功"),
    FAILURE(3, "失败");
    private int code;
    private String name;

    private OperateState(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static OperateState getByCode(int code) {
        for (OperateState operateState : OperateState.values()) {
            if (operateState.getCode() == code) {
                return operateState;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (OperateState operateState : OperateState.values()) {
            if (operateState.getCode() == code) {
                return operateState.name;
            }
        }
        return null;
    }
}

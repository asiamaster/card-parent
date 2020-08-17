package com.dili.card.type;

/**
 * 资金动作枚举
 */
public enum CheckCardActionType {

    CHANGE_CARD(1, "换卡"),
    CARD_STORAGE_OUT(2, "卡出库");
    private int code;
    private String name;

    CheckCardActionType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static CheckCardActionType getByCode(int code) {
        for (CheckCardActionType actionType : CheckCardActionType.values()) {
            if (actionType.getCode() == code) {
                return actionType;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (CheckCardActionType actionType : CheckCardActionType.values()) {
            if (actionType.getCode() == code) {
                return actionType.name;
            }
        }
        return null;
    }
}

package com.dili.card.type;

/**
 * 费用类型
 */
public enum FeeType {

    DEPOSIT(1, "押金"),
    CARD_COST(2, "工本费"),
    SERVICE(3, "手续费");
    private int code;
    private String name;

    private FeeType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static FeeType getByCode(int code) {
        for (FeeType feeType : FeeType.values()) {
            if (feeType.getCode() == code) {
                return feeType;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (FeeType feeType : FeeType.values()) {
            if (feeType.getCode() == code) {
                return feeType.name;
            }
        }
        return null;
    }
}

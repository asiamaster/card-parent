package com.dili.card.type;

/**
 * 交易类型枚举
 */
public enum TradeType {

    CHARGE(1, "充值"),
    WITHDRAW(2, "提现"),
    CONSUME(3, "消费"),
    TRANSFER(4, "转账");
    private int code;
    private String name;

    private TradeType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static TradeType getByCode(int code) {
        for (TradeType tradeType : TradeType.values()) {
            if (tradeType.getCode() == code) {
                return tradeType;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (TradeType tradeType : TradeType.values()) {
            if (tradeType.getCode() == code) {
                return tradeType.name;
            }
        }
        return null;
    }
}

package com.dili.card.type;

/**
 * 交易类型枚举
 */
public enum TradeState {

    SUCCESS(1, "成功"),
    FAILURE(2, "失败"),
    PROCESSING(3, "处理中");
    private int code;
    private String name;

    private TradeState(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static TradeState getByCode(int code) {
        for (TradeState tradeState : TradeState.values()) {
            if (tradeState.getCode() == code) {
                return tradeState;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (TradeState tradeState : TradeState.values()) {
            if (tradeState.getCode() == code) {
                return tradeState.name;
            }
        }
        return null;
    }
}

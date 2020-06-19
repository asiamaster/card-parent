package com.dili.card.type;

/**
 * 交易渠道枚举
 */
public enum TradeChannel {

    CASH(1, "现金"),
    POS(2, "POS"),
    BANK_CARD(3, "网银");
    private int code;
    private String name;

    private TradeChannel(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static TradeChannel getByCode(int code) {
        for (TradeChannel tradeChannel : TradeChannel.values()) {
            if (tradeChannel.getCode() == code) {
                return tradeChannel;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (TradeChannel tradeChannel : TradeChannel.values()) {
            if (tradeChannel.getCode() == code) {
                return tradeChannel.name;
            }
        }
        return null;
    }
}

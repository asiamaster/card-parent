package com.dili.card.type;

import java.util.Arrays;

/**
 * 银行卡类型枚举
 */
public enum BankCardType {

    DEBIT_CARD(1, "借记卡"),
    CREDIT_CARD(2, "信用卡");

    private int code;
    private String name;

    BankCardType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static BankCardType getByCode(int code) {
        for (BankCardType bankCardType : BankCardType.values()) {
            if (bankCardType.getCode() == code) {
                return bankCardType;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        return Arrays.stream(BankCardType.values())
                .filter(bankCardType -> bankCardType.getCode() == code)
                .findFirst()
                .map(bankCardType -> bankCardType.name)
                .orElse(null);
    }
}

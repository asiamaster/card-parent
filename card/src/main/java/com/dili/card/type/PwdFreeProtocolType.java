package com.dili.card.type;

import java.util.Arrays;

/**
 * 银行卡类型枚举
 */
public enum PwdFreeProtocolType {

    ETC(40, "etc场景使用"),
   ;
    private int code;
    private String name;

    PwdFreeProtocolType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static PwdFreeProtocolType getByCode(int code) {
        for (PwdFreeProtocolType bankCardType : PwdFreeProtocolType.values()) {
            if (bankCardType.getCode() == code) {
                return bankCardType;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        return Arrays.stream(PwdFreeProtocolType.values())
                .filter(bankCardType -> bankCardType.getCode() == code)
                .findFirst()
                .map(bankCardType -> bankCardType.name)
                .orElse(null);
    }
}

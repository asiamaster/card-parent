package com.dili.card.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 卡状态信息
 * @author apache
 *
 */
public enum CardStatus {
    NORMAL("正常", 1),

    LOCKED("锁定", 2),

    LOSS("挂失", 3),

    RETURNED("退还", 4);

    private String name;
    private int code;

    CardStatus(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public static CardStatus getCardStatus(int code) {
        for (CardStatus status : CardStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }

    public static String getName(int code) {
//    	if(code == null|| code==0) {
//    		return "";
//    	}
        for (CardStatus status : CardStatus.values()) {
            if (status.getCode() == code) {
                return status.name;
            }
        }
        return null;
    }

    public static List<CardStatus> getAll() {
        return new ArrayList<>(Arrays.asList(CardStatus.values()));
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
}

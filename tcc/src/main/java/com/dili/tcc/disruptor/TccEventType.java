package com.dili.tcc.disruptor;


import java.util.Arrays;

public enum TccEventType {
    SAVE(1,"保存"),
    UPDATE(2,"修改"),
    REMOVE(3,"删除")
            ;
    /**
     * 编码
     */
    private int value;

    private String desc;

    TccEventType(int value, String desc) {
        this.value = value;
        this.desc=desc;
    }


    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 获取枚举
     *
     * @param value
     * @return
     */
    public static TccEventType getByValue(int value) {
        return Arrays.stream(values())
                .filter(e -> e.getValue() == value)
                .findFirst().orElse(null);
    }
}

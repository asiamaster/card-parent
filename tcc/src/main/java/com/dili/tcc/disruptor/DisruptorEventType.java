package com.dili.tcc.disruptor;


import java.util.Arrays;

public enum DisruptorEventType {
    TCC_DURABLE(1,"tcc持久化"),
            ;
    /**
     * 编码
     */
    private int value;

    private String desc;

    DisruptorEventType(int value, String desc) {
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
    public static DisruptorEventType getByValue(int value) {
        return Arrays.stream(values())
                .filter(e -> e.getValue() == value)
                .findFirst().orElse(null);
    }
}

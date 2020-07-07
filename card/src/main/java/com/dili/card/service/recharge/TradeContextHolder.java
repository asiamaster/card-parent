package com.dili.card.service.recharge;

import com.alibaba.fastjson.JSONObject;

/**
 * 用于传递交易场景的上下文变量
 * @Auther: miaoguoxin
 * @Date: 2020/7/6 10:27
 */
public class TradeContextHolder {
    public static final String TRADE_ID_KEY = "tradeId";
    public static final String BUSINESS_RECORD_KEY = "businessRecord";
    public static final String USER_ACCOUNT = "userAccount";

    private static final ThreadLocal<JSONObject> LOCAL = new ThreadLocal<>();

    public static void putVal(String key, Object val) {
        JSONObject jsonObject = initIfNecessary();
        jsonObject.put(key, val);
    }

    public static <T> T getVal(String key, Class<T> clazz) {
        JSONObject jsonObject = LOCAL.get();
        return jsonObject.getObject(key, clazz);
    }

    public static void remove() {
        LOCAL.remove();
    }

    private static JSONObject initIfNecessary() {
        JSONObject jsonObject = LOCAL.get();
        //取的都是同一个线程变量，不需要double check
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        return jsonObject;
    }
}

package com.dili.card.common.constant;

/**
 * 用于定义缓存 key常量(包含redis)
 * @Auther: miaoguoxin
 * @Date: 2020/7/2 13:17
 */
public class CacheKey {
    /**客户信息前缀 key */
    public static final String CUSTOMER_INFO_PREFIX = "customer_info:";
    /**防重提交token前缀key*/
    public static final String FORBID_DUPLICATE_TOKEN_PREFIX = "forbid_duplicate_token:";
    /**处理中的银行圈提流水记录*/
    public static final String BANK_WITHDRAW_PROCESSING_SERIAL_PREFIX = "bank_withdraw_processing_serial:";
}

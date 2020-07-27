package com.dili.card.common.constant;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/10 10:09
 * @Description:
 */
public class Constant {
    public static final String SESSION = "context_session";
    /**交易号，临时threadLocal 字段*/
    public static final String TRADE_ID_KEY = "tradeId";
    /**流水记录，临时threadLocal 字段*/
    public static final String BUSINESS_RECORD_KEY = "businessRecord";
    /**账户信息，临时threadLocal 字段*/
    public static final String USER_ACCOUNT = "userAccount";
    /**充值extra 网银类型字段*/
    public static final String BANK_TYPE = "bankType";
    /**充值extra  pos类型字段*/
    public static final String POS_TYPE = "posType";
    /**充值extra  pos凭证号字段*/
    public static final String POS_CERT_NUM = "posCertNum";
}

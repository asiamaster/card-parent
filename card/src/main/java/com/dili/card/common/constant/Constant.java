package com.dili.card.common.constant;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/10 10:09
 * @Description:
 */
public class Constant {
    public static final String SESSION_TICKET = "context_ticket";
    /** 交易号，临时threadLocal 字段 */
    public static final String TRADE_ID_KEY = "tradeId";
    /** 流水记录，临时threadLocal 字段 */
    public static final String BUSINESS_RECORD_KEY = "businessRecord";
    /** 账户信息，临时threadLocal 字段 */
    public static final String USER_ACCOUNT = "userAccount";

    /** 市场收益账户（对于有子商户需求的市场） */
    public static final String CARD_INCOME_ACCOUNT = "card_income_account";

    /** 操作员字段（用于冻结资金额外字段） */
    public static final String OP_NO = "opNo";
    public static final String OP_NAME = "opName";

    /** 合同快到期时间提醒 */
    public static final Long READY_EXPIRE_DAY = 3L;
    public static final String CONTRACT_EXPIRE_DAYS = "contract_expire_days";
    /** 最大最小金额 单位 分 */
    public static final Long MAX_AMOUNT = 999999999L;
    public static final Long MIN_AMOUNT = 1L;
    /** boolean字段 */
    public static final int TRUE_INT_FLAG = 1;
    public static final int FALSE_INT_FLAG = 0;
    /** 卡号字段（用于换卡记录） */
    public static final String NEW_CARD_NO_PARAM = "new_card_no";

    /** 柜台操作记录扩展字段，卡类型 */
    public static final String BUSINESS_RECORD_ATTACH_CARDTYPE = "cardType";
    /** 柜台操作记录扩展字段，资金账号id */
    public static final String BUSINESS_RECORD_ATTACH_FUND_ACCOUNTID = "fundAccountId";
    /**柜台操作记录扩展字段，证件号*/
    public static final String BUSINESS_RECORD_ATTACH_CERT_NO = "certNo";
    /**柜台操作记录扩展字段，流水号*/
    public static final String BUSINESS_RECORD_NO = "recordNo";

    /**uap相关*/
    public static final String UAP_FIRMID = "UAP_firmId";

    /** 数据字典客户身份类型key */
    public static final String CUS_CUSTOMER_TYPE = "cus_customer_type";

    /**用于支付流水号前缀区分*/
    public static final String PAY_SERIAL_NO_PREFIX = "GT_";

    /**市场圈提特殊卡号*/
    public static final String FIRM_WITHDRAW_CARD_NO = "0";
}

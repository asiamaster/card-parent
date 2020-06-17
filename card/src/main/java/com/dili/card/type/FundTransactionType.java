package com.dili.card.type;

/**
 * @description： 
 *          柜台资金操作记录类型
 * @author ：WangBo
 * @time ：2020年6月17日下午5:15:34
 */
public enum FundTransactionType
{
	MASTER("主卡办卡", 10),
	SLAVE("副卡办卡", 11),
	ANONYMOUS("临时卡办卡", 12),
	UNION("联营卡办卡", 13),
	SJBANK("盛京银行卡", 25),
	CASH_CHARGE ("现金充值", 14),
	POS_CHARGE ("POS充值", 15),
    BANK_CHARGE("银行转账充值", 30),
	CASH_WITHDRAW ("现金提款", 16),
	TRANSFER_WITHDRAW ("转账提款", 17),
	CHANGE ("换卡", 18),
	CASH_BACK("现金退卡", 19),
	TRANSFER_BACK("转账退卡", 20),
	CHANGE_PASSWORD("修改密码", 21),
	RESET_PASSWORD("重置密码", 22),
    TRADE_CONSUME("交易",23),
	UNLOCK("解锁", 24),
	LOSS_CARD("挂失", 26),
    LOSS_REMOVE("解挂", 27);

    private String name;
    private int code;

    private FundTransactionType(String name, int code)
    {
        this.name = name;
        this.code = code;
    }
    
    public static FundTransactionType getTransactionType(int code)
    {
        for (FundTransactionType type : FundTransactionType.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

    public static String getName(int code)
    {
        for (FundTransactionType type : FundTransactionType.values()) {
            if (type.getCode() == code) {
                return type.name;
            }
        }
        return null;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }
}

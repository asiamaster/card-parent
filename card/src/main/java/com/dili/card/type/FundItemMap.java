package com.dili.card.type;

/**
 * 用于冲正时资金项目映射
 */
public enum FundItemMap {
    /** 现金存款 */
    CASH_CHARGE(FundItem.CASH_CHARGE, false),
    /** POS存款 */
    POS_CHARGE(FundItem.POS_CHARGE, false),
    /** 网银存款 */
    EBANK_CHARGE(FundItem.EBANK_CHARGE, false),
    /** 现金取款 */
    CASH_WITHDRAW(FundItem.CASH_WITHDRAW, false),
    /** 网银取款 */
    EBANK_WITHDRAW(FundItem.EBANK_WITHDRAW, false),
    /** 网银手续费 */
    EBANK_SERVICE(FundItem.EBANK_SERVICE, true),
    /** POS手续费 */
    POS_SERVICE(FundItem.POS_SERVICE, true),
    ;
    /**是否是费用项*/
    private Boolean isFee;
    /***/
    private FundItem code;

    FundItemMap(FundItem code, Boolean isFee) {
        this.isFee = isFee;
        this.code = code;
    }


    public Boolean getIsFee() {
        return isFee;
    }

    public FundItem getCode() {
        return code;
    }

    /**
     * 判断是否是费用项
     * @author miaoguoxin
     * @date 2020/11/25
     */
    public static boolean isFeeFundItem(Integer fundItemCode) {
        if (fundItemCode == null) {
            return false;
        }
        FundItem item = FundItem.getByCode(fundItemCode);
        if (item == null) {
            return false;
        }
        FundItemMap itemMap = getByCode(item);
        if (itemMap == null) {
            return false;
        }
        return itemMap.getIsFee();
    }


    public static FundItemMap getByCode(FundItem code) {
        for (FundItemMap itemMap : FundItemMap.values()) {
            if (itemMap.getCode() == code) {
                return itemMap;
            }
        }
        return null;
    }
}

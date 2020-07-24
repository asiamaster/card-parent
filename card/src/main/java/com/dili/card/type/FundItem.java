package com.dili.card.type;

/**
 * 资金类型枚举
 */
public enum FundItem {

    CASH_CHARGE(1, "现金存款"),
    POS_CHARGE(2, "POS存款"),
    EBANK_CHARGE(3, "网银存款"),
    CASH_WITHDRAW(4, "现金取款"),
    EBANK_WITHDRAW(5, "网银取款"),
    IC_CARD_COST(6, "IC卡工本费"),
    EBANK_SERVICE(7, "网银手续费"),
    POS_SERVICE(8, "POS手续费"),
    TRADE_FREEZE(9, "交易冻结"),
    TRADE_UNFREEZE(10, "交易解冻"),
    RETURN_CARD_CHANGE(11, "退卡零钱"),
    MANDATORY_FREEZE_FUND(12, "手动冻结资金"),
    MANDATORY_UNFREEZE_FUND(13, "手动解冻资金"),
    MANDATORY_FREEZE_ACCOUNT(14, "手动冻结账户"),
    MANDATORY_UNFREEZE_ACCOUNT(15, "手动解冻账户"),
    LOCAL_DELIVERY_FEE(16, "本地配送费"),
    CAR_ENTRANCE_FEE(17, "车辆进场费"),
    CAR_OUT_FEE(18, "车辆出场费"),
    WEIGH_SERVICE_FEE(19, "称重服务费"),
    TRANSFER_FEE(20, "车辆转场费"),
    LEAVE_FEE(21, "车辆离场费"),
    TRADE_PAYMENT(22, "货款"),
    TRADE_SERVICE_FEE(23, "交易手续费"),
    PASSPORT_MANAGE_FEE(24, "通行证管理费"),
    TEST_FEE(25, "检测费"),
    QUERY_FEE(26, "查询费");
    private int code;
    private String name;

    private FundItem(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static FundItem getByCode(int code) {
        for (FundItem fundItem : FundItem.values()) {
            if (fundItem.getCode() == code) {
                return fundItem;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (FundItem fundItem : FundItem.values()) {
            if (fundItem.getCode() == code) {
                return fundItem.name;
            }
        }
        return null;
    }
}

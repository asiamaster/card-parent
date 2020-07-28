package com.dili.card.type;

/**
 * 系统科目，收费规则系统中收费项的业务类型,该类型可以各业务系统中写死固定
 * <br><i>各系统code可自行定义，尽量不重复
 */
public enum SystemSubjectType {
	DEFAULT(1, "其它"), 
	CARD_OPEN_COST(101, "开卡工本费"), 
	CARD_CHANGE_COST(102, "换卡工本费"), 
	CARD_RECHARGE_POS_FEE(103, "POS充值手续费"),
	CARD_WITHDRAW_EBANK_FEE(104, "提现网银手续费"),;

	private int code;
	private String name;

	private SystemSubjectType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}

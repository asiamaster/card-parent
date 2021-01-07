package com.dili.card.type;

/**
 * @description： 银行存取款类型
 *
 * @author ：WangBo
 * @time ：2020年4月22日下午6:50:34
 */
public enum BankCounterAction {
	/** 买家卡 */
	DEPOSIT("存款", 1),

	/** 卖家卡 */
	WITHDRAW("取款", 2),
	;
	private String name;
	private Integer code;

	BankCounterAction(String name, Integer code) {
		this.name = name;
		this.code = code;
	}

	public static BankCounterAction getAccountType(Integer code) {
		for (BankCounterAction type : BankCounterAction.values()) {
			if (type.getCode() == code) {
				return type;
			}
		}
		return null;
	}

	public static String getNameByCode(int code) {
		for (BankCounterAction type : BankCounterAction.values()) {
			if (type.getCode() == code) {
				return type.name;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String toString() {
		return name;
	}
}

package com.dili.card.type;

/**
 * @description： 账户类型
 *
 * @author ：WangBo
 * @time ：2020年4月22日下午6:50:34
 */
public enum AccountType {
	/** 买家卡 */
	PURCHASE("买家卡", 1),

	/** 卖家卡 */
	SALE("卖家卡", 2),

	/** 缴费卡 */
	PAY_FEES("缴费卡", 3);

	private String name;
	private Integer code;

	private AccountType(String name, Integer code) {
		this.name = name;
		this.code = code;
	}

	public static AccountType getAccountType(Integer code) {
		for (AccountType type : AccountType.values()) {
			if (type.getCode() == code) {
				return type;
			}
		}
		return null;
	}

	public static String getName(Integer code) {
		if (code == null || code == 0) {
			return "";
		}
		for (AccountType type : AccountType.values()) {
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

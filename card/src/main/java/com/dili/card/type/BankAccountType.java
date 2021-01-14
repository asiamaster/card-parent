package com.dili.card.type;

/**
 * @description：银行卡账户类型
 * 
 * @author ：WangBo
 * @time ：2021年1月13日下午12:44:42
 */
public enum BankAccountType {
	PERSONAL("个人账户", 10),

	PUBLIC("对公账户", 20);

	private String name;
	private int code;

	private BankAccountType(String name, int code) {
		this.name = name;
		this.code = code;
	}

	public static BankAccountType getAccountType(int code) {
		for (BankAccountType type : BankAccountType.values()) {
			if (type.getCode() == code) {
				return type;
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

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String toString() {
		return name;
	}
}

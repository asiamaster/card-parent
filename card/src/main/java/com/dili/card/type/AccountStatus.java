package com.dili.card.type;

import java.io.Serializable;

/**
 *
 * AccountStatus 账户状态的枚举类<支付>
 *
 */
public enum AccountStatus implements Serializable {
	NORMAL(1, "正常"),
	LIMITED(2, "受限"),
	FREEZE(3, "冻结"),
	LOGOUT(4, "注销");

	private int type;
	private String name;

	private AccountStatus(int type, String name) {
		this.type = type;
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public static AccountStatus getByType(int type) {
		for (AccountStatus accountStatus : values()) {
			if (accountStatus.getType() == type) {
				return accountStatus;
			}
		}
		// throw new IllegalArgumentException("none-defined accountStatus type,type:" +
		// type);
		return null;
	}

	public static String getName(int index) {
		for (AccountStatus c : AccountStatus.values()) {
			if (c.getType() == index) {
				return c.name;
			}
		}
		return null;
	}

	/**
	 * 账户是否正常状态
	 *
	 * @param no
	 * @return
	 */
	public static boolean isNormal(int no) {
		return NORMAL.getType() == no;
	}
}

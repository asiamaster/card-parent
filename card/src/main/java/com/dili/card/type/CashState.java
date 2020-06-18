package com.dili.card.type;

/**
 * 领款曲矿记录状态
 * @author wb
 */
public enum CashState {
	
	NORMAL(1, "正常"),
	DELETED(-1, "删除");
	private int code;
	private String name;
	
	private CashState(int code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static CashState getByCode(int code) {
		for (CashState CashState : values()) {
			if (CashState.getCode() == code) {
				return CashState;
			}
		}
		return null;
	}

	public static String getNameByCode(int code) {
		for (CashState CashState : CashState.values()) {
			if (CashState.getCode() == code) {
				return CashState.name;
			}
		}
		return null;
	}

}

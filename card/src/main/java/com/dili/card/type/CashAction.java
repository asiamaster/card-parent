package com.dili.card.type;

/**
 * 合同状态
 * @author wb
 */
public enum CashAction {
	
	PAYEE(1, "领款"),
	PAYER(2, "交款");
	private int code;
	private String name;
	
	private CashAction(int code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static CashAction getByCode(int code) {
		for (CashAction CashAction : values()) {
			if (CashAction.getCode() == code) {
				return CashAction;
			}
		}
		return null;
	}

	public static String getNameByCode(int code) {
		for (CashAction CashAction : CashAction.values()) {
			if (CashAction.getCode() == code) {
				return CashAction.name;
			}
		}
		return null;
	}

}

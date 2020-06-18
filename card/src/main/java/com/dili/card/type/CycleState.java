package com.dili.card.type;

/**
 * 账务周期状态
 * @author apache
 *
 */
public enum CycleState {
	ACTIVE(1, "正常"),
	SETTLED(1, "已结账"),
	FLATED(-1, "已平账");
	private int code;
	private String name;
	
	private CycleState(int code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static CycleState getByCode(int code) {
		for (CycleState CycleState : values()) {
			if (CycleState.getCode() == code) {
				return CycleState;
			}
		}
		return null;
	}

	public static String getNameByCode(int code) {
		for (CycleState CycleState : CycleState.values()) {
			if (CycleState.getCode() == code) {
				return CycleState.name;
			}
		}
		return null;
	}

}

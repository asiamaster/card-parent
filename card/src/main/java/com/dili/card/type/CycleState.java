package com.dili.card.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 账务周期状态
 * @author apache
 */
public enum CycleState {
	ACTIVE(1, "活跃"),
	SETTLED(2, "已结账"),
	FLATED(3, "已对账");
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
	
	public static String getName(int code) {
		for (CycleState status : CycleState.values()) {
			if (status.getCode() == code) {
				return status.name;
			}
		}
		return null;
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
	

	public static List<CycleState> getAll() {
		return new ArrayList<>(Arrays.asList(CycleState.values()));
	}

}

package com.dili.card.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 领款曲矿记录状态
 * 
 * @author wb
 */
public enum CashState {

	UNSETTLED(1, "未对账"), SETTLED(2, "已对账"), DELETED(-1, "删除");

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

	public static String getName(int code) {
		for (CashState status : CashState.values()) {
			if (status.getCode() == code) {
				return status.name;
			}
		}
		return null;
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

	public static List<CashState> getAll() {
		return new ArrayList<>(Arrays.asList(CashState.values()));
	}

}

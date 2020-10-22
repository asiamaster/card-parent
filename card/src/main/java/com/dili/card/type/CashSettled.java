package com.dili.card.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 领款曲矿记录状态
 * 
 * @author wb
 */
public enum CashSettled {

	UNUSUAL(0, "异常"), NORMAL(1, "正常");

	private int code;
	private String name;

	private CashSettled(int code, String name) {
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
		for (CashSettled status : CashSettled.values()) {
			if (status.getCode() == code) {
				return status.name;
			}
		}
		return null;
	}

	public static CashSettled getByCode(int code) {
		for (CashSettled CashState : values()) {
			if (CashState.getCode() == code) {
				return CashState;
			}
		}
		return null;
	}

	public static String getNameByCode(int code) {
		for (CashSettled CashState : CashSettled.values()) {
			if (CashState.getCode() == code) {
				return CashState.name;
			}
		}
		return null;
	}

	public static List<CashSettled> getAll() {
		return new ArrayList<>(Arrays.asList(CashSettled.values())).stream().filter(c -> c.getCode() != 10)
				.collect(Collectors.toList());
	}

}

package com.dili.card.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 领取款动作
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
	
	public static List<CashAction> getAll() {
		return new ArrayList<>(Arrays.asList(CashAction.values())).stream().filter(c -> c.getCode() != -1)
				.collect(Collectors.toList());
	}

	public static String getName(int code) {
		for (CashAction status : CashAction.values()) {
			if (status.getCode() == code) {
				return status.name;
			}
		}
		return null;
	}
}

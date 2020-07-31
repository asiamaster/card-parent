package com.dili.card.type;

/**
 * @description： 支付系统资金冻结状态
 * 
 * @author ：WangBo
 * @time ：2020年7月30日下午4:49:58
 */
public enum PayFreezeFundType {

	FREEZE_FUND(1, "冻结状态"), UNFREEZE_FUND(2, "解冻状态");

	private int code;
	private String name;

	PayFreezeFundType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}

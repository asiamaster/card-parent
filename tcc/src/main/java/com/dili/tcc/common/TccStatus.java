package com.dili.tcc.common;

import java.io.Serializable;

/**
 * 标记tcc执行阶段状态
 */
public enum TccStatus implements Serializable {
	PRE(1, "try阶段"),
	CONFIRM(2, "confirm"),
	CANCEL(3, "cancel"),
	;

	private int type;
	private String name;

	TccStatus(int type, String name) {
		this.type = type;
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}
}

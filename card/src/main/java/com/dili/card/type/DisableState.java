/**
 * Copyright (c) 2019 www.diligrp.com All rights reserved. <br />
 * 本软件源代码版权归地利集团,未经许可不得任意复制与传播.<br />
 *
 */
package com.dili.card.type;

/**
 * 禁用启用状态
 */
public enum DisableState {
	/** 启用 */
	ENABLED(1, "正常"),
	/** 禁用 */
	DISABLED(2, "冻结");

	private Integer code;
	private String name;

	DisableState(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public static String getName(int code) {
        for (DisableState status : DisableState.values()) {
            if (status.getCode() == code) {
                return status.name;
            }
        }
        return null;
    }
}

package com.dili.card.type;

/**
 * 支付流水状态
 */
public enum PaySerialType {
	HANDING("处理中", 1),
	SUCCESS("成功", 2),
	FAILED("失败", 3);

	private String name;
	private Integer code;

	PaySerialType(String name, Integer code) {
		this.name = name;
		this.code = code;
	}


	public static String getName(Integer code) {
		if (code == null || code == 0) {
			return "";
		}
		for (PaySerialType type : PaySerialType.values()) {
			if (type.getCode().equals(code)) {
				return type.name;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String toString() {
		return name;
	}
}

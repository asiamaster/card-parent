package com.dili.card.type;

/**
 * @description： 支付通道类型
 *
 * @author ：WangBo
 * @time ：2020年4月22日下午6:50:34
 */
public enum PayPipelineType {
	BANK_WITHDRAW("圈提", 30),
	;

	private String name;
	private Integer code;

	private PayPipelineType(String name, Integer code) {
		this.name = name;
		this.code = code;
	}

	public static PayPipelineType getAccountType(Integer code) {
		for (PayPipelineType type : PayPipelineType.values()) {
			if (type.getCode() == code) {
				return type;
			}
		}
		return null;
	}

	public static String getName(Integer code) {
		if (code == null || code == 0) {
			return "";
		}
		for (PayPipelineType type : PayPipelineType.values()) {
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

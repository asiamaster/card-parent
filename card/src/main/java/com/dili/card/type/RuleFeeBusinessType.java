package com.dili.card.type;

/**
 * @description： 收费规则系统中的业务类型
 * 
 * @author ：WangBo
 * @time ：2020年7月27日下午5:56:35
 */
public enum RuleFeeBusinessType {
	CARD_OPEN_CARD("CARD_OPEN_CARD"),;

	private String code;

	private RuleFeeBusinessType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}

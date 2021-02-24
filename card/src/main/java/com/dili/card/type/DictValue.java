package com.dili.card.type;

/**
 * @description： 账户类型
 *
 * @author ：WangBo
 * @time ：2020年4月22日下午6:50:34
 */
public enum DictValue {
	PWD_BOX_ALLOW_INPUT("是否允许密码框手输", "pwd_box_allow_input"),
	WITHDRAW_CASH_BOX_ALLOW_CHECK("提现允许校验现金柜余额","withdraw_cash_box_allow_check"),
	;

	private String name;
	private String code;

	DictValue(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public static DictValue getAccountType(String code) {
		for (DictValue type : DictValue.values()) {
			if (type.getCode().equals(code) ) {
				return type;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}

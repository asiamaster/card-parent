package com.dili.card.type;

/**
 * @description： 收费规则系统中的业务类型
 * <br>统一权限管理-数据字典-基础数据中心-业务类型
 * 
 * @author ：WangBo
 * @time ：2020年7月27日下午5:56:35
 */
public enum RuleFeeBusinessType {
	/** 开卡费用项 */
	CARD_OPEN_CARD("CARD_OPEN_CARD"),
	/** 换卡费用项 */
	CARD_CHANGE_CARD("CARD_CHANGE_CARD"),
	/** POS充值 */
	CARD_RECHARGE_POS("CARD_RECHARGE_POS"),
	/** 网银提现 */
	CARD_WITHDRAW_EBANK("CARD_WITHDRAW_EBANK"),
	;

	private String code;

	private RuleFeeBusinessType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}

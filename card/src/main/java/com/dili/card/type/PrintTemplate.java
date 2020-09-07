package com.dili.card.type;

import java.io.Serializable;

/**
 *打印模板
 *
 */
public enum PrintTemplate implements Serializable {
	CASH_WITHDRAW("WithdrawalCashDocument", "现金提款"),
	E_BANK_WITHDRAW("WithdrawalBankDocument", "网银提款"),
	CHANGE_CARD("ReissueCardDocument", "换卡"),
	OPEN_CARD("OpenCardDocument", "办卡"),
	OPEN_MASTER_CARD("MasterCardDocument","办理主卡"),
	OPEN_SLAVE_CARD("SlaveCardDocument","办理副卡"),
	RETURN_CARD("refundCardDocument","退卡"),
	CASH_RECHARGE("CashRechargeDocument","现金存款"),
	E_BANK_RECHARGE("BankRechargeDocument","网银存款"),
	POS_RECHARGE("POSRechargeDocument","pos存款"),
	;

	private String type;
	private String desc;

	PrintTemplate(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public String getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

	public static PrintTemplate getByType(String type) {
		for (PrintTemplate accountStatus : values()) {
			if (accountStatus.getType().equals(type)) {
				return accountStatus;
			}
		}
		return null;
	}

}

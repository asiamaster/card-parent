package com.dili.card.type;

import java.util.ArrayList;
import java.util.List;

/**
 * @description：
 *          柜台操作记录类型
 * @author ：WangBo
 * @time ：2020年6月17日下午5:15:19
 */
public enum SerialRecordType
{
	ACCOUNT_TRANSACT("办卡", 10),
	ACCOUNT_CHARGE("充值", 11),
	ACCOUNT_WITHDRAW("提现", 12),
	CHANGE("换卡", 13),
	LOSS_CARD("挂失", 14),
	LOSS_REMOVE("解挂", 15),
	PWD_CHANGE("密码更改", 16),
	RESET_PWD("重置密码", 17),
	REFUND_CARD("退卡", 18),
	LIFT_LOCKED("解锁", 19),
	FLAT_COST("缴费", 20),
	BANKCARD_BIND("银行卡绑定", 21),
	BANKCARD_REMOVE("银行卡解绑", 22),
	BANK_TRANSFER("转账", 23),
	PERMISSION_SET("权限设置", 24),
	PAYEE("领款", 25),
	PAYER("交款", 26),
	FROZEN_FUND("冻结资金",27),
	;

	private String name;
	private int code;

	private SerialRecordType(String name, int code)
	{
		this.name = name;
		this.code = code;
	}

	public static SerialRecordType getOperateType(int code)
	{
		for (SerialRecordType type : SerialRecordType.values()) {
			if (type.getCode() == code) {
				return type;
			}
		}
		return null;
	}

	public static String getName(int code)
	{
		for (SerialRecordType type : SerialRecordType.values()) {
			if (type.getCode() == code) {
				return type.name;
			}
		}
		return null;
	}

	/**
	 * 获取用于当前账期补打操作列表
	 * @return
	 */
	public static List<Integer> createReprintList() {
		List<Integer> typeList = new ArrayList<>();
		typeList.add(ACCOUNT_TRANSACT.getCode());
		typeList.add(ACCOUNT_CHARGE.getCode());
		typeList.add(ACCOUNT_WITHDRAW.getCode());
		typeList.add(CHANGE.getCode());
		typeList.add(REFUND_CARD.getCode());
		return typeList;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}
}

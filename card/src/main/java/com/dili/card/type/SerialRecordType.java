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
	ACCOUNT_TRANSACT("办卡", OperateType.ACCOUNT_TRANSACT.getCode()),
	ACCOUNT_CHARGE("充值", OperateType.ACCOUNT_CHARGE.getCode()),
	ACCOUNT_WITHDRAW("提现", OperateType.ACCOUNT_WITHDRAW.getCode()),
	CHANGE("换卡", OperateType.CHANGE.getCode()),
	LOSS_CARD("挂失", OperateType.LOSS_CARD.getCode()),
	LOSS_REMOVE("解挂", OperateType.LOSS_REMOVE.getCode()),
	PWD_CHANGE("密码更改", OperateType.PWD_CHANGE.getCode()),
	RESET_PWD("重置密码", OperateType.RESET_PWD.getCode()),
	REFUND_CARD("退卡", OperateType.REFUND_CARD.getCode()),
	LIFT_LOCKED("解锁", OperateType.LIFT_LOCKED.getCode()),
	FLAT_COST("缴费", OperateType.FLAT_COST.getCode()),
	BANKCARD_BIND("银行卡绑定", OperateType.BANKCARD_BIND.getCode()),
	BANKCARD_REMOVE("银行卡解绑", OperateType.BANKCARD_REMOVE.getCode()),
	BANK_TRANSFER("转账", OperateType.BANK_TRANSFER.getCode()),
	PERMISSION_SET("权限设置", OperateType.PERMISSION_SET.getCode()),
	PAYEE("领款", OperateType.PAYEE.getCode()),
	PAYER("交款", OperateType.PAYER.getCode()),
	FROZEN_FUND("冻结资金",OperateType.FROZEN_FUND.getCode()),
	;

	private String name;
	private int code;

	SerialRecordType(String name, int code)
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

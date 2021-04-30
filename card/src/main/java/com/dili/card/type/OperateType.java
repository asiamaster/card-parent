package com.dili.card.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description：
 *          柜台操作记录类型
 * @author ：WangBo
 * @time ：2020年6月17日下午5:15:19
 */
public enum OperateType
{
	ACCOUNT_TRANSACT("办卡", 1010),
	ACCOUNT_CHARGE("存款", 1011),
	ACCOUNT_WITHDRAW("取款", 1012),
	CHANGE("换卡", 1013),
	LOSS_CARD("挂失", 1014),
	LOSS_REMOVE("解挂", 1015),
	PWD_CHANGE("修改密码", 1016),
	RESET_PWD("重置密码", 1017),
	REFUND_CARD("退卡", 1018),
	LIFT_LOCKED("卡片解锁", 1019),
	FLAT_COST("缴费", 1020),
	BANKCARD_BIND("银行卡绑定", 1021),
	BANKCARD_REMOVE("银行卡解绑", 1022),
	BANK_TRANSFER("转账", 1023),
	PERMISSION_SET("权限设置", 1024),
	PAYEE("领款", 1025),
	PAYER("交款", 1026),
	FROZEN_FUND("冻结资金",1027),
	UNFROZEN_FUND("解冻资金",1028),
	FROZEN_ACCOUNT("冻结账户",1029),
	UNFROZEN_ACCOUNT("解冻账户",1030),
	FUND_REVERSE("冲正",1031),
	FIRM_WITHDRAW("市场圈提",1032),
	;

	private String name;
	private int code;

	OperateType(String name, int code)
	{
		this.name = name;
		this.code = code;
	}

	/**
	 * 是否是可以冲正的业务(只有充值和提款)
	 * @author miaoguoxin
	 * @date 2020/11/25
	 */
	public static boolean canReverseType(Integer code) {
		if (code == null) {
			return false;
		}
		return OperateType.ACCOUNT_CHARGE.getCode() == code || OperateType.ACCOUNT_WITHDRAW.getCode() == code;
	}

	public static OperateType getOperateType(int code)
	{
		for (OperateType type : OperateType.values()) {
			if (type.getCode() == code) {
				return type;
			}
		}
		return null;
	}

	public static String getName(int code)
	{
		for (OperateType type : OperateType.values()) {
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
		typeList.add(RESET_PWD.getCode());
		typeList.add(PWD_CHANGE.getCode());
		typeList.add(LOSS_CARD.getCode());
		typeList.add(LOSS_REMOVE.getCode());
		typeList.add(LIFT_LOCKED.getCode());
		typeList.add(PERMISSION_SET.getCode());
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

	public static List<OperateType> getAll() {
		return new ArrayList<>(Arrays.asList(OperateType.values()));
	}
}

package com.dili.card.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description： 
 *          柜台操作记录类型
 * @author ：WangBo
 * @time ：2020年6月17日下午5:15:19
 */
public enum OperateType
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
	PERMISSION_SET("权限设置", 24);
	
	private String name;
	private int code;
	
	private OperateType(String name, int code)
	{
		this.name = name;
		this.code = code;
	}
	
	public static OperateType getTransactionType(int code)
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
	
	public static List<OperateType> getStatisticsList()
	{
		OperateType[] itemList = OperateType.values();
		List<OperateType> filteredItemList = new ArrayList<OperateType>();
		for (OperateType type : itemList) {
			if (type != BANKCARD_BIND && type != BANKCARD_REMOVE) {
				filteredItemList.add(type);
			}
		}
		return filteredItemList;
	}
	
	public static List<OperateType> getAllList()
	{
		OperateType[] itemList = OperateType.values();
		List<OperateType> allItemList = new ArrayList<OperateType>();
		for (OperateType type : itemList) {
			allItemList.add(type);
		}
		return allItemList;
	}
	
	public static List<Integer> getReverseList() {
		OperateType[] itemList = OperateType.values();
		List<Integer> filteredItemList = new ArrayList<Integer>();
		for (OperateType type : itemList) {
			if (type == ACCOUNT_CHARGE || type == ACCOUNT_WITHDRAW) {
				filteredItemList.add(type.code);
			}
		}
		return filteredItemList;
	}
	
	public static boolean isReverseTransaction(int code) {
		List<Integer> list = getReverseList();
		for (Integer e : list) {
			if (e == code) {
				return true;
			}
		}
		return false;
	}
	
	public static OperateType getFinanceReverseType(int code) {
		List<OperateType> list = new ArrayList<>();
		Collections.addAll(list, OperateType.ACCOUNT_CHARGE, OperateType.ACCOUNT_WITHDRAW);
		for (OperateType e : list) {
			if (e.getCode() == code) {
				return e;
			}
		}
		
		return null;
	}
}
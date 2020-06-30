package com.dili.card.type;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @description： 账户固定权限 充值、提现、交易、二次交易、转账
 * 
 * @author ：WangBo
 * @time ：2020年6月23日下午3:44:19
 */
public enum UsePermissionType {

	/** 充值 */
	RECHARGE("充值", 11),
	/** 提现 */
	WITHDRAW("提现", 12),
	/** 交易 */
	TRANSACTION("交易", 13),
	/** 市场缴费业务 */
	PAY_FEES("市场缴费业务", 14),
	/** 水、电扣费 */
	UTILITIES("水、电扣费", 15),
	/** 理财业务 */
	WEALTH("理财业务", 16),;

	private String name;
	private int code;

	private UsePermissionType(String name, int code) {
		this.name = name;
		this.code = code;
	}

	public static List<String> getPermissionList(String permissions) {
		String[] split = permissions.split(",");
		return Arrays.asList(split);
	}

	/**
	 * 将类型code以“,”分隔，末尾增加“,”
	 * 
	 * @param types
	 * @return
	 */
	public static String getPermissions(Integer... types) {
		return StringUtils.join(types, ",") + ",";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}

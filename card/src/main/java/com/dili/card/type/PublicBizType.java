package com.dili.card.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description： 公用业务类型
 * 
 * @author ：WangBo
 * @time ：2020年12月23日下午4:46:12
 */
public enum PublicBizType {
	/** 开卡 */
	ACCOUNT_TRANSACT("开卡", 10),
	/** 存款 */
	ACCOUNT_CHARGE("存款", 11),
	/** 取款 */
	ACCOUNT_WITHDRAW("取款", 12),
	/** 换卡 */
	CHANGE("换卡", 13),
	/** 退卡 */
	REFUND_CARD("退卡", 14),
	/** 冻结资金 */
	FROZEN_FUND("冻结资金", 15),
	/** 解冻资金 */
	UNFROZEN_FUND("解冻资金", 16),
	/** 冻结账户 */
	FROZEN_ACCOUNT("冻结账户", 17),
	/** 解冻账户 */
	UNFROZEN_ACCOUNT("解冻账户", 18),
	/** 冲正 */
	FUND_REVERSE("冲正", 19),
	/** 称重服务 */
	WEIGH_SERVICE("称重服务", 20),
	/** 本地配送 */
	LOCAL_DELIVERY("本地配送", 21),
	/** 车辆进场 */
	CAR_ENTRANCE("车辆进场", 22),
	/** 车辆出场 */
	CAR_OUT("车辆出场", 23),
	/** 检测收费 */
	TEST_FEE("检测收费", 24),
	/** 查询收费 */
	QUERY_FEE("查询收费", 25),
	/** 车辆转场 */
	CAR_TRANSFER("车辆转场", 26),
	/** 车辆离场费 */
	CAR_LEAVE("车辆离场", 27),
	/** 通行证办理 */
	PASSPORT_MANAGE("通行证办理", 28),
	/** 交易 */
	TRADE("交易", 29);

	private String name;
	private int code;

	PublicBizType(String name, int code) {
		this.name = name;
		this.code = code;
	}

	public static PublicBizType getOperateType(int code) {
		for (PublicBizType type : PublicBizType.values()) {
			if (type.getCode() == code) {
				return type;
			}
		}
		return null;
	}

	public static String getName(int code) {
		for (PublicBizType type : PublicBizType.values()) {
			if (type.getCode() == code) {
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

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public static List<PublicBizType> getAll() {
		return new ArrayList<>(Arrays.asList(PublicBizType.values()));
	}
}

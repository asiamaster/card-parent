package com.dili.card.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dili.card.common.constant.FirmIdConstant;
import com.dili.card.common.provider.CardFaceProvider;

/**
 * @description： 卡面
 * 
 * @author ：WangBo
 * @time ：2020年9月8日上午10:31:55
 */
public enum CardFaceType {
	EMPTY("-- 无--", ""),
	/** 园外买方 */
	BUYER("买方卡", "buyer"),
	/** 园内买方 */
	BUYER_VIP("VIP买方卡", "buyer_vip"),
	/** 卖方卡 */
	SELLER("卖方卡", "seller"),

	/** 司机 */
	DRIVER("司机卡", "driver"),
	;

	private String name;
	private String code;

	CardFaceType(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public static List<CardFaceType> getAll(Long firmId) {
		ArrayList<CardFaceType> arrayList = new ArrayList<>();
		if (firmId != FirmIdConstant.SG) {
			arrayList.add(EMPTY);
			return arrayList;
		}
		arrayList.addAll(Arrays.asList(CardFaceType.values()));
		return arrayList;
	}

	public static String getTypeName(String code) {
		for (CardFaceType type : CardFaceType.values()) {
			if (type.getCode().equals(code)) {
				return type.getName();
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

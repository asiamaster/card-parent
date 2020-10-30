package com.dili.card.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description： 客户类型 <br>
 * （与客户系统保持一致，在客户系统中可通过字典表进行配置）
 *
 * @author ：WangBo
 * @time ：2020年6月23日下午3:32:47
 */
public enum CustomerType {
	/** 园外买方 */
	OUTSIDE_BUYER("园外买方", "outside_buyer", CardFaceType.BUYER),
	/** 理货区客户 */
	OPERATION_AREA("理货区客户", "operation_area", CardFaceType.BUYER_VIP),
	/** 省内客户 */
	IN_PROVINCE("省内客户", "in_province", CardFaceType.BUYER_VIP),
	/** 理货区客户 */
	NATIVE_CUSTOMER("本地客户", "native", CardFaceType.BUYER_VIP),
	/** 卖方卡 */
	SELLER("卖方", "seller", CardFaceType.SELLER),
	/** 司机 */
	DRIVER("司机", "driver", CardFaceType.DRIVER);

	private String name;
	private String code;
	private CardFaceType cardFaceType;

	private CustomerType(String name, String code, CardFaceType cardFaceType) {
		this.name = name;
		this.code = code;
		this.cardFaceType = cardFaceType;
	}

	public static String getTypeName(String customerTypeCode) {
		for (CustomerType type : CustomerType.values()) {
			if (type.getCode().equals(customerTypeCode)) {
				return type.getName();
			}
		}
		return null;
	}

	/**
	 * 校验客户类型和卡面是否一致，对应关系固定
	 */
	public static boolean checkCardFace(String customerTypeCode, String cardFace) {
		CustomerType type = getType(customerTypeCode);
		if (type == null) {
			return false;
		}
		return cardFace.equals(type.getCardFaceType().getCode());
	}

	public static CustomerType getType(String customerTypeCode) {
		for (CustomerType type : CustomerType.values()) {
			if (type.getCode().equals(customerTypeCode)) {
				return type;
			}
		}
		return null;
	}

	public static List<CustomerType> getAll() {
		return new ArrayList<>(Arrays.asList(CustomerType.values()));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	public CardFaceType getCardFaceType() {
		return cardFaceType;
	}

	public void setCardFaceType(CardFaceType cardFaceType) {
		this.cardFaceType = cardFaceType;
	}

}

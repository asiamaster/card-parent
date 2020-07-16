package com.dili.card.type;


/**
 * @description： 客户类型 <br>
 * （与客户系统保持一致，在客户系统中可通过字典表进行配置）
 * 
 * @author ：WangBo
 * @time ：2020年6月23日下午3:32:47
 */
public enum CustomerType {
	/** 买家卡 */
	PURCHASE("园内买家", "inside_buyer"),

	/** 卖家卡 */
	SALE("园内卖家", "inside_seller"),

	/** 司机 */
	DRIVER("司机", "driver");

	private String name;
	private String code;

	private CustomerType(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public static String getTypeName(String customerCode) {
		for (CustomerType type : CustomerType.values()) {
			if (type.getCode().equals(customerCode)) {
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

}

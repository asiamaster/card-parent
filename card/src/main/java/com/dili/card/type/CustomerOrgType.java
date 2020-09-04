package com.dili.card.type;

/**
 * @description： 客户类型：个人、企业 <br>
 * （客户系统与支付系统的对应）
 * 
 * @author ：WangBo
 * @time ：2020年6月23日下午3:32:47
 */
public enum CustomerOrgType {
	/** 个人 */
	INDIVIDUAL("个人", "individual", 1),

	/** 企业 */
	ENTERPRISE("企业", "enterprise", 2);

	private String name;
	private String customerCode;
	private Integer payCode;

	private CustomerOrgType(String name, String customerCode, Integer payCode) {
		this.name = name;
		this.customerCode = customerCode;
		this.payCode = payCode;
	}

	public static Integer getPayCode(String customerCode) {
		for (CustomerOrgType type : CustomerOrgType.values()) {
			if (type.getCustomerCode().equals(customerCode)) {
				return type.getPayCode();
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

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public Integer getPayCode() {
		return payCode;
	}

	public void setPayCode(Integer payCode) {
		this.payCode = payCode;
	}

}

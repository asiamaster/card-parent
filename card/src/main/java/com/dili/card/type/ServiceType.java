package com.dili.card.type;

/**
 *服务类型
 */
public enum ServiceType {
	/**支付服务 */
	PAY_SERVICE("pay-service", 1),

	/**账户服务 */
	ACCOUNT_SERVCIE("account_servie", 2);

	private String name;
	private Integer code;

	private ServiceType(String name, Integer code) {
		this.name = name;
		this.code = code;
	}

	public static ServiceType getServiceType(Integer code) {
		for (ServiceType type : ServiceType.values()) {
			if (type.getCode() == code) {
				return type;
			}
		}
		return null;
	}

	public static String getName(Integer code) {
		if (code == null || code == 0) {
			return "";
		}
		for (ServiceType type : ServiceType.values()) {
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

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String toString() {
		return name;
	}
}

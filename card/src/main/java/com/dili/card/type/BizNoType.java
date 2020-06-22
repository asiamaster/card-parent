package com.dili.card.type;

/**
 * 业务编号类型
 * @author zhangxxing
 */
public enum BizNoType {
	
	CONTRACT_NO("合同编号", "contractNo"),
	CYCLET_NO("账务周期编号", "cycleNo"),
	OPERATE_SERIAL_NO("操作流水号", "operateSerialNo");

	private String name;
	private String code;

	private BizNoType(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public static BizNoType getBizNoType(String code) {
		for (BizNoType status : BizNoType.values()) {
			if (status.getCode().equalsIgnoreCase(code)) {
				return status;
			}
		}
		return null;
	}

	public static String getName(String code) {
		for (BizNoType status : BizNoType.values()) {
			if (status.getCode().equalsIgnoreCase(code)) {
				return status.name;
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

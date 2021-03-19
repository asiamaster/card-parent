package com.dili.card.type;


/**
 * @description： 客户类型 <br>
 * （与客户系统保持一致，在客户系统中可通过字典表进行配置）
 *
 * @author ：WangBo
 * @time ：2020年6月23日下午3:32:47
 */
public enum CustomerState {
	CANCEL(0, "注销"),
	VALID(1, "有效"),
	DISABLED(2, "禁用"),
	USELESS(3, "未生效"),
	;


	private Integer code;
	private String name;

	private CustomerState(Integer code, String name) {
		this.name = name;
		this.code = code;
	}

	public static String getStateName(Integer code) {
		for (CustomerState state : CustomerState.values()) {
			if (state.getCode().equals(code)) {
				return state.getName();
			}
		}
		return null;
	}

	/**
	 * @return the code
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the code
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(Integer code) {
		this.code = code;
	}

}

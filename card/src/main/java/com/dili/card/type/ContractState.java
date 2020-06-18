package com.dili.card.type;

/**
 * 合同状态
 * @author wb
 */
public enum ContractState {
	
	UNSTARTED(1, "未开始"),
	ENTUST(2, "委托中"),
	ENDED(3, "结束"),
	REMOVED(4, "解除");
	
	private int code;
	private String name;
	
	private ContractState(int code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static ContractState getByCode(int code) {
		for (ContractState ContractState : values()) {
			if (ContractState.getCode() == code) {
				return ContractState;
			}
		}
		return null;
	}

	public static String getNameByCode(int code) {
		for (ContractState ContractState : ContractState.values()) {
			if (ContractState.getCode() == code) {
				return ContractState.name;
			}
		}
		return null;
	}

}

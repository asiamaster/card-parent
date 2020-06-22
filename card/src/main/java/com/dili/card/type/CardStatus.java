package com.dili.card.type;

/**
 * 卡状态信息
 * @author apache
 *
 */
public enum CardStatus {
	NORMAL("正常", 1),

	LOCKED("锁定", 2),

	LOSS("挂失", 3),

	RETURNED("退还", 4);

	private String name;
	private int code;

	private CardStatus(String name, int code) {
		this.name = name;
		this.code = code;
	}

	public static CardStatus getCardStatus(int code) {
		for (CardStatus status : CardStatus.values()) {
			if (status.getCode() == code) {
				return status;
			}
		}
		return null;
	}

	public static String getName(int code) {
//    	if(code == null|| code==0) {
//    		return "";
//    	}
		for (CardStatus status : CardStatus.values()) {
			if (status.getCode() == code) {
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

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}

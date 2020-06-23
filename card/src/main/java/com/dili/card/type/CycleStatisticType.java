package com.dili.card.type;

/**
 * 账务周期统计信息
 * @author apache
 */
public enum CycleStatisticType {
	
	ACCOUNT_CHARGE_CASH(11, 1, "depoCashTimes", "depoCashAmount"),
	ACCOUNT_CHARGE_POS(11, 2, "depoPosTimes", "depoPosAmount"),
	ACCOUNT_CHARGE_BANK(11, 3, "bankInTimes", "bankInAmount"),
	ACCOUNT_WITHDRAW_CASH(12, 1, "drawCashTimes", "drawCashAmount"),
	ACCOUNT_WITHDRAW_BANK(12, 3, "bankOutTimes", "bankOutAmount"),
	RECIEVE_CASH(25, 3, "receiveTimes", "receiveAmount"),
	DELIVER_CASH(26, 3, "deliverTimes", "deliverAmount");
	
	private int type;
	private int tradeChannel;
	private String times;
	private String amount;
	
	

	CycleStatisticType(int type, int tradeChannel, String times, String amount) {
	}
	
	public static CycleStatisticType getCycleStatisticType(int type, int tradeChannel)
	{
		for (CycleStatisticType statisticType : CycleStatisticType.values()) {
			if (statisticType.getType() == type && statisticType.getTradeChannel() == tradeChannel) {
				return statisticType;
			}
		}
		return null;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getTradeChannel() {
		return tradeChannel;
	}

	public void setTradeChannel(int tradeChannel) {
		this.tradeChannel = tradeChannel;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}

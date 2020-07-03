package com.dili.card.type;

/**
 * 账务周期统计信息
 * @author apache
 */
public enum CycleStatisticType {
	
	/** 现金充值 */
	ACCOUNT_CHARGE_CASH(11, 2, "depoCashTimes", "depoCashAmount"),
	/** POS充值 */
	ACCOUNT_CHARGE_POS(11, 3, "depoPosTimes", "depoPosAmount"),
	/** 网银充值 */
	ACCOUNT_CHARGE_BANK(11, 4, "bankInTimes", "bankInAmount"),
	/** 现金提现 */
	ACCOUNT_WITHDRAW_CASH(12, 2, "drawCashTimes", "drawCashAmount"),
	/** 网银提现 */
	ACCOUNT_WITHDRAW_BANK(12, 4, "bankOutTimes", "bankOutAmount"),
	/** 领款 */
	RECIEVE_CASH(1, 2, "receiveTimes", "receiveAmount"),
	/** 交款 */
	DELIVER_CASH(2, 2, "deliverTimes", "deliverAmount"),
	/** 工本费 */
	COST_FEE_CASH(2, 2, "costFeetimes", "costFee");
	
	private int type;
	private int tradeChannel;
	private String times;
	private String amount;
	
	

	CycleStatisticType(int type, int tradeChannel, String times, String amount) {
		this.type = type;
		this.tradeChannel = tradeChannel;
		this.times = times;
		this.amount = amount;
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

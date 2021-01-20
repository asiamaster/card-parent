package com.dili.card.type;

/**
 * 账务周期统计信息
 * @author apache
 */
public enum CycleStatisticType {

	/** 现金充值 */
	ACCOUNT_CHARGE_CASH(OperateType.ACCOUNT_CHARGE.getCode(), TradeChannel.CASH.getCode(), "depoCashTimes", "depoCashAmount"),
	/** POS充值 */
	ACCOUNT_CHARGE_POS(OperateType.ACCOUNT_CHARGE.getCode(), TradeChannel.POS.getCode(), "depoPosTimes", "depoPosAmount"),
	/** 网银充值 */
	ACCOUNT_CHARGE_BANK(OperateType.ACCOUNT_CHARGE.getCode(), TradeChannel.E_BANK.getCode(), "bankInTimes", "bankInAmount"),
	/** 现金提现 */
	ACCOUNT_WITHDRAW_CASH(OperateType.ACCOUNT_WITHDRAW.getCode(), TradeChannel.CASH.getCode(), "drawCashTimes", "drawCashAmount"),
	/** 网银提现 */
	ACCOUNT_WITHDRAW_EBANK(OperateType.ACCOUNT_WITHDRAW.getCode(), TradeChannel.E_BANK.getCode(), "bankOutTimes", "bankOutAmount"),
	/** 银行提现 （圈提）*/
	ACCOUNT_WITHDRAW_BANK(OperateType.ACCOUNT_WITHDRAW.getCode(), TradeChannel.BANK.getCode(), "bankCircleOutTimes", "bankCircleOutAmount"),
	/** 领款 */
	RECIEVE_CASH(1, 2, "receiveTimes", "receiveAmount"),
	/** 交款 */
	DELIVER_CASH(2, 2, "deliverTimes", "deliverAmount"),
	/** 办卡工本费 */
	COST_FEE_CASH_OPEN(OperateType.ACCOUNT_TRANSACT.getCode(), TradeChannel.CASH.getCode(), "openCostFeetimes", "openCostAmount"),
	/** 换卡工本费 */
	COST_FEE_CASH_CHANGE(OperateType.CHANGE.getCode(), TradeChannel.CASH.getCode(), "changeCostFeetimes", "changeCostAmount");

	/**业务类型*/
	private int type;
	/**交易渠道*/
	private int tradeChannel;
	/**对应次数字段名称 {@link com.dili.card.dto.AccountCycleDetailDto}*/
	private String times;
	/**对应金额字段名称 {@link com.dili.card.dto.AccountCycleDetailDto}*/
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

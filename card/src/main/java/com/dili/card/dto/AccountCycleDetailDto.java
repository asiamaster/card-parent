package com.dili.card.dto;

/**
 * 柜员账务周期详情
 * @author zx
 */
public class AccountCycleDetailDto {

	/** 领款次数 */
	private Integer receiveTimes;
	/** 领款金额-分 */
	private Long receiveAmount;
	/** 交款次数 */
	private Integer deliverTimes;
	/** 交款金额-分 */
	private Long deliverAmount;
	/** 现金充值次数 */
	private Integer depoCashTimes;
	/** 现金充值金额-分 */
	private Long depoCashAmount;
	/** POS充值次数 */
	private Integer depoPosTimes;
	/** POS充值金额-分 */
	private Long depoPosAmount;
	/** 网银充值次数 */
	private Integer bankInTimes;
	/** 网银充值金额-分 */
	private Long bankInAmount;
	/** 现金提现次数 */
	private Integer drawCashTimes;
	/** 现金提现金额-分 */
	private Long drawCashAmount;
	/** 网银提现次数 */
	private Integer bankOutTimes;
	/** 网银提现金额-分 */
	private Long bankOutAmount;
	/** 现金收益金额-分 */
	private Long revenueAmount;
	/** 未交现金金额-分 */
	private Long unDeliverAmount;
	/** 结账交现金金额-分 */
	private Long lastDeliverAmount;

	public Integer getReceiveTimes() {
		return receiveTimes;
	}

	public void setReceiveTimes(Integer receiveTimes) {
		this.receiveTimes = receiveTimes;
	}

	public Long getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(Long receiveAmount) {
		this.receiveAmount = receiveAmount;
	}

	public Integer getDeliverTimes() {
		return deliverTimes;
	}

	public void setDeliverTimes(Integer deliverTimes) {
		this.deliverTimes = deliverTimes;
	}

	public Long getDeliverAmount() {
		return deliverAmount;
	}

	public void setDeliverAmount(Long deliverAmount) {
		this.deliverAmount = deliverAmount;
	}

	public Integer getDepoCashTimes() {
		return depoCashTimes;
	}

	public void setDepoCashTimes(Integer depoCashTimes) {
		this.depoCashTimes = depoCashTimes;
	}

	public Long getDepoCashAmount() {
		return depoCashAmount;
	}

	public void setDepoCashAmount(Long depoCashAmount) {
		this.depoCashAmount = depoCashAmount;
	}

	public Integer getDepoPosTimes() {
		return depoPosTimes;
	}

	public void setDepoPosTimes(Integer depoPosTimes) {
		this.depoPosTimes = depoPosTimes;
	}

	public Long getDepoPosAmount() {
		return depoPosAmount;
	}

	public void setDepoPosAmount(Long depoPosAmount) {
		this.depoPosAmount = depoPosAmount;
	}

	public Integer getBankInTimes() {
		return bankInTimes;
	}

	public void setBankInTimes(Integer bankInTimes) {
		this.bankInTimes = bankInTimes;
	}

	public Long getBankInAmount() {
		return bankInAmount;
	}

	public void setBankInAmount(Long bankInAmount) {
		this.bankInAmount = bankInAmount;
	}

	public Integer getDrawCashTimes() {
		return drawCashTimes;
	}

	public void setDrawCashTimes(Integer drawCashTimes) {
		this.drawCashTimes = drawCashTimes;
	}

	public Long getDrawCashAmount() {
		return drawCashAmount;
	}

	public void setDrawCashAmount(Long drawCashAmount) {
		this.drawCashAmount = drawCashAmount;
	}

	public Integer getBankOutTimes() {
		return bankOutTimes;
	}

	public void setBankOutTimes(Integer bankOutTimes) {
		this.bankOutTimes = bankOutTimes;
	}

	public Long getBankOutAmount() {
		return bankOutAmount;
	}

	public void setBankOutAmount(Long bankOutAmount) {
		this.bankOutAmount = bankOutAmount;
	}

	public Long getRevenueAmount() {
		return revenueAmount;
	}

	public void setRevenueAmount(Long revenueAmount) {
		this.revenueAmount = revenueAmount;
	}

	public Long getUnDeliverAmount() {
		return unDeliverAmount;
	}

	public void setUnDeliverAmount(Long unDeliverAmount) {
		this.unDeliverAmount = unDeliverAmount;
	}

	public Long getLastDeliverAmount() {
		return lastDeliverAmount;
	}

	public void setLastDeliverAmount(Long lastDeliverAmount) {
		this.lastDeliverAmount = lastDeliverAmount;
	}

}

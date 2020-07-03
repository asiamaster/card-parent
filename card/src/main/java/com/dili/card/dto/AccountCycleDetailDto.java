package com.dili.card.dto;

import com.dili.card.common.annotation.TextDisplay;
import com.dili.card.common.provider.FenToYuanProvider;

/**
 * 柜员账务周期详情
 * @author zx
 */
public class AccountCycleDetailDto {
	
	/** 账务周期流水号 */
	private Long cycleNo;
	private Integer receiveTimes = 0; 
	/** 领款金额-分 */
	@TextDisplay(FenToYuanProvider.class)
	private Long receiveAmount = 0L;  
	/** 交款次数 */
	private Integer deliverTimes = 0;
	/** 交款金额-分 */
	@TextDisplay(FenToYuanProvider.class)
	private Long deliverAmount = 0L; 
	/** 现金充值次数 */
	private Integer depoCashTimes = 0;
	/** 现金充值金额-分 */
	@TextDisplay(FenToYuanProvider.class)
	private Long depoCashAmount = 0L;
	/** POS充值次数 */
	private Integer depoPosTimes = 0; 
	/** POS充值金额-分 */
	@TextDisplay(FenToYuanProvider.class)
	private Long depoPosAmount = 0L;
	/** 网银充值次数 */
	private Integer bankInTimes = 0;
	/** 网银充值金额-分 */
	@TextDisplay(FenToYuanProvider.class)
	private Long bankInAmount = 0L;
	/** 现金提现次数 */
	private Integer drawCashTimes = 0;
	/** 现金提现金额-分 */
	@TextDisplay(FenToYuanProvider.class)
	private Long drawCashAmount = 0L;
	/** 网银提现次数 */
	private Integer bankOutTimes = 0; 
	/** 网银提现金额-分 */
	@TextDisplay(FenToYuanProvider.class)
	private Long bankOutAmount = 0L; 
	/** 现金收益金额-分 */
	@TextDisplay(FenToYuanProvider.class)
	private Long revenueAmount = 0L;
	/** 未交现金金额-分 */
	@TextDisplay(FenToYuanProvider.class)
	private Long unDeliverAmount;
	/** 结账交现金金额-分 */
	@TextDisplay(FenToYuanProvider.class)
	private Long lastDeliverAmount;
	/** 银行存取款 */
	@TextDisplay(FenToYuanProvider.class)
	private Long inOutBankAmount;
	/** 工本费次数 */
	private Integer costFeetimes;
	/** 工本费-分 */
	private Long costAmount;

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

	public Long getInOutBankAmount() {
		return inOutBankAmount;
	}

	public void setInOutBankAmount(Long inOutBankAmount) {
		this.inOutBankAmount = inOutBankAmount;
	}

	public Integer getCostFeetimes() {
		return costFeetimes;
	}

	public void setCostFeetimes(Integer costFeetimes) {
		this.costFeetimes = costFeetimes;
	}

	public Long getCostAmount() {
		return costAmount;
	}

	public void setCostAmount(Long costAmount) {
		this.costAmount = costAmount;
	}

	public Long getCycleNo() {
		return cycleNo;
	}

	public void setCycleNo(Long cycleNo) {
		this.cycleNo = cycleNo;
	}

}

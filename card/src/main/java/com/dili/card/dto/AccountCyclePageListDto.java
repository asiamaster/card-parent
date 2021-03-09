package com.dili.card.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.card.common.annotation.TextDisplay;
import com.dili.card.common.provider.CycleStateProvider;
import com.dili.card.common.provider.FenToYuanProvider;
import com.dili.card.validator.ConstantValidator;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 柜员账务周期
 *
 * @author bob
 */
public class AccountCyclePageListDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 数据id不为空 */
	@NotNull(message = "数据id不为空", groups = { ConstantValidator.Default.class })
	private Long id;
	/** 员工ID */
	@NotNull(message = "员工ID不为空", groups = { ConstantValidator.Update.class, ConstantValidator.Check.class })
	private Long userId;
	/** 员工编号 */
	private String userCode;
	/** 员工姓名 */
	private String userName;
	/** 账务周期流水号 */
	private String cycleNo;
	/** 账务状态-激活 结账 平账 */
	@TextDisplay(CycleStateProvider.class)
	private Integer state;
	/** 领款金额-分 */
	@TextDisplay(FenToYuanProvider.class)
	private Long receiveAmount = 0L;
	/** 交款金额-分 */
	@TextDisplay(FenToYuanProvider.class)
	private Long deliverAmount = 0L;
	/** 现金充值金额-分 */
	@TextDisplay(FenToYuanProvider.class)
	private Long depoCashAmount = 0L;
	/** POS充值金额-分 */
	@TextDisplay(FenToYuanProvider.class)
	private Long depoPosAmount = 0L;
	/** 网银充值金额-分 */
	@TextDisplay(FenToYuanProvider.class)
	private Long bankInAmount = 0L;
	/** 现金提现金额-分 */
	@TextDisplay(FenToYuanProvider.class)
	private Long drawCashAmount = 0L;
	/** 网银提现金额-分 */
	@TextDisplay(FenToYuanProvider.class)
	private Long bankOutAmount = 0L;
	/** 现金收益金额-分 */
	@TextDisplay(FenToYuanProvider.class)
	private Long revenueAmount = 0L;
	/** 未交现金金额-分 */
	@TextDisplay(FenToYuanProvider.class)
	private Long unDeliverAmount = 0L;
	/** 结账交现金金额-分 */
	@TextDisplay(FenToYuanProvider.class)
	private Long lastDeliverAmount = 0L;
	/** 银行存取款 */
	@TextDisplay(FenToYuanProvider.class)
	private Long inOutBankAmount = 0L;
	/**结账时间*/
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime settleTime;

	public LocalDateTime getSettleTime() {
		return settleTime;
	}

	public void setSettleTime(LocalDateTime settleTime) {
		this.settleTime = settleTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCycleNo() {
		return cycleNo;
	}

	public void setCycleNo(String cycleNo) {
		this.cycleNo = cycleNo;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(Long receiveAmount) {
		this.receiveAmount = receiveAmount;
	}

	public Long getDeliverAmount() {
		return deliverAmount;
	}

	public void setDeliverAmount(Long deliverAmount) {
		this.deliverAmount = deliverAmount;
	}

	public Long getDepoCashAmount() {
		return depoCashAmount;
	}

	public void setDepoCashAmount(Long depoCashAmount) {
		this.depoCashAmount = depoCashAmount;
	}

	public Long getDepoPosAmount() {
		return depoPosAmount;
	}

	public void setDepoPosAmount(Long depoPosAmount) {
		this.depoPosAmount = depoPosAmount;
	}

	public Long getBankInAmount() {
		return bankInAmount;
	}

	public void setBankInAmount(Long bankInAmount) {
		this.bankInAmount = bankInAmount;
	}

	public Long getDrawCashAmount() {
		return drawCashAmount;
	}

	public void setDrawCashAmount(Long drawCashAmount) {
		this.drawCashAmount = drawCashAmount;
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

}

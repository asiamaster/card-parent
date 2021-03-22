package com.dili.card.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.card.common.annotation.TextDisplay;
import com.dili.card.common.provider.FenToYuanProvider;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @description：对帐打印数据
 *
 * @author ：WangBo
 * @time ：2021年1月27日下午4:49:26
 */
public class AccountSettlePrintPrintlDto {

	/** 账务周期流水号 */
	private Long cycleNo;
	/** 账务结束时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endTime;

	/** 对帐时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime settleTime;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime printTime;
	/** 结账交现金金额-元 */
	private Long lastDeliverAmount = 0L;
	private String lastDeliverAmountText = "";
	/** 员工姓名 */
	private String userName;
	/** 审核员-员工ID */
	private Long auditorId;
	/** 审核员-员工姓名 */
	private String auditorName;
	/** 打印人 */
	private String printUserName;
	private String firmName;
	/**市场简称*/
	private String firmSimpleName;

	private Integer receiveTimes = 0;
	/** 领款金额-分 */
	private Long receiveAmount = 0L;
	/** 领款金额 */
	private String receiveAmountText;
	/** 交款次数 */
	private Integer deliverTimes = 0;
	/** 交款金额-分 */
	private Long deliverAmount = 0L;
	private String deliverAmountText;
	/** 现金充值次数 */
	private Integer depoCashTimes = 0;
	/** 现金充值金额-分 */
	private Long depoCashAmount = 0L;
	private String depoCashAmountText;
	/** POS充值次数 */
	private Integer depoPosTimes = 0;
	/** POS充值金额-分 */
	private Long depoPosAmount = 0L;
	private String depoPosAmountText;
	/** 网银充值次数 */
	private Integer bankInTimes = 0;
	/** 网银充值金额-分 */
	private Long bankInAmount = 0L;
	private String bankInAmountText;
	/** 现金提现次数 */
	private Integer drawCashTimes = 0;
	/** 现金提现金额-分 */
	private Long drawCashAmount = 0L;
	private String drawCashAmountText;
	/** 网银提现次数 */
	private Integer bankOutTimes = 0;
	/** 网银提现金额-分 */
	private Long bankOutAmount = 0L;
	private String bankOutAmountText;

	public String getFirmSimpleName() {
		return firmSimpleName;
	}

	public void setFirmSimpleName(String firmSimpleName) {
		this.firmSimpleName = firmSimpleName;
	}

	public Long getCycleNo() {
		return cycleNo;
	}

	public void setCycleNo(Long cycleNo) {
		this.cycleNo = cycleNo;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public Long getLastDeliverAmount() {
		return lastDeliverAmount;
	}

	public void setLastDeliverAmount(Long lastDeliverAmount) {
		this.lastDeliverAmount = lastDeliverAmount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirmName() {
		return firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public String getLastDeliverAmountText() {
		return lastDeliverAmountText;
	}

	public void setLastDeliverAmountText(String lastDeliverAmountText) {
		this.lastDeliverAmountText = lastDeliverAmountText;
	}

	public LocalDateTime getPrintTime() {
		return printTime;
	}

	public void setPrintTime(LocalDateTime printTime) {
		this.printTime = printTime;
	}

	public String getPrintUserName() {
		return printUserName;
	}

	public void setPrintUserName(String printUserName) {
		this.printUserName = printUserName;
	}

	public LocalDateTime getSettleTime() {
		return settleTime;
	}

	public void setSettleTime(LocalDateTime settleTime) {
		this.settleTime = settleTime;
	}

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

	public String getReceiveAmountText() {
		return receiveAmountText;
	}

	public void setReceiveAmountText(String receiveAmountText) {
		this.receiveAmountText = receiveAmountText;
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

	public String getDeliverAmountText() {
		return deliverAmountText;
	}

	public void setDeliverAmountText(String deliverAmountText) {
		this.deliverAmountText = deliverAmountText;
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

	public String getDepoCashAmountText() {
		return depoCashAmountText;
	}

	public void setDepoCashAmountText(String depoCashAmountText) {
		this.depoCashAmountText = depoCashAmountText;
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

	public String getDepoPosAmountText() {
		return depoPosAmountText;
	}

	public void setDepoPosAmountText(String depoPosAmountText) {
		this.depoPosAmountText = depoPosAmountText;
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

	public String getBankInAmountText() {
		return bankInAmountText;
	}

	public void setBankInAmountText(String bankInAmountText) {
		this.bankInAmountText = bankInAmountText;
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

	public String getDrawCashAmountText() {
		return drawCashAmountText;
	}

	public void setDrawCashAmountText(String drawCashAmountText) {
		this.drawCashAmountText = drawCashAmountText;
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

	public String getBankOutAmountText() {
		return bankOutAmountText;
	}

	public void setBankOutAmountText(String bankOutAmountText) {
		this.bankOutAmountText = bankOutAmountText;
	}

	public Long getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Long auditorId) {
		this.auditorId = auditorId;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

}

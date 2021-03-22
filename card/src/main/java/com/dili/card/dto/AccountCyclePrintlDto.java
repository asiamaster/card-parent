package com.dili.card.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @description：结帐打印
 *
 * @author ：WangBo
 * @time ：2021年1月27日下午4:49:26
 */
public class AccountCyclePrintlDto {

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
	/** 打印人 */
	private String printUserName;
	private String firmName;
	/**市场简称*/
	private String firmSimpleName;

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

}

package com.dili.card.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 柜员账务周期
 * 
 * @author bob
 */
public class AccountCycleDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/**  */
	private Long id;
	/** 员工ID */
	private Long userId;
	/** 员工姓名 */
	private String userName;
	/** 账务周期流水号 */
	private Long cycleNo;
	/** 工位现金柜 */
	private Long cashBox;
	/** 账务开始时间 */
	private LocalDateTime startTime;
	/** 账务结束时间 */
	private LocalDateTime endTime;
	/** 账务状态-激活 结账 平账 */
	private Integer state;
	/** 交付现金金额-分 */
	private Long cashAmount;
	/** 审核员-员工ID */
	private Long auditorId;
	/** 审核员-员工姓名 */
	private String auditorName;
	/** 商户编码 */
	private Long firmId; 
	/** 账务周期详情 */
	private AccountCycleDetailDto accountCycleDetailDto;

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getCycleNo() {
		return cycleNo;
	}

	public void setCycleNo(Long cycleNo) {
		this.cycleNo = cycleNo;
	}

	public Long getCashBox() {
		return cashBox;
	}

	public void setCashBox(Long cashBox) {
		this.cashBox = cashBox;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(Long cashAmount) {
		this.cashAmount = cashAmount;
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

	public AccountCycleDetailDto getAccountCycleDetailDto() {
		return accountCycleDetailDto;
	}

	public void setAccountCycleDetailDto(AccountCycleDetailDto accountCycleDetailDto) {
		this.accountCycleDetailDto = accountCycleDetailDto;
	}

	public Long getFirmId() {
		return firmId;
	}

	public void setFirmId(Long firmId) {
		this.firmId = firmId;
	}

}

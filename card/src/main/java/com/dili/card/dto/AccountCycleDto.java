package com.dili.card.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.card.common.annotation.TextDisplay;
import com.dili.card.common.provider.CycleStateProvider;
import com.dili.card.validator.ConstantValidator;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 柜员账务周期
 *
 * @author bob
 */
public class AccountCycleDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/**数据id不为空*/
	@NotNull(message = "数据id不为空", groups = {ConstantValidator.Default.class})
	private Long id;
	/** 员工ID */
	@NotNull(message = "员工ID不为空", groups = {ConstantValidator.Update.class, ConstantValidator.Check.class})
	private Long userId;
	/** 员工编号 */
	private String userCode;
	/** 员工姓名 */
	private String userName;
	/** 账务周期流水号 */
	private Long cycleNo;
	/** 工位现金柜 */
	private Long cashBox;
	/**对账时间*/
	private LocalDateTime settleTime;
	/** 账务开始时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startTime;
	/** 账务结束时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endTime;
	/**结账开始时间*/
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime flatedStartDate;
	/**结账结束时间*/
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime flatedEndDate;
	/** 账务状态-激活 结账 平账 */
	@TextDisplay(CycleStateProvider.class)
	private Integer state;
	/** 交付现金金额-分 */
	@NotNull(message = "交付现金金额不为空", groups = {ConstantValidator.Update.class})
	private Long cashAmount;
	/** 审核员-员工ID */
	private Long auditorId;
	/** 审核员-员工姓名 */
	private String auditorName;
	/** 商户编码 */
	private Long firmId;
	/** 账务周期详情 */
	private AccountCycleDetailDto accountCycleDetailDto;
	/** 列表查询是可以选择多个状态值*/
	private List<Integer> states;

	public LocalDateTime getFlatedStartDate() {
		return flatedStartDate;
	}

	public void setFlatedStartDate(LocalDateTime flatedStartDate) {
		this.flatedStartDate = flatedStartDate;
	}

	public LocalDateTime getFlatedEndDate() {
		return flatedEndDate;
	}

	public void setFlatedEndDate(LocalDateTime flatedEndDate) {
		this.flatedEndDate = flatedEndDate;
	}

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

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public List<Integer> getStates() {
		return states;
	}

	public void setStates(List<Integer> states) {
		this.states = states;
	}


	public void addStates(Integer state) {
		if (states == null) {
			states = new ArrayList<Integer>();
		}
		states.add(state);
	}
}

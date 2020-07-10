package com.dili.card.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.card.common.annotation.TextDisplay;
import com.dili.card.common.provider.CashStateProvider;
import com.dili.card.validator.ConstantValidator;

/**
 * 柜员交款领款
 * @author bob
 */
public class UserCashDto extends BaseDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**主键id*/
	@NotNull(message = "数据id不为空", groups = {ConstantValidator.Update.class})
	private Long id; 
	/**领取款编号*/
	private Long cashNo;
	/**领取款编号*/
	private Long cycleNo;
	/** 领款或收款人 */
	@NotNull(message = "员工id不为空", groups = {ConstantValidator.Insert.class})
	private Long userId;
	/** 领款或收款人 */
	@NotBlank(message = "员工编号不为空", groups = {ConstantValidator.Insert.class})
	private String userCode;
	/** 领款人名称-保留字段 */
	@NotBlank(message = "员工姓名不为空", groups = {ConstantValidator.Insert.class})
	private String userName; 
	/** 现金动作-领款 交款 */
	private Integer action; 
	/** 操作金额-分 */
	private Long amount; 
	/** 操作金额-元 */
	@NotNull(message = "金额不为空", groups = {ConstantValidator.Insert.class, ConstantValidator.Update.class})
	private String amountYuan;
	/** 状态-预留字段 */
	@TextDisplay(CashStateProvider.class)
	private Integer state; 
	/** 备注 */
	private String notes; 
	/** 经办人 */
	private Long creatorId; 
	/** 经办人工号 */
	private String creatorCode; 
	/** 经办人姓名 */
	private String creator;
	/** 创建时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime; 
	
	/** 创建开始时间 */
	private LocalDateTime createStartTime;
	
	/** 创建结束时间 */
	private LocalDateTime createEndTime;
	
	/** 市场id */
	private Long firmId;

    /**
     * UserCashEntity constructor
     */
	public UserCashDto() {
		super();
	}
	
	 /**
     * setter for 
     */
	public void setFirmId(Long firmId) {
		this.firmId = firmId;
	}

    /**
     * getter for 
     */
	public Long getFirmId() {
		return firmId;
	}

    /**
     * setter for 
     */
	public void setId(Long id) {
		this.id = id;
	}

    /**
     * getter for 
     */
	public Long getId() {
		return id;
	}

    /**
     * setter for 领款或收款人
     */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

    /**
     * getter for 领款或收款人
     */
	public Long getUserId() {
		return userId;
	}

    /**
     * setter for 操作人员名称-保留字段
     */
	public void setUserName(String userName) {
		this.userName = userName;
	}

    /**
     * getter for 操作人员名称-保留字段
     */
	public String getUserName() {
		return userName;
	}

    /**
     * setter for 现金动作-领款 交款
     */
	public void setAction(Integer action) {
		this.action = action;
	}

    /**
     * getter for 现金动作-领款 交款
     */
	public Integer getAction() {
		return action;
	}

    /**
     * setter for 操作金额-分
     */
	public void setAmount(Long amount) {
		this.amount = amount;
	}

    /**
     * getter for 操作金额-分
     */
	public Long getAmount() {
		return amount;
	}

    /**
     * setter for 状态-预留字段
     */
	public void setState(Integer state) {
		this.state = state;
	}

    /**
     * getter for 状态-预留字段
     */
	public Integer getState() {
		return state;
	}

    /**
     * setter for 备注
     */
	public void setNotes(String notes) {
		this.notes = notes;
	}

    /**
     * getter for 备注
     */
	public String getNotes() {
		return notes;
	}

    /**
     * setter for 创建时间
     */
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

    /**
     * getter for 创建时间
     */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public LocalDateTime getCreateStartTime() {
		return createStartTime;
	}

	public void setCreateStartTime(LocalDateTime createStartTime) {
		this.createStartTime = createStartTime;
	}

	public LocalDateTime getCreateEndTime() {
		return createEndTime;
	}

	public void setCreateEndTime(LocalDateTime createEndTime) {
		this.createEndTime = createEndTime;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getAmountYuan() {
		return amountYuan;
	}

	public void setAmountYuan(String amountYuan) {
		this.amountYuan = amountYuan;
	}

	public Long getCashNo() {
		return cashNo;
	}

	public void setCashNo(Long cashNo) {
		this.cashNo = cashNo;
	}

	public String getCreatorCode() {
		return creatorCode;
	}

	public void setCreatorCode(String creatorCode) {
		this.creatorCode = creatorCode;
	}

	public Long getCycleNo() {
		return cycleNo;
	}

	public void setCycleNo(Long cycleNo) {
		this.cycleNo = cycleNo;
	}

}

package com.dili.card.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 柜员交款领款
 * @author bob
 */
public class UserCashDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**领款人编号*/
	private Long id; 
	/** 领款或收款人 */
	private Long userId; 
	/** 领款人名称-保留字段 */
	private String userName; 
	/** 现金动作-领款 交款 */
	private Integer action; 
	/** 操作金额-分 */
	private Long amount; 
	/** 状态-预留字段 */
	private Integer state; 
	/** 备注 */
	private String notes; 
	/** 经办人 */
	private Long creatorId; 
	/** 经办人姓名 */
	private String creator;
	/** 创建时间 */
	private LocalDateTime createTime; 
	
	/** 创建开始时间 */
	private LocalDateTime createStartTime;
	
	/** 创建结束时间 */
	private LocalDateTime createEndTime;

    /**
     * UserCashEntity constructor
     */
	public UserCashDto() {
		super();
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

}

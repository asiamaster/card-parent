package com.dili.card.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 柜员交款领款
 * @author bob
 */
public class UserCashEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Long id; 
	/** 领款或收款人 */
	private Long userId; 
	/** 操作人员名称-保留字段 */
	private String userName; 
	/** 现金动作-领款 交款 */
	private Integer action; 
	/** 账务周期流水号 */
	private Long cycleNo; 
	/** 操作金额-分 */
	private Long amount; 
	/** 状态-预留字段 */
	private Integer state; 
	/** 备注 */
	private String notes; 
	/** 商户编码 */
	private Long firmId; 
	/** 商户名称 */
	private String firmName; 
	/** 经办人 */
	private Long creatorId; 
	/** 经办人姓名 */
	private String creator; 
	/** 创建时间 */
	private LocalDateTime createTime; 
	/** 修改时间 */
	private LocalDateTime modifyTime; 
    /**
     * UserCashEntity constructor
     */
	public UserCashEntity() {
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
     * setter for 账务周期流水号
     */
	public void setCycleNo(Long cycleNo) {
		this.cycleNo = cycleNo;
	}

    /**
     * getter for 账务周期流水号
     */
	public Long getCycleNo() {
		return cycleNo;
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
     * setter for 商户编码
     */
	public void setFirmId(Long firmId) {
		this.firmId = firmId;
	}

    /**
     * getter for 商户编码
     */
	public Long getFirmId() {
		return firmId;
	}

    /**
     * setter for 商户名称
     */
	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

    /**
     * getter for 商户名称
     */
	public String getFirmName() {
		return firmName;
	}

    /**
     * setter for 经办人
     */
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

    /**
     * getter for 经办人
     */
	public Long getCreatorId() {
		return creatorId;
	}

    /**
     * setter for 经办人姓名
     */
	public void setCreator(String creator) {
		this.creator = creator;
	}

    /**
     * getter for 经办人姓名
     */
	public String getCreator() {
		return creator;
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

    /**
     * setter for 修改时间
     */
	public void setModifyTime(LocalDateTime modifyTime) {
		this.modifyTime = modifyTime;
	}

    /**
     * getter for 修改时间
     */
	public LocalDateTime getModifyTime() {
		return modifyTime;
	}

    /**
     * UserCashEntity.toString()
     */
    @Override
    public String toString() {
        return "UserCashEntity{" +
               "id='" + id + '\'' +
               ", userId='" + userId + '\'' +
               ", userName='" + userName + '\'' +
               ", action='" + action + '\'' +
               ", cycleNo='" + cycleNo + '\'' +
               ", amount='" + amount + '\'' +
               ", state='" + state + '\'' +
               ", notes='" + notes + '\'' +
               ", firmId='" + firmId + '\'' +
               ", firmName='" + firmName + '\'' +
               ", creatorId='" + creatorId + '\'' +
               ", creator='" + creator + '\'' +
               ", createTime='" + createTime + '\'' +
               ", modifyTime='" + modifyTime + '\'' +
               '}';
    }

}

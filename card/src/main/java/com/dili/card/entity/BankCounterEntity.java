package com.dili.card.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 银行存取款
 * @author bob
 */
public class BankCounterEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Long id; 
	/** 动作-存款 取款 */
	private Integer action; 
	/** 操作金额-分 */
	private Long amount; 
	/** 状态-新建 封存 */
	private Integer state; 
	/** 银行操作流水号 */
	private Long serialNo; 
	/** 实际存取款时间 */
	private LocalDateTime applyTime; 
	/** 操作人员ID */
	private Long creatorId; 
	/** 操作人员名称 */
	private String creator; 
	/** 商户编码 */
	private Long firmId; 
	/** 商户名称 */
	private String firmName; 
	/** 备注 */
	private String notes; 
	/** 创建时间 */
	private LocalDateTime createTime; 
	/** 修改时间 */
	private LocalDateTime modifyTime; 
    /**
     * BankCounterEntity constructor
     */
	public BankCounterEntity() {
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
     * setter for 动作-存款 取款
     */
	public void setAction(Integer action) {
		this.action = action;
	}

    /**
     * getter for 动作-存款 取款
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
     * setter for 状态-新建 封存
     */
	public void setState(Integer state) {
		this.state = state;
	}

    /**
     * getter for 状态-新建 封存
     */
	public Integer getState() {
		return state;
	}

    /**
     * setter for 银行操作流水号
     */
	public void setSerialNo(Long serialNo) {
		this.serialNo = serialNo;
	}

    /**
     * getter for 银行操作流水号
     */
	public Long getSerialNo() {
		return serialNo;
	}

    /**
     * setter for 实际存取款时间
     */
	public void setApplyTime(LocalDateTime applyTime) {
		this.applyTime = applyTime;
	}

    /**
     * getter for 实际存取款时间
     */
	public LocalDateTime getApplyTime() {
		return applyTime;
	}

    /**
     * setter for 操作人员ID
     */
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

    /**
     * getter for 操作人员ID
     */
	public Long getCreatorId() {
		return creatorId;
	}

    /**
     * setter for 操作人员名称
     */
	public void setCreator(String creator) {
		this.creator = creator;
	}

    /**
     * getter for 操作人员名称
     */
	public String getCreator() {
		return creator;
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
     * BankCounterEntity.toString()
     */
    @Override
    public String toString() {
        return "BankCounterEntity{" +
               "id='" + id + '\'' +
               ", action='" + action + '\'' +
               ", amount='" + amount + '\'' +
               ", state='" + state + '\'' +
               ", serialNo='" + serialNo + '\'' +
               ", applyTime='" + applyTime + '\'' +
               ", creatorId='" + creatorId + '\'' +
               ", creator='" + creator + '\'' +
               ", firmId='" + firmId + '\'' +
               ", firmName='" + firmName + '\'' +
               ", notes='" + notes + '\'' +
               ", createTime='" + createTime + '\'' +
               ", modifyTime='" + modifyTime + '\'' +
               '}';
    }

}

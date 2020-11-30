package com.dili.card.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 银行存取款
 * @author bob
 */
public class BankCounterDo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Long id; 
	/** 动作-存款 取款 */
	private Integer action; 
	/** 操作金额-分 */
	private Long amount; 
	/** 状态-新建 封存 */
	private Integer status; 
	/** 银行操作流水号 */
	private Long serialNo; 
	/** 实际存取款时间 */
	private LocalDateTime applyTime; 
	/** 操作人员ID */
	private Long operatorId; 
	/** 操作人员名称 */
	private String operatorName; 
	/** 园区组织机构编码 */
	private String institutionCode; 
	/** 备注 */
	private String description; 
	/** 创建人ID */
	private LocalDateTime createdUserId; 
	/** 创建人姓名 */
	private LocalDateTime createdUserName; 
	/** 创建时间 */
	private LocalDateTime createdTime; 
	/** 修改时间 */
	private LocalDateTime modifiedTime; 
    /**
     * BankCounterEntity constructor
     */
	public BankCounterDo() {
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
	public void setStatus(Integer status) {
		this.status = status;
	}

    /**
     * getter for 状态-新建 封存
     */
	public Integer getStatus() {
		return status;
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
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

    /**
     * getter for 操作人员ID
     */
	public Long getOperatorId() {
		return operatorId;
	}

    /**
     * setter for 操作人员名称
     */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

    /**
     * getter for 操作人员名称
     */
	public String getOperatorName() {
		return operatorName;
	}

    /**
     * setter for 园区组织机构编码
     */
	public void setInstitutionCode(String institutionCode) {
		this.institutionCode = institutionCode;
	}

    /**
     * getter for 园区组织机构编码
     */
	public String getInstitutionCode() {
		return institutionCode;
	}

    /**
     * setter for 备注
     */
	public void setDescription(String description) {
		this.description = description;
	}

    /**
     * getter for 备注
     */
	public String getDescription() {
		return description;
	}

    /**
     * setter for 创建人ID
     */
	public void setCreatedUserId(LocalDateTime createdUserId) {
		this.createdUserId = createdUserId;
	}

    /**
     * getter for 创建人ID
     */
	public LocalDateTime getCreatedUserId() {
		return createdUserId;
	}

    /**
     * setter for 创建人姓名
     */
	public void setCreatedUserName(LocalDateTime createdUserName) {
		this.createdUserName = createdUserName;
	}

    /**
     * getter for 创建人姓名
     */
	public LocalDateTime getCreatedUserName() {
		return createdUserName;
	}

    /**
     * setter for 创建时间
     */
	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

    /**
     * getter for 创建时间
     */
	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

    /**
     * setter for 修改时间
     */
	public void setModifiedTime(LocalDateTime modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

    /**
     * getter for 修改时间
     */
	public LocalDateTime getModifiedTime() {
		return modifiedTime;
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
               ", status='" + status + '\'' +
               ", serialNo='" + serialNo + '\'' +
               ", applyTime='" + applyTime + '\'' +
               ", operatorId='" + operatorId + '\'' +
               ", operatorName='" + operatorName + '\'' +
               ", institutionCode='" + institutionCode + '\'' +
               ", description='" + description + '\'' +
               ", createdUserId='" + createdUserId + '\'' +
               ", createdUserName='" + createdUserName + '\'' +
               ", createdTime='" + createdTime + '\'' +
               ", modifiedTime='" + modifiedTime + '\'' +
               '}';
    }

}

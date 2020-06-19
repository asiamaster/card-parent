package com.dili.card.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用于记录柜员日常操作-办卡、充值等
 * @author bob
 */
public class UserLogDo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** 主键ID */
	private Long id; 
	/** 操作流水号 */
	private String serialNo; 
	/** 账务周期流水号 */
	private Long cycleNo; 
	/** 账户ID */
	private Long accountId;
	/** 客户ID*/
	private Long customerId;
	/** 客户编号 */
	private String customerNo; 
	/** 客户名称 */
	private String customerName; 
	/** 客户卡号 */
	private String cardNo; 
	/** 操作类型 */
	private Integer type; 
	/** 操作总金额 */
	private Long totalAmount; 
	/** 操作人ID */
	private Long operatorId; 
	/** 操作人工号 */
	private String operatorNo; 
	/** 操作人姓名 */
	private String operatorName; 
	/** 操作时间 */
	private LocalDateTime operateTime; 
	/** 商户ID */
	private Long firmId; 
	/**  */
	private String notes; 
	/** 附带内容(以json存储不同操作类型附加字段) */
	private String attach; 
    /**
     * UserLogEntity constructor
     */
	public UserLogDo() {
		super();
	}

    /**
     * setter for 主键ID
     */
	public void setId(Long id) {
		this.id = id;
	}

    /**
     * getter for 主键ID
     */
	public Long getId() {
		return id;
	}

    /**
     * setter for 操作流水号
     */
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

    /**
     * getter for 操作流水号
     */
	public String getSerialNo() {
		return serialNo;
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
     * setter for 账户ID
     */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

    /**
     * getter for 账户ID
     */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 *
	 * @return
	 */
	public Long getCustomerId() {
		return customerId;
	}

	/**
	 *
	 * @param customerId
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	/**
     * setter for 客户编号
     */
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

    /**
     * getter for 客户编号
     */
	public String getCustomerNo() {
		return customerNo;
	}

    /**
     * setter for 客户名称
     */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

    /**
     * getter for 客户名称
     */
	public String getCustomerName() {
		return customerName;
	}

    /**
     * setter for 客户卡号
     */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

    /**
     * getter for 客户卡号
     */
	public String getCardNo() {
		return cardNo;
	}

    /**
     * setter for 操作类型
     */
	public void setType(Integer type) {
		this.type = type;
	}

    /**
     * getter for 操作类型
     */
	public Integer getType() {
		return type;
	}

    /**
     * setter for 操作总金额
     */
	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

    /**
     * getter for 操作总金额
     */
	public Long getTotalAmount() {
		return totalAmount;
	}

    /**
     * setter for 操作人ID
     */
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

    /**
     * getter for 操作人ID
     */
	public Long getOperatorId() {
		return operatorId;
	}

    /**
     * setter for 操作人工号
     */
	public void setOperatorNo(String operatorNo) {
		this.operatorNo = operatorNo;
	}

    /**
     * getter for 操作人工号
     */
	public String getOperatorNo() {
		return operatorNo;
	}

    /**
     * setter for 操作人姓名
     */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

    /**
     * getter for 操作人姓名
     */
	public String getOperatorName() {
		return operatorName;
	}

    /**
     * setter for 操作时间
     */
	public void setOperateTime(LocalDateTime operateTime) {
		this.operateTime = operateTime;
	}

    /**
     * getter for 操作时间
     */
	public LocalDateTime getOperateTime() {
		return operateTime;
	}

    /**
     * setter for 商户ID
     */
	public void setFirmId(Long firmId) {
		this.firmId = firmId;
	}

    /**
     * getter for 商户ID
     */
	public Long getFirmId() {
		return firmId;
	}

    /**
     * setter for 
     */
	public void setNotes(String notes) {
		this.notes = notes;
	}

    /**
     * getter for 
     */
	public String getNotes() {
		return notes;
	}

    /**
     * setter for 附带内容(以json存储不同操作类型附加字段)
     */
	public void setAttach(String attach) {
		this.attach = attach;
	}

    /**
     * getter for 附带内容(以json存储不同操作类型附加字段)
     */
	public String getAttach() {
		return attach;
	}

    /**
     * UserLogEntity.toString()
     */
    @Override
    public String toString() {
        return "UserLogEntity{" +
               "id='" + id + '\'' +
               ", serialNo='" + serialNo + '\'' +
               ", cycleNo='" + cycleNo + '\'' +
               ", accountId='" + accountId + '\'' +
               ", customerNo='" + customerNo + '\'' +
               ", customerName='" + customerName + '\'' +
               ", cardNo='" + cardNo + '\'' +
               ", type='" + type + '\'' +
               ", totalAmount='" + totalAmount + '\'' +
               ", operatorId='" + operatorId + '\'' +
               ", operatorNo='" + operatorNo + '\'' +
               ", operatorName='" + operatorName + '\'' +
               ", operateTime='" + operateTime + '\'' +
               ", firmId='" + firmId + '\'' +
               ", notes='" + notes + '\'' +
               ", attach='" + attach + '\'' +
               '}';
    }

}

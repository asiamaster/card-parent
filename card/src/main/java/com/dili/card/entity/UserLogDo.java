package com.dili.card.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户操作记录
 * @author bob
 */
public class UserLogDo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** 主键ID */
	private Long id; 
	/** 操作流水号 */
	private String serialNo; 
	/** 服务类型-开卡 提现等 */
	private Integer type; 
	/** 账务周期流水号 */
	private Long cycleNo; 
	/** 用户账号 */
	private Long accountId; 
	/** 用户名称 */
	private Long name; 
	/** 卡号-保留字段 */
	private String cardNo; 
	/** 新卡卡号 */
	private String newCardNo; 
	/** 是否退还卡片 */
	private Integer cardReturned; 
	/** 押金-分 */
	private Long cashPledge; 
	/** 外部流水号 */
	private String outTradeNo; 
	/** 委托合同编号，提现 */
	private String consignorContractNo; 
	/** 代理人名称 */
	private String agentName; 
	/** 代理人身份证号 */
	private String agentIdCode; 
	/** 代理人联系电话 */
	private String agentTelphone; 
	/** 商户编码 */
	private Long firmId; 
	/** 商户名称 */
	private String firmName; 
	/** 操作人员 */
	private Long creatorId; 
	/** 操作员名称 */
	private String creator; 
	/** 备注 */
	private String notes; 
	/** 创建时间 */
	private LocalDateTime createTime; 
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
     * setter for 服务类型-开卡 提现等
     */
	public void setType(Integer type) {
		this.type = type;
	}

    /**
     * getter for 服务类型-开卡 提现等
     */
	public Integer getType() {
		return type;
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
     * setter for 用户账号
     */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

    /**
     * getter for 用户账号
     */
	public Long getAccountId() {
		return accountId;
	}

    /**
     * setter for 用户名称
     */
	public void setName(Long name) {
		this.name = name;
	}

    /**
     * getter for 用户名称
     */
	public Long getName() {
		return name;
	}

    /**
     * setter for 卡号-保留字段
     */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

    /**
     * getter for 卡号-保留字段
     */
	public String getCardNo() {
		return cardNo;
	}

    /**
     * setter for 新卡卡号
     */
	public void setNewCardNo(String newCardNo) {
		this.newCardNo = newCardNo;
	}

    /**
     * getter for 新卡卡号
     */
	public String getNewCardNo() {
		return newCardNo;
	}

    /**
     * setter for 是否退还卡片
     */
	public void setCardReturned(Integer cardReturned) {
		this.cardReturned = cardReturned;
	}

    /**
     * getter for 是否退还卡片
     */
	public Integer getCardReturned() {
		return cardReturned;
	}

    /**
     * setter for 押金-分
     */
	public void setCashPledge(Long cashPledge) {
		this.cashPledge = cashPledge;
	}

    /**
     * getter for 押金-分
     */
	public Long getCashPledge() {
		return cashPledge;
	}

    /**
     * setter for 外部流水号
     */
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

    /**
     * getter for 外部流水号
     */
	public String getOutTradeNo() {
		return outTradeNo;
	}

    /**
     * setter for 委托合同编号，提现
     */
	public void setConsignorContractNo(String consignorContractNo) {
		this.consignorContractNo = consignorContractNo;
	}

    /**
     * getter for 委托合同编号，提现
     */
	public String getConsignorContractNo() {
		return consignorContractNo;
	}

    /**
     * setter for 代理人名称
     */
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

    /**
     * getter for 代理人名称
     */
	public String getAgentName() {
		return agentName;
	}

    /**
     * setter for 代理人身份证号
     */
	public void setAgentIdCode(String agentIdCode) {
		this.agentIdCode = agentIdCode;
	}

    /**
     * getter for 代理人身份证号
     */
	public String getAgentIdCode() {
		return agentIdCode;
	}

    /**
     * setter for 代理人联系电话
     */
	public void setAgentTelphone(String agentTelphone) {
		this.agentTelphone = agentTelphone;
	}

    /**
     * getter for 代理人联系电话
     */
	public String getAgentTelphone() {
		return agentTelphone;
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
     * setter for 操作人员
     */
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

    /**
     * getter for 操作人员
     */
	public Long getCreatorId() {
		return creatorId;
	}

    /**
     * setter for 操作员名称
     */
	public void setCreator(String creator) {
		this.creator = creator;
	}

    /**
     * getter for 操作员名称
     */
	public String getCreator() {
		return creator;
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
     * UserLogEntity.toString()
     */
    @Override
    public String toString() {
        return "UserLogEntity{" +
               "id='" + id + '\'' +
               ", serialNo='" + serialNo + '\'' +
               ", type='" + type + '\'' +
               ", cycleNo='" + cycleNo + '\'' +
               ", accountId='" + accountId + '\'' +
               ", name='" + name + '\'' +
               ", cardNo='" + cardNo + '\'' +
               ", newCardNo='" + newCardNo + '\'' +
               ", cardReturned='" + cardReturned + '\'' +
               ", cashPledge='" + cashPledge + '\'' +
               ", outTradeNo='" + outTradeNo + '\'' +
               ", consignorContractNo='" + consignorContractNo + '\'' +
               ", agentName='" + agentName + '\'' +
               ", agentIdCode='" + agentIdCode + '\'' +
               ", agentTelphone='" + agentTelphone + '\'' +
               ", firmId='" + firmId + '\'' +
               ", firmName='" + firmName + '\'' +
               ", creatorId='" + creatorId + '\'' +
               ", creator='" + creator + '\'' +
               ", notes='" + notes + '\'' +
               ", createTime='" + createTime + '\'' +
               '}';
    }

}

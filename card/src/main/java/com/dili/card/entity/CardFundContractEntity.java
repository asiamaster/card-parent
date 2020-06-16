package com.dili.card.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 资金委托合同
 * @author bob
 */
public class CardFundContractEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Long id; 
	/** 合同编号 */
	private String contractNo; 
	/** 委托人账号ID */
	private Long consignorAccountId; 
	/** 合同开始日期 */
	private LocalDateTime startTime; 
	/** 合同结束日期 */
	private LocalDateTime endTime; 
	/** 状态(委托中、已解除、已到期) */
	private Integer state; 
	/** 委托人签名图片地址 */
	private String signatureImagePath; 
	/** 创建人ID */
	private Long creatorId; 
	/** 创建人姓名 */
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
     * CardFundContractEntity constructor
     */
	public CardFundContractEntity() {
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
     * setter for 合同编号
     */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

    /**
     * getter for 合同编号
     */
	public String getContractNo() {
		return contractNo;
	}

    /**
     * setter for 委托人账号ID
     */
	public void setConsignorAccountId(Long consignorAccountId) {
		this.consignorAccountId = consignorAccountId;
	}

    /**
     * getter for 委托人账号ID
     */
	public Long getConsignorAccountId() {
		return consignorAccountId;
	}

    /**
     * setter for 合同开始日期
     */
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

    /**
     * getter for 合同开始日期
     */
	public LocalDateTime getStartTime() {
		return startTime;
	}

    /**
     * setter for 合同结束日期
     */
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

    /**
     * getter for 合同结束日期
     */
	public LocalDateTime getEndTime() {
		return endTime;
	}

    /**
     * setter for 状态(委托中、已解除、已到期)
     */
	public void setState(Integer state) {
		this.state = state;
	}

    /**
     * getter for 状态(委托中、已解除、已到期)
     */
	public Integer getState() {
		return state;
	}

    /**
     * setter for 委托人签名图片地址
     */
	public void setSignatureImagePath(String signatureImagePath) {
		this.signatureImagePath = signatureImagePath;
	}

    /**
     * getter for 委托人签名图片地址
     */
	public String getSignatureImagePath() {
		return signatureImagePath;
	}

    /**
     * setter for 创建人ID
     */
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

    /**
     * getter for 创建人ID
     */
	public Long getCreatorId() {
		return creatorId;
	}

    /**
     * setter for 创建人姓名
     */
	public void setCreator(String creator) {
		this.creator = creator;
	}

    /**
     * getter for 创建人姓名
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
     * CardFundContractEntity.toString()
     */
    @Override
    public String toString() {
        return "CardFundContractEntity{" +
               "id='" + id + '\'' +
               ", contractNo='" + contractNo + '\'' +
               ", consignorAccountId='" + consignorAccountId + '\'' +
               ", startTime='" + startTime + '\'' +
               ", endTime='" + endTime + '\'' +
               ", state='" + state + '\'' +
               ", signatureImagePath='" + signatureImagePath + '\'' +
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

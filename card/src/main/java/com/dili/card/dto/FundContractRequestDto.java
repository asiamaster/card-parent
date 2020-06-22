package com.dili.card.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 资金委托合同
 * @author bob
 */
public class FundContractRequestDto{
	
	/** id */
	private Long id;
	/** 合同编号 */
	private String contractNo; 
	/** 委托人账号ID */
	private Long consignorAccountId; 
	/** 委托人账号ID */
	private Long customerId;
	/** 合同开始日期 */
	private LocalDateTime startTime; 
	/** 合同结束日期 */
	private LocalDateTime endTime; 
	/** 状态(委托中、已解除、已到期) */
	private Integer state; 
	/** 委托人签名图片地址 */
	private String signatureImagePath; 
	/** 备注 */
	private String notes; 
	/** 创建时间 */
	private LocalDateTime createTime; 
	/** 被委托人列表 */
	private List<FundConsignorDto> Consignors;
	/** 委托人卡信息 */
	private String cardNo;
	/** 委托人姓名或者编号 */
	private String consignorName;
	/** 创建开始时间 */
	private String createStartTime;
	/** 创建结束时间 */
	private String createEndTime;
	/** 距离到期天数 */
	private Integer days;
	/** 距离到期时间 */
	private String expirationTime;
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

	public List<FundConsignorDto> getConsignors() {
		return Consignors;
	}

	public void setConsignors(List<FundConsignorDto> consignors) {
		Consignors = consignors;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getConsignorName() {
		return consignorName;
	}

	public void setConsignorName(String consignorName) {
		this.consignorName = consignorName;
	}

	public String getCreateStartTime() {
		return createStartTime;
	}

	public void setCreateStartTime(String createStartTime) {
		this.createStartTime = createStartTime;
	}

	public String getCreateEndTime() {
		return createEndTime;
	}

	public void setCreateEndTime(String createEndTime) {
		this.createEndTime = createEndTime;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public String getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

}

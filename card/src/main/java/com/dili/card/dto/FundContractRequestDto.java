package com.dili.card.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 资金委托合同
 * @author bob
 */
public class FundContractRequestDto{
	
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
	/** 备注 */
	private String notes; 
	/** 创建时间 */
	private LocalDateTime createTime; 
	/** 被委托人列表 */
	private List<FundConsignorRequestDto> Consignors;

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

	public List<FundConsignorRequestDto> getConsignors() {
		return Consignors;
	}

	public void setConsignors(List<FundConsignorRequestDto> consignors) {
		Consignors = consignors;
	}

}

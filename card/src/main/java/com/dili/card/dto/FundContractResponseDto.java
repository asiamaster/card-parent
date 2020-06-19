package com.dili.card.dto;

import java.time.LocalDateTime;

/**
 * 资金委托合同
 * @author bob
 */
public class FundContractResponseDto{
	
	/** 委托人姓名 */
	private String consignorName; 
	/** 委托人卡号 */
	private String consignorCard; 
	/** 合同编号 */
	private String contractNo; 
	/** 合同开始日期 */
	private LocalDateTime startTime; 
	/** 合同结束日期 */
	private LocalDateTime endTime; 
	/** 状态(委托中、已解除、已到期) */
	private Integer state; 
	/** 备注 */
	private String notes; 
	/** 创建时间 */
	private LocalDateTime createTime; 
	/** 被委托人姓名 */
	private String names;
	/** 被委托人姓名 */
	private String mobiles;
	/** 创建人姓名 */
	private String creator; 

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

	public String getConsignorName() {
		return consignorName;
	}

	public void setConsignorName(String consignorName) {
		this.consignorName = consignorName;
	}

	public String getConsignorCard() {
		return consignorCard;
	}

	public void setConsignorCard(String consignorCard) {
		this.consignorCard = consignorCard;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getMobiles() {
		return mobiles;
	}

	public void setMobiles(String mobiles) {
		this.mobiles = mobiles;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
}

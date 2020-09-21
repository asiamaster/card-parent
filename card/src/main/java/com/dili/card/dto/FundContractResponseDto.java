package com.dili.card.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.card.common.annotation.TextDisplay;
import com.dili.card.common.provider.ContractStateProvider;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 资金委托合同
 * @author bob
 */
public class FundContractResponseDto{
	
	/** 委托人编号 */
	private Long id;
	/** 委托人卡号 */
	private String cardNo; 
	/** 委托人编号 */
	private String consignorCode; 
	/** 委托人姓名 */
	private String consignorName; 
	/** 委托人卡号 */
	private String consignorCard; 
	/** 委托人手机号 */
	private String consignorMobile;
	/** 委托人身份证 */
	private String consignorIdCode;
	/** 委托人签名图片地址 */
	private String signatureImagePath;
	/** 合同编号 */
	private String contractNo; 
	/** 合同开始日期 */
	@JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime startTime; 
	/** 合同结束日期 */
	@JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime endTime; 
	/** 状态(委托中、已解除、已到期) */
	@TextDisplay(ContractStateProvider.class)
	private Integer state; 
	/** 备注 */
	private String notes; 
	/** 创建时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime; 
	/** 被委托人姓名 */
	private String consigneeNames;
	/** 被委托人手机号 */
	private String consigneeMobiles;
	/** 创建人姓名 */
	private String creator; 
	/** 解除人 */
	private String terminater; 
	/** 解除人意见*/
	private String terminateNotes;
	/** 解除时间*/
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime terminateTime;
	/** 被委托人信息*/
	private List<FundConsignorDto> consignorDtos;
	/** 合同即将到期提示*/
	private Boolean readyExpire;
	
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

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getConsigneeNames() {
		return consigneeNames;
	}

	public void setConsigneeNames(String consigneeNames) {
		this.consigneeNames = consigneeNames;
	}

	public String getConsigneeMobiles() {
		return consigneeMobiles;
	}

	public void setConsigneeMobiles(String consigneeMobiles) {
		this.consigneeMobiles = consigneeMobiles;
	}

	public String getConsignorCode() {
		return consignorCode;
	}

	public void setConsignorCode(String consignorCode) {
		this.consignorCode = consignorCode;
	}

	public String getTerminater() {
		return terminater;
	}

	public void setTerminater(String terminater) {
		this.terminater = terminater;
	}

	public String getTerminateNotes() {
		return terminateNotes;
	}

	public void setTerminateNotes(String terminateNotes) {
		this.terminateNotes = terminateNotes;
	}

	public LocalDateTime getTerminateTime() {
		return terminateTime;
	}

	public void setTerminateTime(LocalDateTime terminateTime) {
		this.terminateTime = terminateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<FundConsignorDto> getConsignorDtos() {
		return consignorDtos;
	}

	public void setConsignorDtos(List<FundConsignorDto> consignorDtos) {
		this.consignorDtos = consignorDtos;
	}

	public Boolean getReadyExpire() {
		return readyExpire;
	}

	public void setReadyExpire(Boolean readyExpire) {
		this.readyExpire = readyExpire;
	}

	public String getConsignorMobile() {
		return consignorMobile;
	}

	public void setConsignorMobile(String consignorMobile) {
		this.consignorMobile = consignorMobile;
	}

	public String getConsignorIdCode() {
		return consignorIdCode;
	}

	public void setConsignorIdCode(String consignorIdCode) {
		this.consignorIdCode = consignorIdCode;
	}

	public String getSignatureImagePath() {
		return signatureImagePath;
	}

	public void setSignatureImagePath(String signatureImagePath) {
		this.signatureImagePath = signatureImagePath;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
}

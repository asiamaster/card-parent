package com.dili.card.dto;

import javax.validation.constraints.NotEmpty;

/**
 * 受委托人
 * @author bob
 */
public class FundConsignorDto {
	/** 被委托人id */
	private Long id;
	/** 合同编号 */
	private String contractNo; 
	/** 受托人姓名 */
	@NotEmpty(message = "受托人姓名不能为空")
	private String consigneeName; 
	/** 受委托人签名图片地址 */
	@NotEmpty(message = "受委托人签名图片地址不能为空")
	private String signatureImagePath; 
	/** 受托人证件号 */
	@NotEmpty(message = "受托人证件号不能为空")
	private String consigneeIdCode; 
	/** 受托人手机号 */
	@NotEmpty(message = "受托人手机号不能为空")
	private String consigneeIdMobile; 
	/** 合同即将到期提示*/
	private Boolean readyExpire;

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
     * setter for 受托人姓名
     */
	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

    /**
     * getter for 受托人姓名
     */
	public String getConsigneeName() {
		return consigneeName;
	}

    /**
     * setter for 受委托人签名图片地址
     */
	public void setSignatureImagePath(String signatureImagePath) {
		this.signatureImagePath = signatureImagePath;
	}

    /**
     * getter for 受委托人签名图片地址
     */
	public String getSignatureImagePath() {
		return signatureImagePath;
	}

    /**
     * setter for 受托人证件号
     */
	public void setConsigneeIdCode(String consigneeIdCode) {
		this.consigneeIdCode = consigneeIdCode;
	}

    /**
     * getter for 受托人证件号
     */
	public String getConsigneeIdCode() {
		return consigneeIdCode;
	}

    /**
     * setter for 受托人手机号
     */
	public void setConsigneeIdMobile(String consigneeIdMobile) {
		this.consigneeIdMobile = consigneeIdMobile;
	}

    /**
     * getter for 受托人手机号
     */
	public String getConsigneeIdMobile() {
		return consigneeIdMobile;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getReadyExpire() {
		return readyExpire;
	}

	public void setReadyExpire(Boolean readyExpire) {
		this.readyExpire = readyExpire;
	}
}

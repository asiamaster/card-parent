package com.dili.card.dto;

/**
 * 受委托人
 * @author bob
 */
public class FundConsignorRequestDto {
	
	/** 合同编号 */
	private String contractNo; 
	/** 受托人姓名 */
	private String consigneeName; 
	/** 受委托人签名图片地址 */
	private String signatureImagePath; 
	/** 受托人证件号 */
	private String consigneeIdCode; 
	/** 受托人手机号 */
	private String consigneeIdMobile; 

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
}

package com.dili.card.dto;

import java.io.Serializable;

/**
 * @description： 账户信息及客户信息
 * 
 * @author ：WangBo
 * @time ：2020年7月9日上午10:52:22
 */
public class AccountCustomerDto implements Serializable {
	/** */
	private static final long serialVersionUID = 1L;
	/** 业务账户ID */
	private Long accountId;
	/** 卡类型 */
	private Integer cardType;
	/** 卡号 */
	private String cardNo;
	/** 客户id主键 */
	private Long customerId;
	/** 客户编码 */
	private String code;
	/** 客户名称 */
	private String name;
	/** 客户类型 */
	private String customerType;
	/** 客户类型名称 */
	private String customerTypeName;
	/** 联系电话 */
	private String contactsPhone;
	/** 证件地址 */
	private String certificateAddr;
	/** 证件类型 */
	private String credentialType;
	/** 证件号 */
	private String certificateNumber;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactsPhone() {
		return contactsPhone;
	}

	public void setContactsPhone(String contactsPhone) {
		this.contactsPhone = contactsPhone;
	}

	public String getCertificateAddr() {
		return certificateAddr;
	}

	public void setCertificateAddr(String certificateAddr) {
		this.certificateAddr = certificateAddr;
	}

	public String getCustomerTypeName() {
		return customerTypeName;
	}

	public void setCustomerTypeName(String customerTypeName) {
		this.customerTypeName = customerTypeName;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	public String getCredentialType() {
		return credentialType;
	}

	public void setCredentialType(String credentialType) {
		this.credentialType = credentialType;
	}

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

}

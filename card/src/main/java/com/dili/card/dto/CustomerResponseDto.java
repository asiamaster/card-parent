package com.dili.card.dto;

import java.io.Serializable;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/22 13:45
 * @Description: 客户信息响应Dto
 */
public class CustomerResponseDto implements Serializable {
	/** */
	private static final long serialVersionUID = -193014575328948577L;
	/** 客户id主键 */
	private Long id;
	/** 客户编码 */
	private String code;
	/** 客户名称 */
	private String name;
	/** 客户类型 */
	private String customerType;
	/** 客户类型名称 */
	private String customerTypeName;
	/** 联系电话 */
	private String customerContactsPhone;
	/** 证件地址 */
	private String certificateAddr;
	/** 组织类型,个人/企业 */
	private String organizationType;
	/** 证件号 */
	private String certificateNumber;
	/** 证件类型 */
	private String certificateType;
	/** 市场属性ID */
	private Long firmId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getCustomerContactsPhone() {
		return customerContactsPhone;
	}

	public void setCustomerContactsPhone(String customerContactsPhone) {
		this.customerContactsPhone = customerContactsPhone;
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

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	public String getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}

	public Long getFirmId() {
		return firmId;
	}

	public void setFirmId(Long firmId) {
		this.firmId = firmId;
	}

}

package com.dili.card.dto;

import com.dili.card.type.CardType;

/**
 * @description： 开卡MQ通知其它业务系统使用实体
 * 
 * @author ：WangBo
 * @time ：2020年9月2日上午10:43:00
 */
public class OpenCardMqDto {
	/** 用户姓名 */
	private String customerName;
	/** CRM系统客户ID */
	private Long customerId;
	/** 客户编号 */
	private String customerCode;
	/** 卡号 */
	private String cardNo;
	/** 卡类别 {@link CardType} */
	private Integer cardType;
	/** 持卡人 */
	private String holdName;
	/** 持卡人证件号 */
	private String holdCertificateNumber;
	/** 持卡人联系电话 */
	private String holdContactsPhone;
	/** 商户ID */
	private Long firmId;
	/** 商户名称 */
	private String firmName;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	public Long getFirmId() {
		return firmId;
	}

	public void setFirmId(Long firmId) {
		this.firmId = firmId;
	}

	public String getFirmName() {
		return firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public String getHoldName() {
		return holdName;
	}

	public void setHoldName(String holdName) {
		this.holdName = holdName;
	}

	public String getHoldCertificateNumber() {
		return holdCertificateNumber;
	}

	public void setHoldCertificateNumber(String holdCertificateNumber) {
		this.holdCertificateNumber = holdCertificateNumber;
	}

	public String getHoldContactsPhone() {
		return holdContactsPhone;
	}

	public void setHoldContactsPhone(String holdContactsPhone) {
		this.holdContactsPhone = holdContactsPhone;
	}

}

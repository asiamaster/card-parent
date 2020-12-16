package com.dili.card.dto;

/**
 * @description： 
 *          检查卡面请求参数
 * @author ：WangBo
 * @time ：2020年12月9日下午2:15:53
 */
public class CheckCardDto {
	/** 卡号 */
	private String cardNo;
	/** 客户类型 */
	private String customerType;
	/** 卡类型 */
	private Integer cardType;
	/** 客户ID */
	private Long customerId;

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

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

}
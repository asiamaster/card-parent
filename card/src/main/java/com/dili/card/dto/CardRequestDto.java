package com.dili.card.dto;

/**
 *
 * 卡请求相关
 *
 */
public class CardRequestDto extends BaseDto{
	/** 卡号 */
	private String cardNo;
	/** 新卡号 */
	private String newCardNo;
	/** 账户ID */
	private Long accountId;
	/** 原来登录密码 */
	private String oldLoginPwd;
	/** 登录密码 */
	private String loginPwd;
	/** 二次输入登录密码 */
	private String secondLoginPwd;
	/** 客户ID*/
	private Long customerId;


	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public String getNewCardNo() {
		return newCardNo;
	}

	public void setNewCardNo(String newCardNo) {
		this.newCardNo = newCardNo;
	}

	public String getSecondLoginPwd() {
		return secondLoginPwd;
	}

	public void setSecondLoginPwd(String secondLoginPwd) {
		this.secondLoginPwd = secondLoginPwd;
	}

	public String getOldLoginPwd() {
		return oldLoginPwd;
	}

	public void setOldLoginPwd(String oldLoginPwd) {
		this.oldLoginPwd = oldLoginPwd;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
}

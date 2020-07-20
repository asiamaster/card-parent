package com.dili.card.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.dili.card.validator.CardValidator;

/**
 *
 * 卡请求相关
 *
 */
public class CardRequestDto extends BaseDto{
	private static final long serialVersionUID = 1L;
	/** 卡号 */
	private String cardNo;
	/** 新卡号 */
	private String newCardNo;
	/** 账户ID */
	@NotNull(message = "账号id不能为空",groups = CardValidator.Generic.class)
	@Min(value = 1,message = "id最小为1",groups = CardValidator.Generic.class)
	private Long accountId;
	/** 原来登录密码 */
	private String oldLoginPwd;
	/** 登录密码 */
	@NotNull(message = "密码不能为空",groups = CardValidator.Generic.class)
	@Size(min = 6, max = 6, message = "密码必须6位",groups = CardValidator.Generic.class)
	private String loginPwd;
	/** 二次输入登录密码 */
	@NotNull(message = "确认密码不能为空",groups = CardValidator.ResetPassword.class)
	@Size(min = 6, max = 6, message = "确认密码密码必须6位",groups = CardValidator.ResetPassword.class)
	private String secondLoginPwd;
	/** 客户ID*/
	private Long customerId;
	/**工本费*/
	private Long serviceFee;

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

	public Long getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(Long serviceFee) {
		this.serviceFee = serviceFee;
	}
}

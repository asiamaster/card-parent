package com.dili.card.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.card.common.annotation.TextDisplay;
import com.dili.card.common.provider.CardStateProvider;
import com.dili.card.common.provider.CardTypeProvider;
import com.dili.card.common.provider.DisableStateProvider;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/22 13:39
 * @Description: 卡账户列表响应Dto
 */
public class AccountListResponseDto extends BaseDto implements Serializable {
	/** */
	private static final long serialVersionUID = -1775032495865427107L;
	/** account主键id */
	private Long accountPkId;
	/** card主键id */
	private Long cardPkId;
	/** 账户业务id */
	private Long accountId;
	/** 卡号 */
	private String cardNo;
	/** 卡类别 {@link com.dili.card.type.CardType} */
	@TextDisplay(CardTypeProvider.class)
	private Integer cardType;
	/** 持卡人姓名 */
	private String holdName;
	/** 客户id */
	private Long customerId;
	/** 客户名称 */
	private String customerName;
	/** 客户编号 */
	private String customerCode;
	/** 客户角色 */
	private String customerCharacterType;
	/** 客户子类型 */
	private String customerSubTypeName;
	/** 客户电话 */
	private String customerContactsPhone;
	/** 卡状态 {@link com.dili.card.type.CardStatus} */
	@TextDisplay(CardStateProvider.class)
	private Integer cardState;
	/** 账户是否禁用 {@link com.dili.account.type.DisableState} */
	@TextDisplay(DisableStateProvider.class)
	private Integer disabledState;
	/** 开卡时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime cardCreateTime;

	public Integer getDisabledState() {
		return disabledState;
	}

	public void setDisabledState(Integer disabledState) {
		this.disabledState = disabledState;
	}

	public Long getAccountPkId() {
		return accountPkId;
	}

	public void setAccountPkId(Long accountPkId) {
		this.accountPkId = accountPkId;
	}

	public Long getCardPkId() {
		return cardPkId;
	}

	public void setCardPkId(Long cardPkId) {
		this.cardPkId = cardPkId;
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

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	public Integer getCardState() {
		return cardState;
	}

	public void setCardState(Integer cardState) {
		this.cardState = cardState;
	}

	public LocalDateTime getCardCreateTime() {
		return cardCreateTime;
	}

	public void setCardCreateTime(LocalDateTime cardCreateTime) {
		this.cardCreateTime = cardCreateTime;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerContactsPhone() {
		return customerContactsPhone;
	}

	public void setCustomerContactsPhone(String customerContactsPhone) {
		this.customerContactsPhone = customerContactsPhone;
	}

	public String getCustomerCharacterType() {
		return customerCharacterType;
	}

	public void setCustomerCharacterType(String customerCharacterType) {
		this.customerCharacterType = customerCharacterType;
	}

	public String getCustomerSubTypeName() {
		return customerSubTypeName;
	}

	public void setCustomerSubTypeName(String customerSubTypeName) {
		this.customerSubTypeName = customerSubTypeName;
	}

	public String getHoldName() {
		return holdName;
	}

	public void setHoldName(String holdName) {
		this.holdName = holdName;
	}

}

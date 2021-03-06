package com.dili.card.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.card.common.annotation.TextDisplay;
import com.dili.card.common.provider.AccountStateProvider;
import com.dili.card.common.provider.CardStateProvider;
import com.dili.card.common.provider.CardTypeProvider;
import com.dili.card.common.provider.DisableStateProvider;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/18 14:38
 * @Description: 卡账户响应Dto
 */
public class UserAccountCardResponseDto implements Serializable {
	/** */
	private static final long serialVersionUID = -1375512432139116399L;
	/** account主键id */
	private Long accountPkId;
	/** card主键id */
	private Long cardPkId;
	/** 市场id */
	private Long firmId;
	/** 卡账号id */
	private Long accountId;
	/** 父卡账号 */
	private Long parentAccountId;
	/** 卡交易类型: 1-买家 2-卖家 {@link com.dili.account.type.AccountType} */
	private Integer accountType;
	/** 资金账号ID */
	private Long fundAccountId;
	/** 客户id */
	private Long customerId;
	/** 客户名称 */
	private String customerName;
	/** 客户编号 */
	private String customerCode;
	/** 客户角色 */
	private String customerCharacterType;
	/** 客户子身份类型 */
	private String subTypeNames;
	/** 客户身份类型 */
	private String customerCertificateType;
	/** 客户身份号 */
	private String customerCertificateNumber;
	/** 客户电话 */
	private String customerContactsPhone;
	/** 持卡人姓名 */
	private String holdName;
	/** 持卡人证件号 */
	private String holdCertificateNumber;
	/** 持卡人联系电话 */
	private String holdContactsPhone;
	/** 使用权限(充值、提现、交费等) {@link com.dili.account.type.UsePermissionType} */
	private List<String> permissionList;
	/** 卡ID */
	private Long cardId;
	/** 卡号 */
	private String cardNo;
	/** 卡账户用途 {@link com.dili.account.type.AccountUsageType} */
	private List<String> usageType;
	/** 卡类型-主/副/临时/联营 {@link com.dili.card.type.CardType} */
	@TextDisplay(CardTypeProvider.class)
	private Integer cardType;
	/** 卡片状态 {@link com.dili.card.type.CardStatus} */
	@TextDisplay(CardStateProvider.class)
	private Integer cardState;
	/** 账户状态 {@link com.dili.card.type.AccountStatus} */
	@TextDisplay(AccountStateProvider.class)
	private Integer accountState;
	/** 账户是否禁用 {@link com.dili.account.type.DisableState} */
	@TextDisplay(DisableStateProvider.class)
	private Integer disabledState;
	/** 开卡时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime cardCreateTime;

	/** 建卡人名字 */
	private String creator;
	/** 建卡人id */
	private Long creatorId;

	private Boolean master;
	/**
	 * 组织类型,个人/企业 {@link com.dili.customer.sdk.enums.CustomerEnum.OrganizationType}
	 */
	private String organizationType;
	/** 实体卡是否存在 */
	private Integer cardExist;

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

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Integer getAccountState() {
		return accountState;
	}

	public void setAccountState(Integer accountState) {
		this.accountState = accountState;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getFirmId() {
		return firmId;
	}

	public void setFirmId(Long firmId) {
		this.firmId = firmId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getParentAccountId() {
		return parentAccountId;
	}

	public void setParentAccountId(Long parentAccountId) {
		this.parentAccountId = parentAccountId;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public Long getFundAccountId() {
		return fundAccountId;
	}

	public void setFundAccountId(Long fundAccountId) {
		this.fundAccountId = fundAccountId;
	}

	public Long getCardId() {
		return cardId;
	}

	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public List<String> getUsageType() {
		return usageType;
	}

	public void setUsageType(List<String> usageType) {
		this.usageType = usageType;
	}

	public List<String> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<String> permissionList) {
		this.permissionList = permissionList;
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

	public String getCustomerCertificateType() {
		return customerCertificateType;
	}

	public void setCustomerCertificateType(String customerCertificateType) {
		this.customerCertificateType = customerCertificateType;
	}

	public String getCustomerCertificateNumber() {
		return customerCertificateNumber;
	}

	public void setCustomerCertificateNumber(String customerCertificateNumber) {
		this.customerCertificateNumber = customerCertificateNumber;
	}

	public String getCustomerContactsPhone() {
		return customerContactsPhone;
	}

	public void setCustomerContactsPhone(String customerContactsPhone) {
		this.customerContactsPhone = customerContactsPhone;
	}

	public Integer getDisabledState() {
		return disabledState;
	}

	public void setDisabledState(Integer disabledState) {
		this.disabledState = disabledState;
	}

	public String getCustomerCharacterType() {
		return customerCharacterType;
	}

	public void setCustomerCharacterType(String customerCharacterType) {
		this.customerCharacterType = customerCharacterType;
	}

	public Boolean getMaster() {
		return parentAccountId == null;
	}

	public void setMaster(Boolean master) {
		this.master = master;
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

	public Integer getCardExist() {
		return cardExist;
	}

	public void setCardExist(Integer cardExist) {
		this.cardExist = cardExist;
	}

	public String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}

	public String getSubTypeNames() {
		return subTypeNames;
	}

	public void setSubTypeNames(String subTypeNames) {
		this.subTypeNames = subTypeNames;
	}

}

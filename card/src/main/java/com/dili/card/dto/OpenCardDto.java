package com.dili.card.dto;

import java.util.List;

import com.dili.card.type.CardType;
import com.dili.customer.sdk.domain.CharacterType;

/**
 * @description： 开卡所需要的用户信息
 *
 * @author ：WangBo
 * @time ：2020年7月3日下午3:38:26
 */
public class OpenCardDto extends BaseSerialLogDto {
	/** 用户姓名 */
	private String customerName;
	/** CRM系统客户ID */
	private Long customerId;
	/** 客户编号 */
	private String customerCode;
//	/** 客户类型买卖司机等 {@link CustomerType} */
//	private String customerType;
	/** 客户角色 */
	private List<CharacterType> customerCharacterTypeList;
	/** 客户角色 */
	private String customerCharacterType;
	/** 客户子身份类型 */
	private String customerSubTypes;
	/** 个人、对公 */
	private String customerOrganizationType;
	/** 证件类型 */
	private String customerCredentialType;
	/** 证件号 */
	private String customerCertificateNumber;
	/** 客户联系电话 */
	private String customerContactsPhone;
	/** 资金账户ID */
	private Long fundAccountId;
	/** 父资金账户ID */
	private Long parentFundAccountId;
	/** 登录密码 */
	private String loginPwd;
	/** 交易密码 */
	private String tradePwd;
	/** 开户来源 {@link CreateSource} */
	private Integer createSource;
	/** 卡号 */
	private String cardNo;
	/** 是否存在实体卡1-存在，2-不存在 */
	private Integer cardExist;

	/** 卡类别 {@link CardType} */
	private Integer cardType;
	/** 父账号ID */
	private Long parentAccountId;
	/** 父账号密码 */
	private String parentLoginPwd;
	/** 商户ID */
	private Long firmId;
	/** 商户名称 */
	private String firmName;
	/***/
	private String firmCode;
	/** 工本费 */
	private Long costFee;
	/** 持卡人 */
	private String holdName;
	/** 持卡人证件号 */
	private String holdCertificateNumber;
	/** 持卡人联系电话 */
	private String holdContactsPhone;
	/** 客户信息修改时是否同步修改持卡人信息1-是，2-否 */
	private Integer customerSyncModifyHoldinfo;

	public String getFirmCode() {
		return firmCode;
	}

	public void setFirmCode(String firmCode) {
		this.firmCode = firmCode;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Integer getCreateSource() {
		return createSource;
	}

	public void setCreateSource(Integer createSource) {
		this.createSource = createSource;
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

	public Long getParentAccountId() {
		return parentAccountId;
	}

	public void setParentAccountId(Long parentAccountId) {
		this.parentAccountId = parentAccountId;
	}

	public Long getFundAccountId() {
		return fundAccountId;
	}

	public void setFundAccountId(Long fundAccountId) {
		this.fundAccountId = fundAccountId;
	}

	public String getTradePwd() {
		return tradePwd;
	}

	public void setTradePwd(String tradePwd) {
		this.tradePwd = tradePwd;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
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

	public Long getCostFee() {
		return costFee;
	}

	public void setCostFee(Long costFee) {
		this.costFee = costFee;
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

	public String getCustomerOrganizationType() {
		return customerOrganizationType;
	}

	public void setCustomerOrganizationType(String customerOrganizationType) {
		this.customerOrganizationType = customerOrganizationType;
	}

	public String getCustomerCredentialType() {
		return customerCredentialType;
	}

	public void setCustomerCredentialType(String customerCredentialType) {
		this.customerCredentialType = customerCredentialType;
	}

	public String getCustomerCertificateNumber() {
		return customerCertificateNumber;
	}

	public void setCustomerCertificateNumber(String customerCertificateNumber) {
		this.customerCertificateNumber = customerCertificateNumber;
	}

	public Integer getCardExist() {
		return cardExist;
	}

	public void setCardExist(Integer cardExist) {
		this.cardExist = cardExist;
	}

	public String getCustomerContactsPhone() {
		return customerContactsPhone;
	}

	public void setCustomerContactsPhone(String customerContactsPhone) {
		this.customerContactsPhone = customerContactsPhone;
	}

	public String getParentLoginPwd() {
		return parentLoginPwd;
	}

	public void setParentLoginPwd(String parentLoginPwd) {
		this.parentLoginPwd = parentLoginPwd;
	}

	public Long getParentFundAccountId() {
		return parentFundAccountId;
	}

	public void setParentFundAccountId(Long parentFundAccountId) {
		this.parentFundAccountId = parentFundAccountId;
	}

	public String getHoldName() {
		return holdName;
	}

	public void setHoldName(String holdName) {
		this.holdName = holdName;
	}

	public String getCustomerCharacterType() {
		return customerCharacterType;
	}

	public void setCustomerCharacterType(String customerCharacterType) {
		this.customerCharacterType = customerCharacterType;
	}

	public List<CharacterType> getCustomerCharacterTypeList() {
		return customerCharacterTypeList;
	}

	public void setCustomerCharacterTypeList(List<CharacterType> customerCharacterTypeList) {
		this.customerCharacterTypeList = customerCharacterTypeList;
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

	public Integer getCustomerSyncModifyHoldinfo() {
		return customerSyncModifyHoldinfo;
	}

	public void setCustomerSyncModifyHoldinfo(Integer customerSyncModifyHoldinfo) {
		this.customerSyncModifyHoldinfo = customerSyncModifyHoldinfo;
	}

	public String getCustomerSubTypes() {
		return customerSubTypes;
	}

	public void setCustomerSubTypes(String customerSubTypes) {
		this.customerSubTypes = customerSubTypes;
	}

}

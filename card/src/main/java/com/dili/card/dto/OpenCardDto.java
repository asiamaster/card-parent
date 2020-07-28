package com.dili.card.dto;

import java.time.LocalDateTime;

import com.dili.card.type.AccountType;
import com.dili.card.type.CardType;
import com.dili.card.type.UsePermissionType;

/**
 * @description： 开卡所需要的用户信息
 * 
 * @author ：WangBo
 * @time ：2020年7月3日下午3:38:26
 */
public class OpenCardDto extends BaseSerialLogDto {
	/** 用户姓名 */
	private String name;
	/** CRM系统客户ID */
	private Long customerId;
	/** 客户类型买卖 {@link AccountType} */
	private String custormerType;
	/** 个人、对公 */
	private String organizationType;
	/** 证件类型 */
	private String credentialType;
	/** 证件号 */
	private String certificateNumber;
	/** 证件有效期 */
	private LocalDateTime validityDate;
	/** 手机号 */
	private String mobile;
	/** 资金账户ID */
	private Long fundAccountId;
	/** 账户类型(主、子/副) */
	private Integer accountType;
	/** 登录密码 */
	private String loginPwd;
	/** 交易密码 */
	private String tradePwd;
	/** 卡交易类型 */
	private Integer usageType;
	/** 开户来源 {@link CreateSource} */
	private Integer createSource;
	/** 业务权限集合 {@link UsePermissionType},以逗号分隔 */
	private String usePermission;
	/** 卡号 */
	private String cardNo;

	/** 卡类别 {@link CardType} */
	private Integer cardType;
	/** {@link CardType} */
	private Integer seinsweise;
	/** 父账号ID */
	private Long parentAccountId;
	/** 商户ID */
	private Long firmId;
	/** 商户名称 */
	private String firmName;
	/** 工本费 */
	private Long costFee;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustormerType() {
		return custormerType;
	}

	public void setCustomerType(String custormerType) {
		this.custormerType = custormerType;
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

	public LocalDateTime getValidityDate() {
		return validityDate;
	}

	public void setValidityDate(LocalDateTime validityDate) {
		this.validityDate = validityDate;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public Integer getUsageType() {
		return usageType;
	}

	public void setUsageType(Integer usageType) {
		this.usageType = usageType;
	}

	public Integer getCreateSource() {
		return createSource;
	}

	public void setCreateSource(Integer createSource) {
		this.createSource = createSource;
	}

	public String getUsePermission() {
		return usePermission;
	}

	public void setUsePermission(String usePermission) {
		this.usePermission = usePermission;
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

	public Integer getSeinsweise() {
		return seinsweise;
	}

	public void setSeinsweise(Integer seinsweise) {
		this.seinsweise = seinsweise;
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

	public void setCustormerType(String custormerType) {
		this.custormerType = custormerType;
	}

	public String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}

	public Long getCostFee() {
		return costFee;
	}

	public void setCostFee(Long costFee) {
		this.costFee = costFee;
	}

}

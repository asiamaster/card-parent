package com.dili.card.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户账户信息
 * @author bob
 */
public class UserAccountDo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Long id; 
	/** 业务主键，其它业务外键都引用该键 */
	private Long accountId; 
	/** 父账号ID */
	private Long parentAccountId; 
	/** 客户ID */
	private Long custormerId; 
	/** 资金账号 */
	private Long fundAccountId; 
	/** 账号类型-买家账户/卖家账户 */
	private Integer type; 
	/** 账户用途-交易账户/水电费账户 */
	private Integer usageType; 
	/** 持有人姓名 */
	private String holderName; 
	/** 持有人性别 */
	private Integer holderGender; 
	/** 持有人手机号 */
	private String holderMobile; 
	/** 持有人证件号码 */
	private String holderCertificateNumber; 
	/** 持有人证件类型 */
	private String holderCertificateType; 
	/** 持有人联系地址 */
	private String holderAddress; 
	/** 使用权限(充值、提现、交费等),多个以逗号分隔 */
	private String permissions; 
	/** 登陆密码 */
	private String loginPwd; 
	/** 强制修改密码 */
	private Integer pwdChanged; 
	/** 最近登陆时间 */
	private LocalDateTime loginTime; 
	/** 安全密钥 */
	private String secretKey; 
	/** 账户状态-正常/锁定/禁用/注销 */
	private Integer state; 
	/** 注册来源-柜台 APP */
	private Integer source; 
	/** 数据版本号 */
	private Integer version; 
	/** 商户ID */
	private Long firmId;
	/** 商户名称 */
	private String firmName; 
	/** 创建时间 */
	private LocalDateTime createTime; 
	/** 修改时间 */
	private LocalDateTime modifyTime; 
    /**
     * UserAccountEntity constructor
     */
	public UserAccountDo() {
		super();
	}

    /**
     * setter for 
     */
	public void setId(Long id) {
		this.id = id;
	}

    /**
     * getter for 
     */
	public Long getId() {
		return id;
	}

    /**
     * setter for 业务主键，其它业务外键都引用该键
     */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

    /**
     * getter for 业务主键，其它业务外键都引用该键
     */
	public Long getAccountId() {
		return accountId;
	}

    /**
     * setter for 父账号ID
     */
	public void setParentAccountId(Long parentAccountId) {
		this.parentAccountId = parentAccountId;
	}

    /**
     * getter for 父账号ID
     */
	public Long getParentAccountId() {
		return parentAccountId;
	}

    /**
     * setter for 客户ID
     */
	public void setCustormerId(Long custormerId) {
		this.custormerId = custormerId;
	}

    /**
     * getter for 客户ID
     */
	public Long getCustormerId() {
		return custormerId;
	}

    /**
     * setter for 资金账号
     */
	public void setFundAccountId(Long fundAccountId) {
		this.fundAccountId = fundAccountId;
	}

    /**
     * getter for 资金账号
     */
	public Long getFundAccountId() {
		return fundAccountId;
	}

    /**
     * setter for 账号类型-买家账户/卖家账户
     */
	public void setType(Integer type) {
		this.type = type;
	}

    /**
     * getter for 账号类型-买家账户/卖家账户
     */
	public Integer getType() {
		return type;
	}

    /**
     * setter for 账户用途-交易账户/水电费账户
     */
	public void setUsageType(Integer usageType) {
		this.usageType = usageType;
	}

    /**
     * getter for 账户用途-交易账户/水电费账户
     */
	public Integer getUsageType() {
		return usageType;
	}

    /**
     * setter for 持有人姓名
     */
	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

    /**
     * getter for 持有人姓名
     */
	public String getHolderName() {
		return holderName;
	}

    /**
     * setter for 持有人性别
     */
	public void setHolderGender(Integer holderGender) {
		this.holderGender = holderGender;
	}

    /**
     * getter for 持有人性别
     */
	public Integer getHolderGender() {
		return holderGender;
	}

    /**
     * setter for 持有人手机号
     */
	public void setHolderMobile(String holderMobile) {
		this.holderMobile = holderMobile;
	}

    /**
     * getter for 持有人手机号
     */
	public String getHolderMobile() {
		return holderMobile;
	}

    /**
     * setter for 持有人证件号码
     */
	public void setHolderCertificateNumber(String holderCertificateNumber) {
		this.holderCertificateNumber = holderCertificateNumber;
	}

    /**
     * getter for 持有人证件号码
     */
	public String getHolderCertificateNumber() {
		return holderCertificateNumber;
	}

    /**
     * setter for 持有人证件类型
     */
	public void setHolderCertificateType(String holderCertificateType) {
		this.holderCertificateType = holderCertificateType;
	}

    /**
     * getter for 持有人证件类型
     */
	public String getHolderCertificateType() {
		return holderCertificateType;
	}

    /**
     * setter for 持有人联系地址
     */
	public void setHolderAddress(String holderAddress) {
		this.holderAddress = holderAddress;
	}

    /**
     * getter for 持有人联系地址
     */
	public String getHolderAddress() {
		return holderAddress;
	}

    /**
     * setter for 使用权限(充值、提现、交费等),多个以逗号分隔
     */
	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

    /**
     * getter for 使用权限(充值、提现、交费等),多个以逗号分隔
     */
	public String getPermissions() {
		return permissions;
	}

    /**
     * setter for 登陆密码
     */
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

    /**
     * getter for 登陆密码
     */
	public String getLoginPwd() {
		return loginPwd;
	}

    /**
     * setter for 强制修改密码
     */
	public void setPwdChanged(Integer pwdChanged) {
		this.pwdChanged = pwdChanged;
	}

    /**
     * getter for 强制修改密码
     */
	public Integer getPwdChanged() {
		return pwdChanged;
	}

    /**
     * setter for 最近登陆时间
     */
	public void setLoginTime(LocalDateTime loginTime) {
		this.loginTime = loginTime;
	}

    /**
     * getter for 最近登陆时间
     */
	public LocalDateTime getLoginTime() {
		return loginTime;
	}

    /**
     * setter for 安全密钥
     */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

    /**
     * getter for 安全密钥
     */
	public String getSecretKey() {
		return secretKey;
	}

    /**
     * setter for 账户状态-正常/锁定/禁用/注销
     */
	public void setState(Integer state) {
		this.state = state;
	}

    /**
     * getter for 账户状态-正常/锁定/禁用/注销
     */
	public Integer getState() {
		return state;
	}

    /**
     * setter for 注册来源-柜台 APP
     */
	public void setSource(Integer source) {
		this.source = source;
	}

    /**
     * getter for 注册来源-柜台 APP
     */
	public Integer getSource() {
		return source;
	}

    /**
     * setter for 数据版本号
     */
	public void setVersion(Integer version) {
		this.version = version;
	}

    /**
     * getter for 数据版本号
     */
	public Integer getVersion() {
		return version;
	}

    /**
     * setter for 商户ID
     */
	public void setFirmId(Long firmId) {
		this.firmId = firmId;
	}

    /**
     * getter for 商户ID
     */
	public Long getFirmId() {
		return firmId;
	}

    /**
     * setter for 商户名称
     */
	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

    /**
     * getter for 商户名称
     */
	public String getFirmName() {
		return firmName;
	}

    /**
     * setter for 创建时间
     */
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

    /**
     * getter for 创建时间
     */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

    /**
     * setter for 修改时间
     */
	public void setModifyTime(LocalDateTime modifyTime) {
		this.modifyTime = modifyTime;
	}

    /**
     * getter for 修改时间
     */
	public LocalDateTime getModifyTime() {
		return modifyTime;
	}

    /**
     * UserAccountEntity.toString()
     */
    @Override
    public String toString() {
        return "UserAccountEntity{" +
               "id='" + id + '\'' +
               ", accountId='" + accountId + '\'' +
               ", parentAccountId='" + parentAccountId + '\'' +
               ", custormerId='" + custormerId + '\'' +
               ", fundAccountId='" + fundAccountId + '\'' +
               ", type='" + type + '\'' +
               ", usageType='" + usageType + '\'' +
               ", holderName='" + holderName + '\'' +
               ", holderGender='" + holderGender + '\'' +
               ", holderMobile='" + holderMobile + '\'' +
               ", holderCertificateNumber='" + holderCertificateNumber + '\'' +
               ", holderCertificateType='" + holderCertificateType + '\'' +
               ", holderAddress='" + holderAddress + '\'' +
               ", permissions='" + permissions + '\'' +
               ", loginPwd='" + loginPwd + '\'' +
               ", pwdChanged='" + pwdChanged + '\'' +
               ", loginTime='" + loginTime + '\'' +
               ", secretKey='" + secretKey + '\'' +
               ", state='" + state + '\'' +
               ", source='" + source + '\'' +
               ", version='" + version + '\'' +
               ", firmId='" + firmId + '\'' +
               ", firmName='" + firmName + '\'' +
               ", createTime='" + createTime + '\'' +
               ", modifyTime='" + modifyTime + '\'' +
               '}';
    }

}

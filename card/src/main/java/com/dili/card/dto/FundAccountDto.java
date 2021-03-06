package com.dili.card.dto;

/**
 * @description： 创建资金账号相关参数
 * 
 * @author ：WangBo
 * @time ：2020年6月22日下午5:59:18
 */
public class FundAccountDto {
	// 客户ID 必须
	private Long customerId;
	// 主资金账号ID 
	private Long parentId;
	// 账号类型 必须
	private Integer type;
	// 业务用途 必须
	private Integer useFor;
	// 登陆账号-卡号
	private String code;
	// 姓名 必须
	private String name;
	// 性别
	private Integer gender;
	// 手机号 必须
	private String mobile;
	// 邮箱
	private String email;
	// 身份证编号
	private String idCode;
	// 地址
	private String address;
	// 交易密码 必须
	private String password;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getUseFor() {
		return useFor;
	}

	public void setUseFor(Integer useFor) {
		this.useFor = useFor;
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

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdCode() {
		return idCode;
	}

	public void setIdCode(String idCode) {
		this.idCode = idCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

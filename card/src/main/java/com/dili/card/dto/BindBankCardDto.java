package com.dili.card.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.dili.card.validator.ConstantValidator;

/**
 * 园区账户绑定银行卡
 * 
 * @author bob
 */
public class BindBankCardDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/**  */
	private Long id;
	/** 账户类型-个人账户 对公账户 */
	private Integer accountType;
	/** 园区卡账号 */
	private Long accountId;
	/** 资金账号ID */
	private Long fundAccountId;
	/** 银行类型-工商银行 */
	private Integer bankType;
	/** 银行卡号/对公账号 */
	private String bankNo;
	/** 开户行如:成都XX银行XX支行 */
	private String openingBank;
	/** 开户行编码 */
	private String openingBankNum;
	/** 姓名 */
	private String name;
	/** 绑定状态 */
	private Integer status;
	/** 员工ID */
	private Long operatorId;
	/** 员工名称-保留字段 */
	private String operatorName;
	/** 创建时间 */
	private LocalDateTime createdTime;
	/** 修改时间 */
	private LocalDateTime modifiedTime;
	/** 备注 */
	private String description;

	/** 页码 */
	private Integer page;
	/** 每行多少条 */
	private Integer rows;

	/**
	 * BankBindingEntity constructor
	 */
	public BindBankCardDto() {
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
	 * setter for 账户类型-个人账户 对公账户
	 */
	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	/**
	 * getter for 账户类型-个人账户 对公账户
	 */
	public Integer getAccountType() {
		return accountType;
	}

	/**
	 * setter for 园区卡账号
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	/**
	 * getter for 园区卡账号
	 */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 * setter for 资金账号ID
	 */
	public void setFundAccountId(Long fundAccountId) {
		this.fundAccountId = fundAccountId;
	}

	/**
	 * getter for 资金账号ID
	 */
	public Long getFundAccountId() {
		return fundAccountId;
	}

	/**
	 * setter for 银行类型-工商银行
	 */
	public void setBankType(Integer bankType) {
		this.bankType = bankType;
	}

	/**
	 * getter for 银行类型-工商银行
	 */
	public Integer getBankType() {
		return bankType;
	}

	/**
	 * setter for 银行卡号/对公账号
	 */
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	/**
	 * getter for 银行卡号/对公账号
	 */
	public String getBankNo() {
		return bankNo;
	}

	/**
	 * setter for 开户行如:成都XX银行XX支行
	 */
	public void setOpeningBank(String openingBank) {
		this.openingBank = openingBank;
	}

	/**
	 * getter for 开户行如:成都XX银行XX支行
	 */
	public String getOpeningBank() {
		return openingBank;
	}

	/**
	 * setter for 开户行编码
	 */
	public void setOpeningBankNum(String openingBankNum) {
		this.openingBankNum = openingBankNum;
	}

	/**
	 * getter for 开户行编码
	 */
	public String getOpeningBankNum() {
		return openingBankNum;
	}

	/**
	 * setter for 姓名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * getter for 姓名
	 */
	public String getName() {
		return name;
	}

	/**
	 * setter for 绑定状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * getter for 绑定状态
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * setter for 员工ID
	 */
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	/**
	 * getter for 员工ID
	 */
	public Long getOperatorId() {
		return operatorId;
	}

	/**
	 * setter for 员工名称-保留字段
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	/**
	 * getter for 员工名称-保留字段
	 */
	public String getOperatorName() {
		return operatorName;
	}

	/**
	 * setter for 创建时间
	 */
	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * getter for 创建时间
	 */
	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	/**
	 * setter for 修改时间
	 */
	public void setModifiedTime(LocalDateTime modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	/**
	 * getter for 修改时间
	 */
	public LocalDateTime getModifiedTime() {
		return modifiedTime;
	}

	/**
	 * setter for 备注
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * getter for 备注
	 */
	public String getDescription() {
		return description;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	/**
	 * BankBindingEntity.toString()
	 */
	@Override
	public String toString() {
		return "BankBindingEntity{" + "id='" + id + '\'' + ", accountType='" + accountType + '\'' + ", accountId='"
				+ accountId + '\'' + ", fundAccountId='" + fundAccountId + '\'' + ", bankType='" + bankType + '\''
				+ ", bankNo='" + bankNo + '\'' + ", openingBank='" + openingBank + '\'' + ", openingBankNum='"
				+ openingBankNum + '\'' + ", name='" + name + '\'' + ", status='" + status + '\'' + ", operatorId='"
				+ operatorId + '\'' + ", operatorName='" + operatorName + '\'' + ", createdTime='" + createdTime + '\''
				+ ", modifiedTime='" + modifiedTime + '\'' + ", description='" + description + '\'' + '}';
	}

}

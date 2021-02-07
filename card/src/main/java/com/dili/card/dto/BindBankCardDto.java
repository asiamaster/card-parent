package com.dili.card.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.card.type.BankAccountType;
import com.dili.card.type.BindBankStatus;
import com.dili.card.util.CardNoUtil;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 园区账户绑定银行卡
 * 
 * @author bob
 */
public class BindBankCardDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/**  */
	private Long id;
	/** 账户类型-个人账户 对公账户 */
	private Integer bankAccountType;
	/** 账户类型-个人账户 对公账户 */
	private String bankAccountTypeText;
	/** 园区卡账号 */
	private Long accountId;
	/** 客户名称 */
	private String customerName;
	/** 客户编号 */
	private String customerCode;
	/** 卡号 */
	private String cardNo;
	/** 资金账号ID */
	private Long fundAccountId;
	/** 银行类型-工商银行 */
	private Integer bankType;
	/** 银行名称 */
	private String bankName;
	/** 银行卡号/对公账号 */
	private String bankNo;
	/** 银行卡号尾号 */
	private String bankNoTailNumber;

	/** 开户行如:成都XX银行XX支行 */
	private String openingBank;
	/** 开户行编码 */
	private String openingBankNum;
	/** 姓名 */
	private String name;
	/** 绑定状态 */
	private Integer status;
	private String statusText;
	/** 创建人 */
	private String operatorName;
	/** 创建时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;
	/** 修改时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime modifyTime;
	/** 备注 */
	private String description;
	/** 密码 */
	private String loginPwd;

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

	public Integer getBankAccountType() {
		return bankAccountType;
	}

	public void setBankAccountType(Integer bankAccountType) {
		this.bankAccountType = bankAccountType;
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

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(LocalDateTime modifyTime) {
		this.modifyTime = modifyTime;
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

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
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

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getBankAccountTypeText() {
		if (bankAccountType == null) {
			bankAccountTypeText = "";
		} else {
			bankAccountTypeText = BankAccountType.getAccountType(bankAccountType).getName();
		}
		return bankAccountTypeText;
	}

	public String getStatusText() {
		if (status == null) {
			statusText = "";
		} else {
			statusText = BindBankStatus.getName(status);
		}
		return statusText;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public String getBankNoTailNumber() {
		if(StringUtils.isBlank(bankNo)) {
			return "";
		}
		bankNoTailNumber = CardNoUtil.getTailNumber(bankNo);
		return bankNoTailNumber;
	}

}

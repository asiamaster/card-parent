package com.dili.card.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 账户资金操作记录,在柜员办理的业务,仅用作记录，统计结账以支付系统为主
 * @author bob
 */
public class FundLogEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Long id; 
	/** 操作流水号 */
	private String serialNo; 
	/** 服务类型-开卡 提现等 */
	private Integer type; 
	/** 对方账号ID */
	private Long toAccountId; 
	/** 对方用户名称 */
	private Long toName; 
	/** 对方卡号 */
	private String toCardNo; 
	/** 账务周期流水号 */
	private Long cycleNo; 
	/** 操作前账户余额 */
	private Long balance; 
	/** 操作金额-分 */
	private Long amount; 
	/** 操作原始金额， */
	private Long originalAmount; 
	/** 费用总金额-分 */
	private Long totalFee; 
	/** 渠道类型，现金 POS、银行卡等 */
	private Integer channelType; 
	/** 银行卡类型，借记卡、信用卡 */
	private Integer bankCardType; 
	/** 商户编码 */
	private Long firmId; 
	/** 商户名称 */
	private String firmName; 
	/** 1-成功，2-失败，3-处理中 */
	private Integer state; 
	/** 数据版本号 */
	private Integer version; 
	/** 转帐交易流水号-支付系统 */
	private String paySerialNo; 
	/** 备注 */
	private String notes; 
	/** 操作人员 */
	private Long creatorId; 
	/** 操作员名称 */
	private String creator; 
	/** 修改时间 */
	private LocalDateTime modifyTime; 
	/** 创建时间 */
	private LocalDateTime createTime; 
    /**
     * FundLogEntity constructor
     */
	public FundLogEntity() {
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
     * setter for 操作流水号
     */
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

    /**
     * getter for 操作流水号
     */
	public String getSerialNo() {
		return serialNo;
	}

    /**
     * setter for 服务类型-开卡 提现等
     */
	public void setType(Integer type) {
		this.type = type;
	}

    /**
     * getter for 服务类型-开卡 提现等
     */
	public Integer getType() {
		return type;
	}

    /**
     * setter for 对方账号ID
     */
	public void setToAccountId(Long toAccountId) {
		this.toAccountId = toAccountId;
	}

    /**
     * getter for 对方账号ID
     */
	public Long getToAccountId() {
		return toAccountId;
	}

    /**
     * setter for 对方用户名称
     */
	public void setToName(Long toName) {
		this.toName = toName;
	}

    /**
     * getter for 对方用户名称
     */
	public Long getToName() {
		return toName;
	}

    /**
     * setter for 对方卡号
     */
	public void setToCardNo(String toCardNo) {
		this.toCardNo = toCardNo;
	}

    /**
     * getter for 对方卡号
     */
	public String getToCardNo() {
		return toCardNo;
	}

    /**
     * setter for 账务周期流水号
     */
	public void setCycleNo(Long cycleNo) {
		this.cycleNo = cycleNo;
	}

    /**
     * getter for 账务周期流水号
     */
	public Long getCycleNo() {
		return cycleNo;
	}

    /**
     * setter for 操作前账户余额
     */
	public void setBalance(Long balance) {
		this.balance = balance;
	}

    /**
     * getter for 操作前账户余额
     */
	public Long getBalance() {
		return balance;
	}

    /**
     * setter for 操作金额-分
     */
	public void setAmount(Long amount) {
		this.amount = amount;
	}

    /**
     * getter for 操作金额-分
     */
	public Long getAmount() {
		return amount;
	}

    /**
     * setter for 操作原始金额，
     */
	public void setOriginalAmount(Long originalAmount) {
		this.originalAmount = originalAmount;
	}

    /**
     * getter for 操作原始金额，
     */
	public Long getOriginalAmount() {
		return originalAmount;
	}

    /**
     * setter for 费用总金额-分
     */
	public void setTotalFee(Long totalFee) {
		this.totalFee = totalFee;
	}

    /**
     * getter for 费用总金额-分
     */
	public Long getTotalFee() {
		return totalFee;
	}

    /**
     * setter for 渠道类型，现金 POS、银行卡等
     */
	public void setChannelType(Integer channelType) {
		this.channelType = channelType;
	}

    /**
     * getter for 渠道类型，现金 POS、银行卡等
     */
	public Integer getChannelType() {
		return channelType;
	}

    /**
     * setter for 银行卡类型，借记卡、信用卡
     */
	public void setBankCardType(Integer bankCardType) {
		this.bankCardType = bankCardType;
	}

    /**
     * getter for 银行卡类型，借记卡、信用卡
     */
	public Integer getBankCardType() {
		return bankCardType;
	}

    /**
     * setter for 商户编码
     */
	public void setFirmId(Long firmId) {
		this.firmId = firmId;
	}

    /**
     * getter for 商户编码
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
     * setter for 1-成功，2-失败，3-处理中
     */
	public void setState(Integer state) {
		this.state = state;
	}

    /**
     * getter for 1-成功，2-失败，3-处理中
     */
	public Integer getState() {
		return state;
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
     * setter for 转帐交易流水号-支付系统
     */
	public void setPaySerialNo(String paySerialNo) {
		this.paySerialNo = paySerialNo;
	}

    /**
     * getter for 转帐交易流水号-支付系统
     */
	public String getPaySerialNo() {
		return paySerialNo;
	}

    /**
     * setter for 备注
     */
	public void setNotes(String notes) {
		this.notes = notes;
	}

    /**
     * getter for 备注
     */
	public String getNotes() {
		return notes;
	}

    /**
     * setter for 操作人员
     */
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

    /**
     * getter for 操作人员
     */
	public Long getCreatorId() {
		return creatorId;
	}

    /**
     * setter for 操作员名称
     */
	public void setCreator(String creator) {
		this.creator = creator;
	}

    /**
     * getter for 操作员名称
     */
	public String getCreator() {
		return creator;
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
     * FundLogEntity.toString()
     */
    @Override
    public String toString() {
        return "FundLogEntity{" +
               "id='" + id + '\'' +
               ", serialNo='" + serialNo + '\'' +
               ", type='" + type + '\'' +
               ", toAccountId='" + toAccountId + '\'' +
               ", toName='" + toName + '\'' +
               ", toCardNo='" + toCardNo + '\'' +
               ", cycleNo='" + cycleNo + '\'' +
               ", balance='" + balance + '\'' +
               ", amount='" + amount + '\'' +
               ", originalAmount='" + originalAmount + '\'' +
               ", totalFee='" + totalFee + '\'' +
               ", channelType='" + channelType + '\'' +
               ", bankCardType='" + bankCardType + '\'' +
               ", firmId='" + firmId + '\'' +
               ", firmName='" + firmName + '\'' +
               ", state='" + state + '\'' +
               ", version='" + version + '\'' +
               ", paySerialNo='" + paySerialNo + '\'' +
               ", notes='" + notes + '\'' +
               ", creatorId='" + creatorId + '\'' +
               ", creator='" + creator + '\'' +
               ", modifyTime='" + modifyTime + '\'' +
               ", createTime='" + createTime + '\'' +
               '}';
    }

}

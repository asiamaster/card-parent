package com.dili.card.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 账户资金操作记录
 * @author bob
 */
public class FundLogDo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** 主键ID */
	private Long id; 
	/** 操作流水号 与操作记录关联字段 */
	private String serialNo; 
	/** 客户账户ID */
	private Long accountId; 
	/** 客户编号 */
	private String customerNo; 
	/** 客户名称 */
	private String customerName; 
	/** 园区卡号 */
	private String cardNo; 
	/** 初期余额 */
	private Long balance; 
	/** 交易金额 */
	private Long amount; 
	/** 资金动作-收入、支出 */
	private Integer action; 
	/** 交易类型-充值、体现、消费、转账 */
	private Integer tradeType; 
	/** 交易渠道-现金、POS等 */
	private Integer tradeChannel; 
	/** 交易流水号 */
	private String tradeNo; 
	/** 备注 */
	private String notes; 
	/** 状态 1-成功，2-失败，3-处理中 */
	private Integer state; 
	/** 操作时间 与操作记录中字段保持一致 */
	private LocalDateTime operateTime; 
	/** 商户ID */
	private Long firmId; 
	/**  */
	private LocalDateTime modifyTime; 
	/**  */
	private Integer version; 
    /**
     * FundLogEntity constructor
     */
	public FundLogDo() {
		super();
	}

    /**
     * setter for 主键ID
     */
	public void setId(Long id) {
		this.id = id;
	}

    /**
     * getter for 主键ID
     */
	public Long getId() {
		return id;
	}

    /**
     * setter for 操作流水号 与操作记录关联字段
     */
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

    /**
     * getter for 操作流水号 与操作记录关联字段
     */
	public String getSerialNo() {
		return serialNo;
	}

    /**
     * setter for 客户账户ID
     */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

    /**
     * getter for 客户账户ID
     */
	public Long getAccountId() {
		return accountId;
	}

    /**
     * setter for 客户编号
     */
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

    /**
     * getter for 客户编号
     */
	public String getCustomerNo() {
		return customerNo;
	}

    /**
     * setter for 客户名称
     */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

    /**
     * getter for 客户名称
     */
	public String getCustomerName() {
		return customerName;
	}

    /**
     * setter for 园区卡号
     */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

    /**
     * getter for 园区卡号
     */
	public String getCardNo() {
		return cardNo;
	}

    /**
     * setter for 初期余额
     */
	public void setBalance(Long balance) {
		this.balance = balance;
	}

    /**
     * getter for 初期余额
     */
	public Long getBalance() {
		return balance;
	}

    /**
     * setter for 交易金额
     */
	public void setAmount(Long amount) {
		this.amount = amount;
	}

    /**
     * getter for 交易金额
     */
	public Long getAmount() {
		return amount;
	}

    /**
     * setter for 资金动作-收入、支出
     */
	public void setAction(Integer action) {
		this.action = action;
	}

    /**
     * getter for 资金动作-收入、支出
     */
	public Integer getAction() {
		return action;
	}

    /**
     * setter for 交易类型-充值、体现、消费、转账
     */
	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

    /**
     * getter for 交易类型-充值、体现、消费、转账
     */
	public Integer getTradeType() {
		return tradeType;
	}

    /**
     * setter for 交易渠道-现金、POS等
     */
	public void setTradeChannel(Integer tradeChannel) {
		this.tradeChannel = tradeChannel;
	}

    /**
     * getter for 交易渠道-现金、POS等
     */
	public Integer getTradeChannel() {
		return tradeChannel;
	}

    /**
     * setter for 交易流水号
     */
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

    /**
     * getter for 交易流水号
     */
	public String getTradeNo() {
		return tradeNo;
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
     * setter for 状态 1-成功，2-失败，3-处理中
     */
	public void setState(Integer state) {
		this.state = state;
	}

    /**
     * getter for 状态 1-成功，2-失败，3-处理中
     */
	public Integer getState() {
		return state;
	}

    /**
     * setter for 操作时间 与操作记录中字段保持一致
     */
	public void setOperateTime(LocalDateTime operateTime) {
		this.operateTime = operateTime;
	}

    /**
     * getter for 操作时间 与操作记录中字段保持一致
     */
	public LocalDateTime getOperateTime() {
		return operateTime;
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
     * setter for 
     */
	public void setModifyTime(LocalDateTime modifyTime) {
		this.modifyTime = modifyTime;
	}

    /**
     * getter for 
     */
	public LocalDateTime getModifyTime() {
		return modifyTime;
	}

    /**
     * setter for 
     */
	public void setVersion(Integer version) {
		this.version = version;
	}

    /**
     * getter for 
     */
	public Integer getVersion() {
		return version;
	}

    /**
     * FundLogEntity.toString()
     */
    @Override
    public String toString() {
        return "FundLogEntity{" +
               "id='" + id + '\'' +
               ", serialNo='" + serialNo + '\'' +
               ", accountId='" + accountId + '\'' +
               ", customerNo='" + customerNo + '\'' +
               ", customerName='" + customerName + '\'' +
               ", cardNo='" + cardNo + '\'' +
               ", balance='" + balance + '\'' +
               ", amount='" + amount + '\'' +
               ", action='" + action + '\'' +
               ", tradeType='" + tradeType + '\'' +
               ", tradeChannel='" + tradeChannel + '\'' +
               ", tradeNo='" + tradeNo + '\'' +
               ", notes='" + notes + '\'' +
               ", state='" + state + '\'' +
               ", operateTime='" + operateTime + '\'' +
               ", firmId='" + firmId + '\'' +
               ", modifyTime='" + modifyTime + '\'' +
               ", version='" + version + '\'' +
               '}';
    }

}

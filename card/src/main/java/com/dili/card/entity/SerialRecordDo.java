package com.dili.card.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.card.type.ActionType;
import com.dili.card.type.OperateType;
import com.dili.card.type.TradeType;
import com.dili.card.util.CurrencyUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 业务端账户流水
 * 
 * @author bob
 */
public class SerialRecordDo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**  */
	private Long id;
	/** 流水号 */
	private String serialNo;
	/** 操作类型 */
	private Integer type;
	/** 账户ID */
	private Long accountId;
	/** 关联卡号 */
	private String cardNo;
	/** 客户ID */
	private Long customerId;
	/** 客户编号 */
	private String customerNo;
	/** 客户姓名 */
	private String customerName;
	/** 客户身份类型 */
	private String customerType;
	/** 持卡人 */
	private String holdName;
	/** 持卡人证件号 */
	private String holdCertificateNumber;
	/** 持卡人联系电话 */
	private String holdContactsPhone;
	/** 资金动作-收入,支出 */
	private Integer action;
	/** 期初余额-分 */
	private Long startBalance;
	/** 操作金额-分 */
	private Long amount;
	/** 期末余额-分 */
	private Long endBalance;
	/** 交易类型-充值、提现、消费、转账、其他 */
	private Integer tradeType;
	/** 交易渠道-现金、POS、网银 */
	private Integer tradeChannel;
	/** 交易流水号 */
	private String tradeNo;
	/** 资金项目 */
	private Integer fundItem;
	/** 资金项目名称 */
	private String fundItemName;
	/** 操作员ID */
	private Long operatorId;
	/** 操作员工号 */
	private String operatorNo;
	/** 操作员名称 */
	private String operatorName;
	/** 操作时间-与支付系统保持一致 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime operateTime;
	/** 备注 */
	private String notes;
	/** 商户ID */
	private Long firmId;

	public SerialRecordDo() {
		super();
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public void setAction(Integer action) {
		this.action = action;
	}

	public Integer getAction() {
		return action;
	}

	public void setStartBalance(Long startBalance) {
		this.startBalance = startBalance;
	}

	public Long getStartBalance() {
		return startBalance;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getAmount() {
		return amount;
	}

	public void setEndBalance(Long endBalance) {
		this.endBalance = endBalance;
	}

	public Long getEndBalance() {
		return endBalance;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeChannel(Integer tradeChannel) {
		this.tradeChannel = tradeChannel;
	}

	public Integer getTradeChannel() {
		return tradeChannel;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setFundItem(Integer fundItem) {
		this.fundItem = fundItem;
	}

	public Integer getFundItem() {
		return fundItem;
	}

	public void setFundItemName(String fundItemName) {
		this.fundItemName = fundItemName;
	}

	public String getFundItemName() {
		return fundItemName;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorNo(String operatorNo) {
		this.operatorNo = operatorNo;
	}

	public String getOperatorNo() {
		return operatorNo;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperateTime(LocalDateTime operateTime) {
		this.operateTime = operateTime;
	}

	public LocalDateTime getOperateTime() {
		return operateTime;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getNotes() {
		return notes;
	}

	public void setFirmId(Long firmId) {
		this.firmId = firmId;
	}

	public Long getFirmId() {
		return firmId;
	}

	public String getHoldName() {
		return holdName;
	}

	public void setHoldName(String holdName) {
		this.holdName = holdName;
	}

	@Override
	public String toString() {
		return "SerialRecordEntity{" + "id='" + id + '\'' + ", serialNo='" + serialNo + '\'' + ", accountId='"
				+ accountId + '\'' + ", cardNo='" + cardNo + '\'' + ", customerId='" + customerId + '\''
				+ ", customerNo='" + customerNo + '\'' + ", customerName='" + customerName + '\'' + ", action='"
				+ action + '\'' + ", startBalance='" + startBalance + '\'' + ", amount='" + amount + '\''
				+ ", endBalance='" + endBalance + '\'' + ", tradeType='" + tradeType + '\'' + ", tradeChannel='"
				+ tradeChannel + '\'' + ", tradeNo='" + tradeNo + '\'' + ", fundItem='" + fundItem + '\''
				+ ", fundItemName='" + fundItemName + '\'' + ", operatorId='" + operatorId + '\'' + ", operatorNo='"
				+ operatorNo + '\'' + ", operatorName='" + operatorName + '\'' + ", operateTime='" + operateTime + '\''
				+ ", notes='" + notes + '\'' + ", firmId='" + firmId + '\'' + '}';
	}

	/**
	 * 获取期初金额展示
	 * 
	 * @return
	 */
	public String getStartBalanceView() {
		return this.startBalance != null ? CurrencyUtils.toYuanWithStripTrailingZeros(this.startBalance) : null;
	}

	public String getTradeTypeView() {
		if (this.tradeType == null) {
			return "";
		}
		TradeType tradeType = TradeType.getByCode(this.tradeType);
		return tradeType == null ? "" : tradeType.getName();
	}

	public String getTypeView() {
		if (this.type == null) {
			return "";
		}
		String name = OperateType.getName(this.type);
		return name;
	}
	/**
	 * 获取发生金额展示
	 * 
	 * @return
	 */
	public String getAmountView() {
		String symbol = Integer.valueOf(ActionType.INCOME.getCode()).equals(this.action) ? "+" : "-";
		return this.amount != null ? symbol + CurrencyUtils.toYuanWithStripTrailingZeros(this.amount) : null;
	}

	/**
	 * 获取期末金额展示
	 * 
	 * @return
	 */
	public String getEndBalanceView() {
		return this.endBalance != null ? CurrencyUtils.toYuanWithStripTrailingZeros(this.endBalance) : null;
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

}

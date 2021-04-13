package com.dili.card.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.card.type.*;
import com.dili.card.util.CurrencyUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 柜台业务办理记录
 * @author bob
 */
public class BusinessRecordDo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**  */
	private Long id;
	/** 流水号 */
	private String serialNo;
	/** 账务周期号 */
	private Long cycleNo;
	/** 业务类型-办卡、充值、提现等 */
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
	/** 持卡人姓名 */
	private String holdName;
	/** 期初余额-分 */
	private Long startBalance;
	/** 操作金额-分 */
	private Long amount;
	/** 期末余额-分 */
	private Long endBalance;
	/** 总余额(包括冻结金额) */
	private Long totalBalance;
	/** 交易类型-充值、提现、消费、转账、其他 */
	private Integer tradeType;
	/** 交易渠道-现金、POS、网银 */
	private Integer tradeChannel;
	/** 银行卡类型-借记卡、信用卡 */
	private Integer bankCardType;
	/** 交易流水号 */
	private String tradeNo;
	/** 委托合同编号 */
	private String contractNo;
	/** 委托人ID */
	private Long consignorId;
	/** 新卡卡号 */
	private String newCardNo;
	/** 押金-分 */
	private Long deposit;
	/** 工本费-分 */
	private Long cardCost;
	/** 手续费-分 */
	private Long serviceCost;
	/** 附加内容-存储不太重要的内容，否则请扩充该表字段 */
	private String attach;
	/** 操作员ID */
	private Long operatorId;
	/** 操作员工号 */
	private String operatorNo;
	/** 操作员名称 */
	private String operatorName;
	/** 操作时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime operateTime;
	/** 备注 */
	private String notes;
	/** 办理状态-处理中、成功、失败 */
	private Integer state;
	/** 商户ID */
	private Long firmId;
	/** 商户名称 */
	private String firmName;
	/** 修改时间 */
	private LocalDateTime modifyTime;
	/** 数据版本号 */
	private Integer version;
	/**pos类型，从字典中获取*/
	private String posType;
	/** 客户身份类型  用于构建交易流水，业务办理记录不存储该字段*/
	@Transient
	private String customerType;
    /**
     * BusinessRecordEntity constructor
     */
	public BusinessRecordDo() {
		super();
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getHoldName() {
		return holdName;
	}

	public void setHoldName(String holdName) {
		this.holdName = holdName;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getSerialNo() {
		return serialNo;
	}
	public void setCycleNo(Long cycleNo) {
		this.cycleNo = cycleNo;
	}

	public Long getCycleNo() {
		return cycleNo;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getType() {
		return type;
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

	public Long getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(Long totalBalance) {
		this.totalBalance = totalBalance;
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

	public void setBankCardType(Integer bankCardType) {
		this.bankCardType = bankCardType;
	}

	public Integer getBankCardType() {
		return bankCardType;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setConsignorId(Long consignorId) {
		this.consignorId = consignorId;
	}

	public Long getConsignorId() {
		return consignorId;
	}

	public void setNewCardNo(String newCardNo) {
		this.newCardNo = newCardNo;
	}

	public String getNewCardNo() {
		return newCardNo;
	}

	public void setDeposit(Long deposit) {
		this.deposit = deposit;
	}

	public Long getDeposit() {
		return deposit;
	}

	public void setCardCost(Long cardCost) {
		this.cardCost = cardCost;
	}

	public Long getCardCost() {
		return cardCost;
	}

	public void setServiceCost(Long serviceCost) {
		this.serviceCost = serviceCost;
	}

	public Long getServiceCost() {
		return serviceCost;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getAttach() {
		return attach;
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
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getState() {
		return state;
	}
	public void setFirmId(Long firmId) {
		this.firmId = firmId;
	}
	public Long getFirmId() {
		return firmId;
	}
	public void setModifyTime(LocalDateTime modifyTime) {
		this.modifyTime = modifyTime;
	}
	public LocalDateTime getModifyTime() {
		return modifyTime;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getVersion() {
		return version;
	}
    @Override
    public String toString() {
        return "BusinessRecordEntity{" +
               "id='" + id + '\'' +
               ", serialNo='" + serialNo + '\'' +
               ", cycleNo='" + cycleNo + '\'' +
               ", type='" + type + '\'' +
               ", accountId='" + accountId + '\'' +
               ", cardNo='" + cardNo + '\'' +
               ", customerId='" + customerId + '\'' +
               ", customerNo='" + customerNo + '\'' +
               ", customerName='" + customerName + '\'' +
               ", startBalance='" + startBalance + '\'' +
               ", amount='" + amount + '\'' +
               ", endBalance='" + endBalance + '\'' +
               ", tradeType='" + tradeType + '\'' +
               ", tradeChannel='" + tradeChannel + '\'' +
               ", bankCardType='" + bankCardType + '\'' +
               ", tradeNo='" + tradeNo + '\'' +
               ", contractNo='" + contractNo + '\'' +
               ", consignorId='" + consignorId + '\'' +
               ", newCardNo='" + newCardNo + '\'' +
               ", deposit='" + deposit + '\'' +
               ", cardCost='" + cardCost + '\'' +
               ", serviceCost='" + serviceCost + '\'' +
               ", attach='" + attach + '\'' +
               ", operatorId='" + operatorId + '\'' +
               ", operatorNo='" + operatorNo + '\'' +
               ", operatorName='" + operatorName + '\'' +
               ", operateTime='" + operateTime + '\'' +
               ", notes='" + notes + '\'' +
               ", state='" + state + '\'' +
               ", firmId='" + firmId + '\'' +
               ", modifyTime='" + modifyTime + '\'' +
               ", version='" + version + '\'' +
               '}';
    }

	/**
	 * 获取期初金额展示
	 * @return
	 */
	public String getStartBalanceView() {
		if (this.startBalance == null || this.startBalance.equals(0L)) {
			return "--";
		}
		return CurrencyUtils.toYuanWithStripTrailingZeros(this.startBalance);
	}

	/**
	 * 获取发生金额展示
	 * @return
	 */
	public String getAmountView() {
		if (this.amount == null || this.amount.equals(0L)) {
			return "--";
		}
		return CurrencyUtils.toYuanWithStripTrailingZeros(this.amount);
	}

	/**
	 * 获取期末金额展示
	 * @return
	 */
	public String getEndBalanceView() {
		if (this.endBalance == null || this.endBalance.equals(0L)) {
			return "--";
		}
		return CurrencyUtils.toYuanWithStripTrailingZeros(this.endBalance);
	}

	public String getTypeName() {
		return this.type != null ? OperateType.getName(this.type) : "";
	}

	public String getTradeTypeName() {
		return this.tradeType != null ? TradeType.getNameByCode(this.tradeType) : "";
	}

	public String getTradeChannelName() {
		return this.tradeChannel != null ? TradeChannel.getNameByCode(this.tradeChannel) : "";
	}

	public String getBankCardTypeName() {
		return this.bankCardType != null ? BankCardType.getNameByCode(this.bankCardType) : "";
	}

	public String getStateName() {
		return this.state != null ? OperateState.getNameByCode(this.state) : "";
	}

	public String getPosType() {
		return posType;
	}

	public void setPosType(String posType) {
		this.posType = posType;
	}

	public String getFirmName() {
		return firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}
}

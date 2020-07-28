package com.dili.card.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.card.common.annotation.TextDisplay;
import com.dili.card.common.provider.FenToYuanProvider;
import com.dili.card.common.provider.OperationTypeProvider;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/17 15:35
 */
public class BusinessRecordResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;
	/**  */
    private Long id;
    /** 流水号 */
    private String serialNo;
    /** 账务周期号 */
    private Long cycleNo;
    /** 业务类型-办卡、充值、提现等 */
    @TextDisplay(OperationTypeProvider.class)
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
    /** 期初余额-分 */
    @TextDisplay(FenToYuanProvider.class)
    private Long startBalance;
    /** 操作金额-分 */
    @TextDisplay(FenToYuanProvider.class)
    private Long amount;
    /** 期末余额-分 */
    @TextDisplay(FenToYuanProvider.class)
    private Long endBalance;
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
    /** 修改时间 */
    private LocalDateTime modifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Long getCycleNo() {
        return cycleNo;
    }

    public void setCycleNo(Long cycleNo) {
        this.cycleNo = cycleNo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getStartBalance() {
        return startBalance;
    }

    public void setStartBalance(Long startBalance) {
        this.startBalance = startBalance;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getEndBalance() {
        return endBalance;
    }

    public void setEndBalance(Long endBalance) {
        this.endBalance = endBalance;
    }

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    public Integer getTradeChannel() {
        return tradeChannel;
    }

    public void setTradeChannel(Integer tradeChannel) {
        this.tradeChannel = tradeChannel;
    }

    public Integer getBankCardType() {
        return bankCardType;
    }

    public void setBankCardType(Integer bankCardType) {
        this.bankCardType = bankCardType;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public Long getConsignorId() {
        return consignorId;
    }

    public void setConsignorId(Long consignorId) {
        this.consignorId = consignorId;
    }

    public String getNewCardNo() {
        return newCardNo;
    }

    public void setNewCardNo(String newCardNo) {
        this.newCardNo = newCardNo;
    }

    public Long getDeposit() {
        return deposit;
    }

    public void setDeposit(Long deposit) {
        this.deposit = deposit;
    }

    public Long getCardCost() {
        return cardCost;
    }

    public void setCardCost(Long cardCost) {
        this.cardCost = cardCost;
    }

    public Long getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(Long serviceCost) {
        this.serviceCost = serviceCost;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorNo() {
        return operatorNo;
    }

    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public LocalDateTime getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(LocalDateTime operateTime) {
        this.operateTime = operateTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}

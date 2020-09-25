package com.dili.card.dto;

/**
 * 打印数据dto
 */
public class PrintDto {

    /** 票据名称 */
    private String name;
    /** 操作时间 */
    private String operateTime;
    /** 补打值 */
    private String reprint;
    /** 客户名称 */
    private String customerName;
    /** 客户卡号 */
    private String cardNo;
    /** 操作金额 */
    private String amount;
    /** 合计金额 */
    private String totalAmount;
    /** 合计金额大写*/
    private String totalAmountWords;
    /** 余额 */
    private String balance;
    /** 交易渠道 */
    private String tradeChannel;
    /** 押金 */
    private String deposit;
    /** 工本费 */
    private String cardCost;
    /** 手续费 */
    private String serviceCost;
    /** 操作员工号 */
    private String operatorNo;
    /** 操作员名称 */
    private String operatorName;
    /** 备注 */
    private String notes;
    /**新卡卡号(换卡时需要)*/
    private String newCardNo;
    /**凭证号(充值需要)*/
    private String posCertNum;
    /**银行卡类型(充值需要) {@link com.dili.card.type.BankCardType}*/
    private Integer bankType;
    /**pos类型(充值需要),从字典获取*/
    private Integer posType;

    public String getPosCertNum() {
        return posCertNum;
    }

    public void setPosCertNum(String posCertNum) {
        this.posCertNum = posCertNum;
    }

    public Integer getBankType() {
        return bankType;
    }

    public void setBankType(Integer bankType) {
        this.bankType = bankType;
    }

    public Integer getPosType() {
        return posType;
    }

    public void setPosType(Integer posType) {
        this.posType = posType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public String getReprint() {
        return reprint;
    }

    public void setReprint(String reprint) {
        this.reprint = reprint;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalAmountWords() {
        return totalAmountWords;
    }

    public void setTotalAmountWords(String totalAmountWords) {
        this.totalAmountWords = totalAmountWords;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTradeChannel() {
        return tradeChannel;
    }

    public void setTradeChannel(String tradeChannel) {
        this.tradeChannel = tradeChannel;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getCardCost() {
        return cardCost;
    }

    public void setCardCost(String cardCost) {
        this.cardCost = cardCost;
    }

    public String getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(String serviceCost) {
        this.serviceCost = serviceCost;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNewCardNo() {
        return newCardNo;
    }

    public void setNewCardNo(String newCardNo) {
        this.newCardNo = newCardNo;
    }
}

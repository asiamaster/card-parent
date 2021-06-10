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
	/** 客户卡号 */
	private String cardNoCipher;
	/** 操作金额 */
	private String amount;
	/** 合计金额 */
	private String totalAmount;
	/** 合计金额大写 */
	private String totalAmountWords;
	/** 余额 */
	private String balance;
	/**可用余额*/
	private String availableBalance;
	/**冻结金额*/
	private String frozenBalance;
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
	/** 新卡卡号(换卡时需要) */
	private String newCardNo;
	/** 新卡卡号 */
	private String newCardNoCipher;
	/** 凭证号(充值需要) */
	private String posCertNum;
	/** 银行卡类型(充值需要) {@link com.dili.card.type.BankCardType} */
	private Integer bankType;
	/** pos类型(充值需要),从字典获取 */
	private String posType;
	/** 流水号 */
	private String serialNo;
	/** 持卡人姓名 */
	private String holdName;
	/** 商户ID */
	private String firmName;
	/** 卡类型 */
	private String cardType;
/*-----------------------------权限设置所需字段---------------------*/
	/**客户身份号*/
	private String customerCertificateNumber;
	/**权限名称*/
	private String permissionNames;
	/**提现额度*/
	private String withdrawDailyAmount;
	/**提现次数*/
	private Integer withdrawDailyTimes;
	/**提现额度*/
	private String tradeDailyAmount;
	/**提现次数*/
	private Integer tradeDailyTimes;

	public String getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(String availableBalance) {
		this.availableBalance = availableBalance;
	}

	public String getFrozenBalance() {
		return frozenBalance;
	}

	public void setFrozenBalance(String frozenBalance) {
		this.frozenBalance = frozenBalance;
	}

	public String getPermissionNames() {
		return permissionNames;
	}

	public void setPermissionNames(String permissionNames) {
		this.permissionNames = permissionNames;
	}

	public String getWithdrawDailyAmount() {
		return withdrawDailyAmount;
	}

	public void setWithdrawDailyAmount(String withdrawDailyAmount) {
		this.withdrawDailyAmount = withdrawDailyAmount;
	}

	public Integer getWithdrawDailyTimes() {
		return withdrawDailyTimes;
	}

	public void setWithdrawDailyTimes(Integer withdrawDailyTimes) {
		this.withdrawDailyTimes = withdrawDailyTimes;
	}

	public String getTradeDailyAmount() {
		return tradeDailyAmount;
	}

	public void setTradeDailyAmount(String tradeDailyAmount) {
		this.tradeDailyAmount = tradeDailyAmount;
	}

	public Integer getTradeDailyTimes() {
		return tradeDailyTimes;
	}

	public void setTradeDailyTimes(Integer tradeDailyTimes) {
		this.tradeDailyTimes = tradeDailyTimes;
	}

	public String getCustomerCertificateNumber() {
		return customerCertificateNumber;
	}

	public void setCustomerCertificateNumber(String customerCertificateNumber) {
		this.customerCertificateNumber = customerCertificateNumber;
	}

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

	public String getPosType() {
		return posType;
	}

	public void setPosType(String posType) {
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

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getHoldName() {
		return holdName;
	}

	public void setHoldName(String holdName) {
		this.holdName = holdName;
	}

	public String getFirmName() {
		return firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardNoCipher() {
		return cardNoCipher;
	}

	public void setCardNoCipher(String cardNoCipher) {
		this.cardNoCipher = cardNoCipher;
	}

	public String getNewCardNoCipher() {
		return newCardNoCipher;
	}

	public void setNewCardNoCipher(String newCardNoCipher) {
		this.newCardNoCipher = newCardNoCipher;
	}

}

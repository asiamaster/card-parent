package com.dili.card.dto.pay;

/**
 * @description： 
 *          支付接口返回数据的通用字段
 * @author ：WangBo
 * @time ：2020年7月22日上午10:13:14
 */
public class PayResponseBaseDto {

	/** 资金账号ID */
	private Long accountId;
	/** 期初余额-包含冻结金额 */
	private Long balance;
	/** 操作金额 */
	private Long amount;
	/** 期初冻结余额 */
	private Long frozenBalance;
	/** 解冻金额 */
	private Long frozenAmount;
	/** 发生时间 */
	private Long when;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getBalance() {
		return balance;
	}

	public void setBalance(Long balance) {
		this.balance = balance;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getFrozenBalance() {
		return frozenBalance;
	}

	public void setFrozenBalance(Long frozenBalance) {
		this.frozenBalance = frozenBalance;
	}

	public Long getFrozenAmount() {
		return frozenAmount;
	}

	public void setFrozenAmount(Long frozenAmount) {
		this.frozenAmount = frozenAmount;
	}

	public Long getWhen() {
		return when;
	}

	public void setWhen(Long when) {
		this.when = when;
	}

}

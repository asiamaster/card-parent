package com.dili.card.dto.pay;

import java.util.List;

/**
 * @description： 客户总资产信息
 * 
 * @author ：WangBo
 * @time ：2020年12月24日上午11:12:48
 */
public class CustomerBalanceResponseDto {
	/** 账户ID */
	private Long customerId;
	/** 余额 */
	private Long balance;
	/** 冻结余额 */
	private Long frozenAmount;
	/** 可用余额 */
	private Long availableAmount;
	/** 交易冻结金额 */
	private Long tradeFrozen;
	/** 人工冻结金额 */
	private Long manFrozen;
	/** 资产明细 */
	private List<BalanceResponseDto> fundAccounts;

	public Long getTradeFrozen() {
		return tradeFrozen;
	}

	public void setTradeFrozen(Long tradeFrozen) {
		this.tradeFrozen = tradeFrozen;
	}

	public Long getManFrozen() {
		return manFrozen;
	}

	public void setManFrozen(Long manFrozen) {
		this.manFrozen = manFrozen;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	/**
	 *
	 * @return
	 */
	public Long getBalance() {
		return balance;
	}

	/**
	 *
	 * @param balance
	 */
	public void setBalance(Long balance) {
		this.balance = balance;
	}

	/**
	 *
	 * @return
	 */
	public Long getFrozenAmount() {
		return frozenAmount;
	}

	/**
	 *
	 * @param frozenAmount
	 */
	public void setFrozenAmount(Long frozenAmount) {
		this.frozenAmount = frozenAmount;
	}

	/**
	 *
	 * @return
	 */
	public Long getAvailableAmount() {
		return availableAmount;
	}

	/**
	 *
	 * @param availableAmount
	 */
	public void setAvailableAmount(Long availableAmount) {
		this.availableAmount = availableAmount;
	}

	public List<BalanceResponseDto> getFundAccounts() {
		return fundAccounts;
	}

	public void setFundAccounts(List<BalanceResponseDto> fundAccounts) {
		this.fundAccounts = fundAccounts;
	}

}

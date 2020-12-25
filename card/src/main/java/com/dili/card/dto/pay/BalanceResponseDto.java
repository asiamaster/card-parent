package com.dili.card.dto.pay;

/**
 * 余额查询返回dto
 * @author xuliang
 */
/**
 * @description： 客户总资金明细资金情况
 * 
 * @author ：WangBo
 * @time ：2020年12月24日上午11:29:09
 */
public class BalanceResponseDto {
	/** 账户ID */
	private Long accountId;
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
	/** 卡号 */
	private String cardNo;
	/** 实体卡是否存在 */
	private Integer cardExist;

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

	/**
	 *
	 * @return
	 */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 *
	 * @param accountId
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
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

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Integer getCardExist() {
		return cardExist;
	}

	public void setCardExist(Integer cardExist) {
		this.cardExist = cardExist;
	}

}

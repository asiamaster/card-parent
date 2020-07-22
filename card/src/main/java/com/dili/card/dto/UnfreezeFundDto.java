package com.dili.card.dto;

/**
 * @description： 解冻资金实体类
 * 
 * @author ：WangBo
 * @time ：2020年7月21日下午2:22:04
 */
public class UnfreezeFundDto extends BaseDto {
	/** */
	private static final long serialVersionUID = 1L;
	/** 交易渠道 */
	private Long[] tradeNos;
	/** */
	private Long accountId;
	/** 冻结资金id,与支付对应 */
	private Long frozenId;
	/** 备注 */
	private String mark;

	public Long[] getTradeNos() {
		return tradeNos;
	}

	public void setTradeNos(Long[] tradeNos) {
		this.tradeNos = tradeNos;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getFrozenId() {
		return frozenId;
	}

	public void setFrozenId(Long frozenId) {
		this.frozenId = frozenId;
	}

}

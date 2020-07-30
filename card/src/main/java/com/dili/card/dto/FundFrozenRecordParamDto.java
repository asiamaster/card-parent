package com.dili.card.dto;

/**
 * @description： 未解冻资金列表
 * 
 * @author ：WangBo
 * @time ：2020年7月22日下午3:37:38
 */
public class FundFrozenRecordParamDto {

	/** 账户ID */
	private Long accountId;
	/** 页码，从1开始 */
	private Integer page;
	/** 每页行数 */
	private Integer rows;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

}

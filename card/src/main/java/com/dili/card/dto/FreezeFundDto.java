package com.dili.card.dto;

import java.math.BigDecimal;

/**
 * @description：
 * 			解冻资金操作
 * @author ：WangBo
 * @time ：2020年6月29日上午10:15:19
 */
public class FreezeFundDto {
	private Long accountId;
	private Long fundAccountId;
	private BigDecimal extraFee;
	private String serialNo;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getFundAccountId() {
		return fundAccountId;
	}

	public void setFundAccountId(Long fundAccountId) {
		this.fundAccountId = fundAccountId;
	}

	public BigDecimal getExtraFee() {
		return extraFee;
	}

	public void setExtraFee(BigDecimal extraFee) {
		this.extraFee = extraFee;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

}

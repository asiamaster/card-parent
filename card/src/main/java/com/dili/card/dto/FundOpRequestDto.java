package com.dili.card.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 解、冻资金操作
 * @author ：WangBo
 * @time ：2020年6月29日上午10:15:19
 */
public class FundOpRequestDto implements Serializable {
	/**卡账户id*/
	private Long accountId;
	private Long fundAccountId;
	private BigDecimal extraFee;
	private String serialNo;
	/**解冻、冻结的资金*/
	private Long fee;
	/**备注*/
	private String mark;

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

	public Long getFee() {
		return fee;
	}

	public void setFee(Long fee) {
		this.fee = fee;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
}

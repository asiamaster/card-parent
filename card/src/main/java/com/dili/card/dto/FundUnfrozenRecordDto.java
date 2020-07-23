package com.dili.card.dto;

import java.time.LocalDateTime;

/**
 * @description： 未解冻资金列表
 * 
 * @author ：WangBo
 * @time ：2020年7月22日下午3:37:38
 */
public class FundUnfrozenRecordDto {

	/** 冻结金额 */
	private Long amount;
	/** 冻结备注 */
	private String notes;
	/** 冻结时间 */
	private LocalDateTime operateTime;
	/** 冻结员工号 */
	private String operatorNo;
	/** 冻结员 */
	private String operatorName;

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public LocalDateTime getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(LocalDateTime operateTime) {
		this.operateTime = operateTime;
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

}

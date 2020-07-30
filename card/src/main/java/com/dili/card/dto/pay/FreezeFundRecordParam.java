package com.dili.card.dto.pay;

/**
 * @description： 支付资金人工冻结解冻记录查询参数 
 * 
 * @author ：WangBo
 * @time ：2020年7月30日上午11:35:02
 */
public class FreezeFundRecordParam {
	/** 资金账号 */
	private Long accountId;
	/** 冻结状态：1-冻结，2-解冻 */
	private Integer state;
	/** 冻结开始时间 */
	private String startTime;
	/** 冻结结束时间 */
	private String endTime;
	/** 页号 */
	private Integer pageNum;
	/** 每页记录数 */
	private Integer pageSize;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}

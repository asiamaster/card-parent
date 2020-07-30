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
	/** 冻结状态：1-冻结，2-解冻 {@link} PayFreezeFundType} */
	private Integer state;
	/** 冻结开始时间 */
	private String startTime;
	/** 冻结结束时间 */
	private String endTime;
	/** 页号 */
	private Integer pageNo;
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

	/**
	 * 冻结状态：1-冻结，2-解冻{@link} PayFreezeFundType
	 * @param state
	 */
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

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}

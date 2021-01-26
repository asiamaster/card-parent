package com.dili.card.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于操作记录、账户流水查询
 * 
 * @author xuliang
 */
public class SerialQueryDto extends SerialDto {
	/** */
	private static final long serialVersionUID = 242732303288305257L;
	/** 资金项目 */
	private Integer fundItem;
	/** 账户ID */
	private Long accountId;
	/** 客户ID */
	private Long customerId;
	/** 卡号 */
	private String cardNo;
	/** 卡号列表 */
	private List<String> cardNos;
	/** 操作员ID */
	private Long operatorId;
	/** 操作开始时间 */
	private String operateTimeStart;
	/** 操作结束时间 */
	private String operateTimeEnd;
	/** 商户ID */
	private Long firmId;
	/** 账务周期号 */
	private Long cycleNo;
	/** 限制条数 */
	private Integer limit;
	/** 操作状态 */
	private Integer state;
	/** 操作类型 */
	private Integer type;
	/** 交易渠道 {@link com.dili.card.type.TradeChannel} */
	private Integer tradeChannel;
	/** 操作类型列表 */
	private List<Integer> operateTypeList;
	/** 账户id列表 */
	private List<Long> accountIdList;
	/** 用于流水记录排序 */
	private String serialSort;
	/** 用于排序转换标记 */
	private Boolean sortConvert;
	/** 是否包含副卡 1是 */
	private Integer includeSlave;

	public Integer getTradeChannel() {
		return tradeChannel;
	}

	public void setTradeChannel(Integer tradeChannel) {
		this.tradeChannel = tradeChannel;
	}

	public List<String> getCardNos() {
		return cardNos;
	}

	public void setCardNos(List<String> cardNos) {
		this.cardNos = cardNos;
	}

	public Integer getIncludeSlave() {
		return includeSlave;
	}

	public void setIncludeSlave(Integer includeSlave) {
		this.includeSlave = includeSlave;
	}

	public Integer getFundItem() {
		return fundItem;
	}

	public void setFundItem(Integer fundItem) {
		this.fundItem = fundItem;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperateTimeStart() {
		return operateTimeStart;
	}

	public void setOperateTimeStart(String operateTimeStart) {
		this.operateTimeStart = operateTimeStart;
	}

	public String getOperateTimeEnd() {
		return operateTimeEnd;
	}

	public void setOperateTimeEnd(String operateTimeEnd) {
		this.operateTimeEnd = operateTimeEnd;
	}

	public Long getFirmId() {
		return firmId;
	}

	public void setFirmId(Long firmId) {
		this.firmId = firmId;
	}

	public Long getCycleNo() {
		return cycleNo;
	}

	public void setCycleNo(Long cycleNo) {
		this.cycleNo = cycleNo;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List<Integer> getOperateTypeList() {
		return operateTypeList;
	}

	public void setOperateTypeList(List<Integer> operateTypeList) {
		this.operateTypeList = operateTypeList;
	}

	public List<Long> getAccountIdList() {
		return accountIdList;
	}

	public void setAccountIdList(List<Long> accountIdList) {
		this.accountIdList = accountIdList;
	}

	public String getSerialSort() {
		return serialSort;
	}

	public void setSerialSort(String serialSort) {
		this.serialSort = serialSort;
	}

	public Boolean getSortConvert() {
		return sortConvert;
	}

	public void setSortConvert(Boolean sortConvert) {
		this.sortConvert = sortConvert;
	}

}

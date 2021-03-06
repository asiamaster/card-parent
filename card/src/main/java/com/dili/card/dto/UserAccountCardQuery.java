package com.dili.card.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @description： 用户信息查询参数
 *
 * @author ：WangBo
 * @time ：2020年4月26日下午4:30:03
 */
public class UserAccountCardQuery extends BaseDto {
	private static final long serialVersionUID = 1L;
	/** 客户id */
	private List<Long> customerIds;
	/** 客户名字 */
	private String customerName;
	/** 多个账户ID */
	private List<Long> accountIds;
	/** 多个卡号 */
	private List<String> cardNos;
	/** 主账户ID */
	private Long parentAccountId;
	/** 持卡人 */
	private String holdName;
	/** 市场ID */
	private Long firmId;
	/** 结束时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startDate;
	/** 开始时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endDate;
	/** 卡类别 {@link com.dili.card.type.CardType} */
	private Integer cardType;
	/** 卡状态 {@link com.dili.card.type.CardStatus} */
	private Integer cardState;
	/**账户状态 {@link com.dili.card.type.DisableState}*/
	private Integer disableState;
	/**是否要排除异常状态的账户 ex：卡退还、账户被禁用*/
	private Integer excludeUnusualState;

	public Integer getDisableState() {
		return disableState;
	}

	public void setDisableState(Integer disableState) {
		this.disableState = disableState;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public List<Long> getCustomerIds() {
		return customerIds;
	}

	public void setCustomerIds(List<Long> customerIds) {
		this.customerIds = customerIds;
	}

	public List<Long> getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(List<Long> accountIds) {
		this.accountIds = accountIds;
	}

	public Long getParentAccountId() {
		return parentAccountId;
	}

	public void setParentAccountId(Long parentAccountId) {
		this.parentAccountId = parentAccountId;
	}

	public List<String> getCardNos() {
		return cardNos;
	}

	public void setCardNos(List<String> cardNos) {
		this.cardNos = cardNos;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	public Integer getCardState() {
		return cardState;
	}

	public void setCardState(Integer cardState) {
		this.cardState = cardState;
	}

	public Integer getExcludeUnusualState() {
		return excludeUnusualState;
	}

	public String getHoldName() {
		return holdName;
	}

	public void setHoldName(String holdName) {
		this.holdName = holdName;
	}

	public void setExcludeUnusualState(Integer excludeUnusualState) {
		this.excludeUnusualState = excludeUnusualState;
	}

	public void addAccountId(Long accountId) {
		if (this.accountIds == null) {
			accountIds = new ArrayList<Long>();
		}
		accountIds.add(accountId);
	}

	public static UserAccountCardQuery createInstance(Long accountId) {
		UserAccountCardQuery userAccountCardQuery = new UserAccountCardQuery();
		userAccountCardQuery.addAccountId(accountId);
		return userAccountCardQuery;
	}

	public Long getFirmId() {
		return firmId;
	}

	public void setFirmId(Long firmId) {
		this.firmId = firmId;
	}

}

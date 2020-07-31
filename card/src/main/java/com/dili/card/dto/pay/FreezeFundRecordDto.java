package com.dili.card.dto.pay;

import com.dili.card.common.annotation.TextDisplay;
import com.dili.card.common.provider.FenToYuanProvider;

/**
 * @description： 支付资金人工冻结解冻记录
 *
 * @author ：WangBo
 * @time ：2020年7月30日上午11:35:02
 */
public class FreezeFundRecordDto {
	/** 冻结ID */
	private Long frozenId;
	/** 资金账号 */
	private Long accountId;
	/** 冻结金额 */
	@TextDisplay(FenToYuanProvider.class)
	private Long amount;
	/** 冻结状态：1-冻结，2-解冻 */
	private Integer state;
	/** 扩展信息 */
	private String extension;
	/** 冻结时间 */
	private String freezeTime;
	/** 解冻时间 */
	private Long unfreezeTime;
	/** 备注 */
	private String description;
	/** 操作员名字 */
	private String opName;
	/** 操作员工号 */
	private String opNo;

	public Long getFrozenId() {
		return frozenId;
	}

	public void setFrozenId(Long frozenId) {
		this.frozenId = frozenId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getFreezeTime() {
		return freezeTime;
	}

	public void setFreezeTime(String freezeTime) {
		this.freezeTime = freezeTime;
	}

	public Long getUnfreezeTime() {
		return unfreezeTime;
	}

	public void setUnfreezeTime(Long unfreezeTime) {
		this.unfreezeTime = unfreezeTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public String getOpNo() {
		return opNo;
	}

	public void setOpNo(String opNo) {
		this.opNo = opNo;
	}


}

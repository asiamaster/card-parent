package com.dili.card.dto;

/**
 * @description： 解冻资金实体类
 *
 * @author ：WangBo
 * @time ：2020年7月21日下午2:22:04
 */
public class UnfreezeFundDto extends CardRequestDto {
	/** */
	private static final long serialVersionUID = 1L;
	/** 交易渠道 */
	private Long[] frozenIds;
	/** 冻结资金id,与支付对应 */
	private Long frozenId;
	/** 备注 */
	private String remark;

	public Long[] getFrozenIds() {
		return frozenIds;
	}

	public void setFrozenIds(Long[] frozenIds) {
		this.frozenIds = frozenIds;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getFrozenId() {
		return frozenId;
	}

	public void setFrozenId(Long frozenId) {
		this.frozenId = frozenId;
	}

}

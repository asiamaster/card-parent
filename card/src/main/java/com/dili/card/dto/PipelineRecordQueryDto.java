package com.dili.card.dto;

/**
 *
 */
public class PipelineRecordQueryDto extends CardRequestDto{
	/** 交易类型*/
	private Integer type;
	/** 圈提选择的市场ID */
	private Long firmAccount;

	public Long getFirmAccount() {
		return firmAccount;
	}

	public void setFirmAccount(Long firmAccount) {
		this.firmAccount = firmAccount;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}

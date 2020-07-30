package com.dili.card.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description： 卡片批量激活数据
 *
 * @author ：WangBo
 * @time ：2020年4月28日下午4:14:56
 */
public class BatchActivateCardDto {
	/** 卡片在仓库中的状态 */
	private Integer state;
	/** 修改时间 */
	private LocalDateTime modifyTime;
	/** 激活的卡片列表 */
	private List<String> cardNos;

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public LocalDateTime getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(LocalDateTime modifyTime) {
		this.modifyTime = modifyTime;
	}

	public List<String> getCardNos() {
		return cardNos;
	}

	public void setCardNos(List<String> cardNos) {
		this.cardNos = cardNos;
	}

}

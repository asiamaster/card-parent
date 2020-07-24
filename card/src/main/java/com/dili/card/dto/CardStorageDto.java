package com.dili.card.dto;

import java.time.LocalDateTime;

import com.dili.card.common.annotation.TextDisplay;
import com.dili.card.common.provider.CardStorageStateProvider;
import com.dili.card.common.provider.CardTypeProvider;

/**
 * @description： 卡片入库条件查询
 * 
 * @author ：WangBo
 * @time ：2020年7月17日上午10:07:34
 */
public class CardStorageDto extends BaseDto {
	/** 卡号 */
	private String cardNo;
	/** 卡类型 */
	@TextDisplay(value = CardTypeProvider.class)
	private Integer type;
	/** 卡片在仓库状态 */
	@TextDisplay(value = CardStorageStateProvider.class)
	private Integer state;
	/** 操作员ID */
	private Long creatorId;
	/** 操作员名称 */
	private String creator;
	/** 备注 */
	private String notes;
	/** 商户ID */
	private Long firmId;
	/** 创建时间 */
	private LocalDateTime createTime;
	/** 修改时间 */
	private LocalDateTime modifyTime;
	/** 结束时间 */
	private LocalDateTime startDate;
	/** 开始时间 */
	private LocalDateTime endDate;

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Long getFirmId() {
		return firmId;
	}

	public void setFirmId(Long firmId) {
		this.firmId = firmId;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(LocalDateTime modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

}
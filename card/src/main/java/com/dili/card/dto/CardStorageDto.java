package com.dili.card.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.card.common.annotation.TextDisplay;
import com.dili.card.common.provider.CardFaceProvider;
import com.dili.card.common.provider.CardStorageStateProvider;
import com.dili.card.common.provider.CardTypeProvider;
import com.dili.card.type.CardFaceType;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @description： 卡片入库条件查询
 *
 * @author ：WangBo
 * @time ：2020年7月17日上午10:07:34
 */
public class CardStorageDto extends BaseDto {
	/** */
	private static final long serialVersionUID = 1L;
	/** 卡号 */
	private String cardNo;
	/** 卡类型 */
	@TextDisplay(value = CardTypeProvider.class)
	private Integer type;
	/** 卡面 */
	@TextDisplay(value = CardFaceProvider.class)
	private String cardFace;
	/** 卡片在仓库状态 */
	@TextDisplay(value = CardStorageStateProvider.class)
	private Integer state;
	/** 操作员ID */
	private Long creatorId;
	/** 操作员名称 */
	private String creator;
	/** 入库记录ID */
	private Long storageInId;
	/** 备注 */
	private String notes;
	/** 商户ID */
	private Long firmId;
	/** 创建时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;
	/** 修改时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime modifyTime;
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startDate;
	/** 开始时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
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

	public String getCardFace() {
		return cardFace;
	}

	public void setCardFace(String cardFace) {
		this.cardFace = cardFace;
	}

	public Long getStorageInId() {
		return storageInId;
	}

	public void setStorageInId(Long storageInId) {
		this.storageInId = storageInId;
	}

}

package com.dili.card.dto;

/**
 * @description： 卡片号段入库数据封装
 * 
 * @author ：WangBo
 * @time ：2020年4月28日下午4:14:56
 */
public class BatchCardAddStorageDto {
	/** 商户ID */
	private Long firmId;
	/** 商户名称 */
	private String firmName;
	/** 备注 */
	private String notes;
	/** 入库人 */
	private String creator;
	/** 操作员 */
	private Long creatorId;
	/** 卡类型 */
	private Integer cardType;
	/** 卡面（与客户类型值相同,司机园内买园外买卖） */
	private String cardFace;
	/** 入库记录ID */
	private Long storageInId;
	/** 卡片起始号 */
	private String startCardNo;
	/** 卡片结束号 */
	private String endCardNo;

	public Long getFirmId() {
		return firmId;
	}

	public void setFirmId(Long firmId) {
		this.firmId = firmId;
	}

	public String getFirmName() {
		return firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	public String getStartCardNo() {
		return startCardNo;
	}

	public void setStartCardNo(String startCardNo) {
		this.startCardNo = startCardNo;
	}

	public String getEndCardNo() {
		return endCardNo;
	}

	public void setEndCardNo(String endCardNo) {
		this.endCardNo = endCardNo;
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

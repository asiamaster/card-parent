package com.dili.card.dto;

/**
 * @description： 操作人字段
 * 
 * @author ：WangBo
 * @time ：2020年7月3日下午3:18:56
 */
public class BaseSerialLogDto {
	/** 操作人员 */
	private Long creatorId;
	/** 操作人员姓名 */
	private String creator;
	/** 操作人员编号 */
	private String creatorCode;
	/** 商户ID */
	private Long firmId;
	/** 商户名称 */
	private String firmName;
	/** 操作类型 */
	private Integer type;

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorCode() {
		return creatorCode;
	}

	public void setCreatorCode(String creatorCode) {
		this.creatorCode = creatorCode;
	}

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}

package com.dili.card.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.card.common.annotation.TextDisplay;
import com.dili.card.common.provider.CardTypeProvider;
import com.dili.card.type.CardType;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 卡片采购入库记录，从厂家到市场。
 * 
 * @author bob
 */
public class StorageInDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键ID */
	private Long id;
	/** 起始卡号 */
	private String startCardNo;
	/** 结束卡号 */
	private String endCardNo;
	/** 数量 */
	private Integer amount;
	/** 卡类型 */
	@TextDisplay(value = CardTypeProvider.class)
	private Integer cardType;
	/** 操作人员ID */
	private Long creatorId;
	/** 操作人员名称-保留字段 */
	private String creator;
	/** 创建时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;
	/** 商户编码 */
	private Long firmId;
	/** 商户名称 */
	private String firmName;
	/** 修改时间 */
	private LocalDateTime modifyTime;
	/** 备注 */
	private String notes;
	/** 卡号范围 */
	@Transient
	private String cardNoRange;

	public String getCardNoRange() {
		return String.join("-", startCardNo, endCardNo);
	}

	public String getCardTypeText() {
		return CardType.getName(cardType);
	}

	/**
	 * StorageInEntity constructor
	 */
	public StorageInDto() {
		super();
	}

	/**
	 * setter for 主键ID
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * getter for 主键ID
	 */
	public Long getId() {
		return id;
	}

	/**
	 * setter for 起始卡号
	 */
	public void setStartCardNo(String startCardNo) {
		this.startCardNo = startCardNo;
	}

	/**
	 * getter for 起始卡号
	 */
	public String getStartCardNo() {
		return startCardNo;
	}

	/**
	 * setter for 结束卡号
	 */
	public void setEndCardNo(String endCardNo) {
		this.endCardNo = endCardNo;
	}

	/**
	 * getter for 结束卡号
	 */
	public String getEndCardNo() {
		return endCardNo;
	}

	/**
	 * setter for 数量
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	/**
	 * getter for 数量
	 */
	public Integer getAmount() {
		return amount;
	}

	/**
	 * setter for 卡类型
	 */
	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	/**
	 * getter for 卡类型
	 */
	public Integer getCardType() {
		return cardType;
	}

	/**
	 * setter for 操作人员ID
	 */
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	/**
	 * getter for 操作人员ID
	 */
	public Long getCreatorId() {
		return creatorId;
	}

	/**
	 * setter for 操作人员名称-保留字段
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * getter for 操作人员名称-保留字段
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * setter for 创建时间
	 */
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	/**
	 * getter for 创建时间
	 */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * setter for 商户编码
	 */
	public void setFirmId(Long firmId) {
		this.firmId = firmId;
	}

	/**
	 * getter for 商户编码
	 */
	public Long getFirmId() {
		return firmId;
	}

	/**
	 * setter for 商户名称
	 */
	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	/**
	 * getter for 商户名称
	 */
	public String getFirmName() {
		return firmName;
	}

	/**
	 * setter for 修改时间
	 */
	public void setModifyTime(LocalDateTime modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * getter for 修改时间
	 */
	public LocalDateTime getModifyTime() {
		return modifyTime;
	}

	/**
	 * setter for 备注
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * getter for 备注
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * StorageInEntity.toString()
	 */
	@Override
	public String toString() {
		return "StorageInEntity{" + "id='" + id + '\'' + ", startCardNo='" + startCardNo + '\'' + ", endCardNo='"
				+ endCardNo + '\'' + ", amount='" + amount + '\'' + ", cardType='" + cardType + '\'' + ", creatorId='"
				+ creatorId + '\'' + ", creator='" + creator + '\'' + ", createTime='" + createTime + '\''
				+ ", firmId='" + firmId + '\'' + ", firmName='" + firmName + '\'' + ", modifyTime='" + modifyTime + '\''
				+ ", notes='" + notes + '\'' + '}';
	}

}

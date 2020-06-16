package com.dili.card.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 柜员申领卡片记录
 * @author bob
 */
public class CardApplyRecordEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Long id; 
	/** 领取时间 */
	private LocalDateTime applyTime; 
	/** 卡号详情,多条以逗号分割 */
	private String cardNo; 
	/** 数量 */
	private Integer amount; 
	/** 领取人工号 */
	private Long applyUserId; 
	/** 领取人 */
	private String applyUserName; 
	/** 操作人员ID */
	private Long creatorId; 
	/** 操作人员名称-保留字段 */
	private String creator; 
	/** 创建时间 */
	private LocalDateTime createTime; 
	/** 商户编码 */
	private Long firmId; 
	/** 商户名称 */
	private String firmName; 
	/** 修改时间 */
	private LocalDateTime modifyTime; 
	/** 备注 */
	private String notes; 
    /**
     * CardApplyRecordEntity constructor
     */
	public CardApplyRecordEntity() {
		super();
	}

    /**
     * setter for 
     */
	public void setId(Long id) {
		this.id = id;
	}

    /**
     * getter for 
     */
	public Long getId() {
		return id;
	}

    /**
     * setter for 领取时间
     */
	public void setApplyTime(LocalDateTime applyTime) {
		this.applyTime = applyTime;
	}

    /**
     * getter for 领取时间
     */
	public LocalDateTime getApplyTime() {
		return applyTime;
	}

    /**
     * setter for 卡号详情,多条以逗号分割
     */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

    /**
     * getter for 卡号详情,多条以逗号分割
     */
	public String getCardNo() {
		return cardNo;
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
     * setter for 领取人工号
     */
	public void setApplyUserId(Long applyUserId) {
		this.applyUserId = applyUserId;
	}

    /**
     * getter for 领取人工号
     */
	public Long getApplyUserId() {
		return applyUserId;
	}

    /**
     * setter for 领取人
     */
	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}

    /**
     * getter for 领取人
     */
	public String getApplyUserName() {
		return applyUserName;
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
     * CardApplyRecordEntity.toString()
     */
    @Override
    public String toString() {
        return "CardApplyRecordEntity{" +
               "id='" + id + '\'' +
               ", applyTime='" + applyTime + '\'' +
               ", cardNo='" + cardNo + '\'' +
               ", amount='" + amount + '\'' +
               ", applyUserId='" + applyUserId + '\'' +
               ", applyUserName='" + applyUserName + '\'' +
               ", creatorId='" + creatorId + '\'' +
               ", creator='" + creator + '\'' +
               ", createTime='" + createTime + '\'' +
               ", firmId='" + firmId + '\'' +
               ", firmName='" + firmName + '\'' +
               ", modifyTime='" + modifyTime + '\'' +
               ", notes='" + notes + '\'' +
               '}';
    }

}

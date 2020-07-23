package com.dili.card.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 柜员申领卡片记录
 * @author bob
 */
public class CardStorageOut implements Serializable {

	private static final long serialVersionUID = 1L;

	/**  */
	private Long id;
	/** 领取时间 */
	private LocalDateTime applyTime;
	/** 卡号详情,多条以逗号分割 */
	private String cardNo;
	/** 数量 */
	private Integer amount;
	/** 领取人id*/
	private Long applyUserId;
	/**领取人工号*/
	private String applyUserCode;
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

	public CardStorageOut() {
		super();
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setApplyTime(LocalDateTime applyTime) {
		this.applyTime = applyTime;
	}

	public LocalDateTime getApplyTime() {
		return applyTime;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setApplyUserId(Long applyUserId) {
		this.applyUserId = applyUserId;
	}

	public Long getApplyUserId() {
		return applyUserId;
	}

	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}

	public String getApplyUserName() {
		return applyUserName;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setFirmId(Long firmId) {
		this.firmId = firmId;
	}

	public Long getFirmId() {
		return firmId;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public String getFirmName() {
		return firmName;
	}

	public void setModifyTime(LocalDateTime modifyTime) {
		this.modifyTime = modifyTime;
	}

	public LocalDateTime getModifyTime() {
		return modifyTime;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getNotes() {
		return notes;
	}

	public String getApplyUserCode() {
		return applyUserCode;
	}

	public void setApplyUserCode(String applyUserCode) {
		this.applyUserCode = applyUserCode;
	}

	/**
     * ApplyRecordEntity.toString()
     */
    @Override
    public String toString() {
        return "ApplyRecordEntity{" +
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

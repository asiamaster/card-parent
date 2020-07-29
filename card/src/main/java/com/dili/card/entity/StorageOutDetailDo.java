package com.dili.card.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 卡片出库，柜员领取激活的详细卡号
 * @author bob
 */
public class StorageOutDetailDo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Long id; 
	/** 卡号 */
	private String cardNo; 
	/** 出库记录ID */
	private Long storageOutId; 
    /**
     * StorageOutDetailEntity constructor
     */
	public StorageOutDetailDo() {
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
     * setter for 卡号
     */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

    /**
     * getter for 卡号
     */
	public String getCardNo() {
		return cardNo;
	}

    /**
     * setter for 出库记录ID
     */
	public void setStorageOutId(Long storageOutId) {
		this.storageOutId = storageOutId;
	}

    /**
     * getter for 出库记录ID
     */
	public Long getStorageOutId() {
		return storageOutId;
	}

    /**
     * StorageOutDetailEntity.toString()
     */
    @Override
    public String toString() {
        return "StorageOutDetailEntity{" +
               "id='" + id + '\'' +
               ", cardNo='" + cardNo + '\'' +
               ", storageOutId='" + storageOutId + '\'' +
               '}';
    }

}

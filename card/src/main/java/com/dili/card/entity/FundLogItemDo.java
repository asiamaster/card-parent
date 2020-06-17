package com.dili.card.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 账户资金操作费用,在柜员办理的业务
 * @author bob
 */
public class FundLogItemDo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Long id; 
	/** 操作流水号 */
	private String serialNo; 
	/** 费用类型-押金/工本费/手续费 */
	private Integer type; 
	/** 费用名称 */
	private String typeName; 
	/** 费用金额 -分 */
	private Long amount; 
	/** 创建时间 */
	private LocalDateTime createTime; 
    /**
     * FundLogItemEntity constructor
     */
	public FundLogItemDo() {
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
     * setter for 操作流水号
     */
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

    /**
     * getter for 操作流水号
     */
	public String getSerialNo() {
		return serialNo;
	}

    /**
     * setter for 费用类型-押金/工本费/手续费
     */
	public void setType(Integer type) {
		this.type = type;
	}

    /**
     * getter for 费用类型-押金/工本费/手续费
     */
	public Integer getType() {
		return type;
	}

    /**
     * setter for 费用名称
     */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

    /**
     * getter for 费用名称
     */
	public String getTypeName() {
		return typeName;
	}

    /**
     * setter for 费用金额 -分
     */
	public void setAmount(Long amount) {
		this.amount = amount;
	}

    /**
     * getter for 费用金额 -分
     */
	public Long getAmount() {
		return amount;
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
     * FundLogItemEntity.toString()
     */
    @Override
    public String toString() {
        return "FundLogItemEntity{" +
               "id='" + id + '\'' +
               ", serialNo='" + serialNo + '\'' +
               ", type='" + type + '\'' +
               ", typeName='" + typeName + '\'' +
               ", amount='" + amount + '\'' +
               ", createTime='" + createTime + '\'' +
               '}';
    }

}

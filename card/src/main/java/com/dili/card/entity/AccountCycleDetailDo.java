package com.dili.card.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 柜员账务周期详情
 * @author bob
 */
public class AccountCycleDetailDo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Long id; 
	/** 账务周期流水号 */
	private Long cycleNo; 
	/** 领款次数 */
	private Integer receiveTimes; 
	/** 领款金额-分 */
	private Long receiveAmount; 
	/** 交款次数 */
	private Integer deliverTimes; 
	/** 交款金额-分 */
	private Long deliverAmount; 
	/** 现金充值次数 */
	private Integer depoCashTimes; 
	/** 现金充值金额-分 */
	private Long depoCashAmount; 
	/** POS充值次数 */
	private Integer depoPosTimes; 
	/** POS充值金额-分 */
	private Long depoPosAmount; 
	/** 提现次数 */
	private Integer drawCashTimes; 
	/** 提现金额-分 */
	private Long drawCashAmount; 
	/** 现金收益金额-分 */
	private Long revenueAmount; 
	/** 商户编码 */
	private Long firmId; 
	/** 商户名称 */
	private String firmName; 
	/** 创建时间 */
	private LocalDateTime createTime; 
	/** 修改时间 */
	private LocalDateTime modifyTime; 
    /**
     * AccountCycleDetailEntity constructor
     */
	public AccountCycleDetailDo() {
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
     * setter for 账务周期流水号
     */
	public void setCycleNo(Long cycleNo) {
		this.cycleNo = cycleNo;
	}

    /**
     * getter for 账务周期流水号
     */
	public Long getCycleNo() {
		return cycleNo;
	}

    /**
     * setter for 领款次数
     */
	public void setReceiveTimes(Integer receiveTimes) {
		this.receiveTimes = receiveTimes;
	}

    /**
     * getter for 领款次数
     */
	public Integer getReceiveTimes() {
		return receiveTimes;
	}

    /**
     * setter for 领款金额-分
     */
	public void setReceiveAmount(Long receiveAmount) {
		this.receiveAmount = receiveAmount;
	}

    /**
     * getter for 领款金额-分
     */
	public Long getReceiveAmount() {
		return receiveAmount;
	}

    /**
     * setter for 交款次数
     */
	public void setDeliverTimes(Integer deliverTimes) {
		this.deliverTimes = deliverTimes;
	}

    /**
     * getter for 交款次数
     */
	public Integer getDeliverTimes() {
		return deliverTimes;
	}

    /**
     * setter for 交款金额-分
     */
	public void setDeliverAmount(Long deliverAmount) {
		this.deliverAmount = deliverAmount;
	}

    /**
     * getter for 交款金额-分
     */
	public Long getDeliverAmount() {
		return deliverAmount;
	}

    /**
     * setter for 现金充值次数
     */
	public void setDepoCashTimes(Integer depoCashTimes) {
		this.depoCashTimes = depoCashTimes;
	}

    /**
     * getter for 现金充值次数
     */
	public Integer getDepoCashTimes() {
		return depoCashTimes;
	}

    /**
     * setter for 现金充值金额-分
     */
	public void setDepoCashAmount(Long depoCashAmount) {
		this.depoCashAmount = depoCashAmount;
	}

    /**
     * getter for 现金充值金额-分
     */
	public Long getDepoCashAmount() {
		return depoCashAmount;
	}

    /**
     * setter for POS充值次数
     */
	public void setDepoPosTimes(Integer depoPosTimes) {
		this.depoPosTimes = depoPosTimes;
	}

    /**
     * getter for POS充值次数
     */
	public Integer getDepoPosTimes() {
		return depoPosTimes;
	}

    /**
     * setter for POS充值金额-分
     */
	public void setDepoPosAmount(Long depoPosAmount) {
		this.depoPosAmount = depoPosAmount;
	}

    /**
     * getter for POS充值金额-分
     */
	public Long getDepoPosAmount() {
		return depoPosAmount;
	}

    /**
     * setter for 提现次数
     */
	public void setDrawCashTimes(Integer drawCashTimes) {
		this.drawCashTimes = drawCashTimes;
	}

    /**
     * getter for 提现次数
     */
	public Integer getDrawCashTimes() {
		return drawCashTimes;
	}

    /**
     * setter for 提现金额-分
     */
	public void setDrawCashAmount(Long drawCashAmount) {
		this.drawCashAmount = drawCashAmount;
	}

    /**
     * getter for 提现金额-分
     */
	public Long getDrawCashAmount() {
		return drawCashAmount;
	}

    /**
     * setter for 现金收益金额-分
     */
	public void setRevenueAmount(Long revenueAmount) {
		this.revenueAmount = revenueAmount;
	}

    /**
     * getter for 现金收益金额-分
     */
	public Long getRevenueAmount() {
		return revenueAmount;
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
     * AccountCycleDetailEntity.toString()
     */
    @Override
    public String toString() {
        return "AccountCycleDetailEntity{" +
               "id='" + id + '\'' +
               ", cycleNo='" + cycleNo + '\'' +
               ", receiveTimes='" + receiveTimes + '\'' +
               ", receiveAmount='" + receiveAmount + '\'' +
               ", deliverTimes='" + deliverTimes + '\'' +
               ", deliverAmount='" + deliverAmount + '\'' +
               ", depoCashTimes='" + depoCashTimes + '\'' +
               ", depoCashAmount='" + depoCashAmount + '\'' +
               ", depoPosTimes='" + depoPosTimes + '\'' +
               ", depoPosAmount='" + depoPosAmount + '\'' +
               ", drawCashTimes='" + drawCashTimes + '\'' +
               ", drawCashAmount='" + drawCashAmount + '\'' +
               ", revenueAmount='" + revenueAmount + '\'' +
               ", firmId='" + firmId + '\'' +
               ", firmName='" + firmName + '\'' +
               ", createTime='" + createTime + '\'' +
               ", modifyTime='" + modifyTime + '\'' +
               '}';
    }

}

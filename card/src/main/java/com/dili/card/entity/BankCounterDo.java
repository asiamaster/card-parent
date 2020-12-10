package com.dili.card.entity;

import com.dili.card.dto.BankCounterRequestDto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 银行存取款
 * @author bob
 */
public class BankCounterDo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**  */
	private Long id;
	/** 动作-存款 取款 */
	private Integer action;
	/** 操作金额-分 */
	private Long amount;
	/** 状态-新建 封存 */
	private Integer status;
	/** 银行操作流水号 */
	private String serialNo;
	/** 实际存取款时间 */
	private LocalDateTime applyTime;
	/** 操作人员ID */
	private Long operatorId;
	/** 操作人员名称 */
	private String operatorName;
	/** 市场id */
	private Long firmId;
	/**市场名称*/
	private String firmName;
	/** 备注 */
	private String description;
	/** 创建人ID */
	private Long createdUserId;
	/** 创建人姓名 */
	private String createdUserName;
	/** 创建时间 */
	private LocalDateTime createdTime;
	/** 修改时间 */
	private LocalDateTime modifiedTime;

	public BankCounterDo() {
		super();
	}

	public static BankCounterDo create(BankCounterRequestDto requestDto){
		BankCounterDo bankCounterDo = new BankCounterDo();
		bankCounterDo.setAction(requestDto.getAction());
		bankCounterDo.setAmount(requestDto.getAmount());
		bankCounterDo.setSerialNo(requestDto.getSerialNo());
		bankCounterDo.setStatus(0);
		bankCounterDo.setApplyTime(requestDto.getApplyTime());
		bankCounterDo.setCreatedUserId(requestDto.getOpId());
		bankCounterDo.setCreatedUserName(requestDto.getOpName());
		bankCounterDo.setOperatorId(requestDto.getOpId());
		bankCounterDo.setOperatorName(requestDto.getOpName());
		bankCounterDo.setFirmId(requestDto.getFirmId());
		bankCounterDo.setFirmName(requestDto.getFirmName());
		bankCounterDo.setDescription(requestDto.getDescription());
		bankCounterDo.setModifiedTime(LocalDateTime.now());
		bankCounterDo.setCreatedTime(LocalDateTime.now());
		return bankCounterDo;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getFirmName() {
		return firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public void setAction(Integer action) {
		this.action = action;
	}

	public Integer getAction() {
		return action;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getAmount() {
		return amount;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public void setApplyTime(LocalDateTime applyTime) {
		this.applyTime = applyTime;
	}

	public LocalDateTime getApplyTime() {
		return applyTime;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public Long getFirmId() {
		return firmId;
	}

	public void setFirmId(Long firmId) {
		this.firmId = firmId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public Long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}

	public String getCreatedUserName() {
		return createdUserName;
	}

	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setModifiedTime(LocalDateTime modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public LocalDateTime getModifiedTime() {
		return modifiedTime;
	}


}

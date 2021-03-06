package com.dili.card.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.type.BizNoType;
import com.dili.card.type.CycleState;
import com.dili.ss.util.SpringUtil;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

/**
 * 柜员账务周期
 * @author bob
 */
public class AccountCycleDo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Long id; 
	/** 员工ID */
	private Long userId; 
	/** 员工工号 编号 */
	private String userCode;
	/** 员工姓名 */
	private String userName; 
	/** 账务周期流水号 */
	private Long cycleNo; 
	/** 工位现金柜 */
	private Long cashBox; 
	/** 账务开始时间 */
	private LocalDateTime startTime; 
	/** 账务结束时间 */
	private LocalDateTime endTime; 
	/** 对账时间 */
	private LocalDateTime checkTime; 
	/** 账务状态-激活 结账 平账 */
	private Integer state; 
	/** 交付现金金额-分 */
	private Long cashAmount; 
	/** 审核员-员工ID */
	private Long auditorId; 
	/** 审核员-员工姓名 */
	private String auditorName; 
	/** 数据版本号 */
	private Integer version; 
	/** 商户编码 */
	private Long firmId; 
	/** 商户名称 */
	private String firmName; 
	/** 备注 */
	private String notes; 
	/** 创建时间 */
	private LocalDateTime createTime; 
	/** 修改时间 */
	private LocalDateTime modifyTime; 
    /**
     * AccountCycleEntity constructor
     */
	public AccountCycleDo() {
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
     * setter for 员工ID
     */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

    /**
     * getter for 员工ID
     */
	public Long getUserId() {
		return userId;
	}

    /**
     * setter for 员工姓名
     */
	public void setUserName(String userName) {
		this.userName = userName;
	}

    /**
     * getter for 员工姓名
     */
	public String getUserName() {
		return userName;
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
     * setter for 工位现金柜
     */
	public void setCashBox(Long cashBox) {
		this.cashBox = cashBox;
	}

    /**
     * getter for 工位现金柜
     */
	public Long getCashBox() {
		return cashBox;
	}

    /**
     * setter for 账务开始时间
     */
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

    /**
     * getter for 账务开始时间
     */
	public LocalDateTime getStartTime() {
		return startTime;
	}

    /**
     * setter for 账务结束时间
     */
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

    /**
     * getter for 账务结束时间
     */
	public LocalDateTime getEndTime() {
		return endTime;
	}

    /**
     * setter for 对账时间
     */
	public void setCheckTime(LocalDateTime checkTime) {
		this.checkTime = checkTime;
	}

    /**
     * getter for 对账时间
     */
	public LocalDateTime getCheckTime() {
		return checkTime;
	}

    /**
     * setter for 账务状态-激活 结账 平账
     */
	public void setState(Integer state) {
		this.state = state;
	}

    /**
     * getter for 账务状态-激活 结账 平账
     */
	public Integer getState() {
		return state;
	}

    /**
     * setter for 交付现金金额-分
     */
	public void setCashAmount(Long cashAmount) {
		this.cashAmount = cashAmount;
	}

    /**
     * getter for 交付现金金额-分
     */
	public Long getCashAmount() {
		return cashAmount;
	}

    /**
     * setter for 审核员-员工ID
     */
	public void setAuditorId(Long auditorId) {
		this.auditorId = auditorId;
	}

    /**
     * getter for 审核员-员工ID
     */
	public Long getAuditorId() {
		return auditorId;
	}

    /**
     * setter for 审核员-员工姓名
     */
	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

    /**
     * getter for 审核员-员工姓名
     */
	public String getAuditorName() {
		return auditorName;
	}

    /**
     * setter for 数据版本号
     */
	public void setVersion(Integer version) {
		this.version = version;
	}

    /**
     * getter for 数据版本号
     */
	public Integer getVersion() {
		return version;
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
     * AccountCycleEntity.toString()
     */
    @Override
    public String toString() {
        return "AccountCycleEntity{" +
               "id='" + id + '\'' +
               ", userId='" + userId + '\'' +
               ", userName='" + userName + '\'' +
               ", cycleNo='" + cycleNo + '\'' +
               ", cashBox='" + cashBox + '\'' +
               ", startTime='" + startTime + '\'' +
               ", endTime='" + endTime + '\'' +
               ", checkTime='" + checkTime + '\'' +
               ", state='" + state + '\'' +
               ", cashAmount='" + cashAmount + '\'' +
               ", auditorId='" + auditorId + '\'' +
               ", auditorName='" + auditorName + '\'' +
               ", version='" + version + '\'' +
               ", firmId='" + firmId + '\'' +
               ", firmName='" + firmName + '\'' +
               ", notes='" + notes + '\'' +
               ", createTime='" + createTime + '\'' +
               ", modifyTime='" + modifyTime + '\'' +
               '}';
    }

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	public static class Factory{
		
		public static AccountCycleDo create(Long userId, String userName, String userCode) {
			AccountCycleDo accountCycle = new AccountCycleDo();
			accountCycle.setUserId(userId);
			accountCycle.setUserCode(userCode);
			accountCycle.setUserName(userName);
			accountCycle.setCycleNo(Long.valueOf(SpringUtil.getBean(UidRpcResovler.class).bizNumber(BizNoType.CYCLET_NO.getCode())));
			accountCycle.setCashBox(0L);
			accountCycle.setCashAmount(0L);
			accountCycle.setState(CycleState.ACTIVE.getCode());
			// 构建商户信息
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			accountCycle.setFirmId(userTicket.getFirmId());
			accountCycle.setFirmName(userTicket.getFirmName());
			accountCycle.setVersion(1);
			return accountCycle;
		}
		
	}

}

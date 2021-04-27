package com.dili.card.entity;

import java.time.LocalDateTime;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/4/27 09:31
 * @Description: etc绑定
 */
public class BindETCDo {
    /**主键Id*/
    private Long id;
    /**账号id*/
    private Long accountId;
    /**客户id*/
    private Long customerId;
    /**关联卡号*/
    private String cardNo;
    /**持卡人名称*/
    private String holdName;
    /**车牌号*/
    private String plateNo;
    /**备注*/
    private String description;
    /**绑定状态 1：绑定 0：非绑定*/
    private Integer state;
    /**操作人id*/
    private Long operatorId;
    /**操作人名称*/
    private String operatorName;
    /**支付免密协议号*/
    private String licenseNo;
    /**市场id*/
    private Long firmId;
    /**市场名称*/
    private String firmName;
    /**创建时间*/
    private LocalDateTime createTime;
    /**修改时间*/
    private LocalDateTime modifyTime;

    public BindETCDo() {
        this.createTime = LocalDateTime.now();
        this.modifyTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getHoldName() {
        return holdName;
    }

    public void setHoldName(String holdName) {
        this.holdName = holdName;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
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
}

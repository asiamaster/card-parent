package com.dili.card.entity;

import com.dili.card.type.CardStatus;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户卡片信息（包括电子卡）
 * @author bob
 */
public class UserCardDo implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;
    /** 卡片硬件标识 */
    private String deviceId;
    /** 用户账号 */
    private Long accountId;
    /** 卡号 */
    private String cardNo;
    /** 卡类别-园区卡 银行卡 */
    private Integer category;
    /** 类型-主/副/临时 */
    private Integer type;
    /** 用途-买家/卖家卡 */
    private Integer usageType;
    /** 验证码 */
    private String verifyCode;
    /** 卡片押金-分 */
    private Integer cashPledge;
    /** 是否最近卡片-换卡时使用 */
    private Integer last;
    /** 卡片状态-正常/挂失 */
    private Integer state;
    /** 数据版本号 */
    private Integer version;
    /** 操作人员 */
    private Long creatorId;
    /** 员工名称-保留字段 */
    private String creator;
    /** 商户ID */
    private Long firmId;
    /** 商户名称 */
    private String firmName;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 修改时间 */
    private LocalDateTime modifyTime;

    public UserCardDo() {

    }

    @Override
    public Object clone() {
        UserCardDo userCardDo = new UserCardDo();
        BeanUtils.copyProperties(this, userCardDo);
        return userCardDo;
    }

    public UserCardDo cloneWhenChangeCard(){
        UserCardDo newCard = (UserCardDo)this.clone();
        newCard.setState(CardStatus.NORMAL.getCode());
        return newCard;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getCategory() {
        return category;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setUsageType(Integer usageType) {
        this.usageType = usageType;
    }

    public Integer getUsageType() {
        return usageType;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setCashPledge(Integer cashPledge) {
        this.cashPledge = cashPledge;
    }

    public Integer getCashPledge() {
        return cashPledge;
    }

    public void setLast(Integer last) {
        this.last = last;
    }

    public Integer getLast() {
        return last;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getState() {
        return state;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getVersion() {
        return version;
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

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    @Override
    public String toString() {
        return "UserCardEntity{" +
                "id='" + id + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", accountId='" + accountId + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", usageType='" + usageType + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                ", cashPledge='" + cashPledge + '\'' +
                ", last='" + last + '\'' +
                ", state='" + state + '\'' +
                ", version='" + version + '\'' +
                ", creatorId='" + creatorId + '\'' +
                ", creator='" + creator + '\'' +
                ", firmId='" + firmId + '\'' +
                ", firmName='" + firmName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", modifyTime='" + modifyTime + '\'' +
                '}';
    }

}

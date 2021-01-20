package com.dili.card.entity;

import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.type.BizNoType;
import com.dili.ss.util.SpringUtil;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 冲正记录表(CardReverseRecord)实体类
 *
 * @author miaoguoxin
 * @since 2020-11-24 10:56:09
 */
public class ReverseRecord implements Serializable {
    private static final long serialVersionUID = 778156149778910807L;
    /**主键*/
    private Long id;
    /**冲正业务id*/
    private Long reverseId;
    /**关联的流水号*/
    private String serialNo;
    /**关联的账务周期号(冗余)*/
    private Long cycleNo;
    /**对应的原业务交易渠道(冗余),{@link com.dili.card.type.TradeChannel}*/
    private Integer bizTradeChannel;
    /**对应的业务流水号*/
    private String bizSerialNo;
    /**对应的业务类型，{@link com.dili.card.type.OperateType}*/
    private Integer bizType;
    /**冲正金额（区分正负），单位：分*/
    private Long amount;
    /**园区收益变动金额（区分正负），单位：分*/
    private Long inAccChangeAmount;
    /**操作员id*/
    private Long operatorId;
    /**操作员工号*/
    private String operatorNo;
    /**操作员名字*/
    private String operatorName;
    /**市场id*/
    private Long firmId;
    /**市场名称*/
    private String firmName;
    /**创建时间*/
    private LocalDateTime createTime;
    /**修改时间*/
    private LocalDateTime modifyTime;
    /**标记，1：删除 0：正常*/
    private Integer isDel;

    public static ReverseRecord create(BusinessRecordDo businessRecord, Long amount, Long inAccChangeAmount) {
        ReverseRecord reverseRecord = new ReverseRecord();
        UidRpcResovler uidRpcResovler = SpringUtil.getBean(UidRpcResovler.class);
        String idStr = uidRpcResovler.bizNumber(BizNoType.REVERS_ID.getCode());
        reverseRecord.setAmount(amount);
        reverseRecord.setInAccChangeAmount(inAccChangeAmount);
        reverseRecord.setSerialNo(businessRecord.getSerialNo());
        reverseRecord.setCycleNo(businessRecord.getCycleNo());
        reverseRecord.setBizTradeChannel(businessRecord.getTradeChannel());
        reverseRecord.setReverseId(NumberUtils.toLong(idStr));
        reverseRecord.setCreateTime(LocalDateTime.now());
        reverseRecord.setModifyTime(LocalDateTime.now());
        reverseRecord.setIsDel(0);
        return reverseRecord;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReverseId() {
        return reverseId;
    }

    public void setReverseId(Long reverseId) {
        this.reverseId = reverseId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getBizSerialNo() {
        return bizSerialNo;
    }

    public void setBizSerialNo(String bizSerialNo) {
        this.bizSerialNo = bizSerialNo;
    }

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorNo() {
        return operatorNo;
    }

    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
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

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Long getCycleNo() {
        return cycleNo;
    }

    public void setCycleNo(Long cycleNo) {
        this.cycleNo = cycleNo;
    }

    public Integer getBizTradeChannel() {
        return bizTradeChannel;
    }

    public void setBizTradeChannel(Integer bizTradeChannel) {
        this.bizTradeChannel = bizTradeChannel;
    }

    public Long getInAccChangeAmount() {
        return inAccChangeAmount;
    }

    public void setInAccChangeAmount(Long inAccChangeAmount) {
        this.inAccChangeAmount = inAccChangeAmount;
    }
}

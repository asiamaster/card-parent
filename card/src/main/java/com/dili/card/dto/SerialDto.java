package com.dili.card.dto;

import com.dili.card.entity.SerialRecordDo;
import com.dili.ss.domain.BaseDomain;

import java.util.List;

/**
 * 流水相关dto
 * @author xuliang
 */
public class SerialDto extends BaseDomain {

    /** 流水号*/
    private String serialNo;
    /** 资金项目*/
    private Integer fundItem;
    /** 账户ID*/
    private Long accountId;
    /** 客户ID*/
    private Long customerId;
    /** 卡号*/
    private String cardNo;
    /** 操作员ID*/
    private Long operatorId;
    /** 操作开始时间*/
    private String operateTimeStart;
    /** 操作结束时间*/
    private String operateTimeEnd;
    /** 总的期初余额*/
    private Long startBalance;
    /** 总的期末余额*/
    private Long endBalance;
    /** 商户ID*/
    private Long firmId;
    /** 账务周期号 */
    private Long cycleNo;
    /** 限制条数*/
    private Integer limit;
    /** 操作状态*/
    private Integer state;
    /** 操作类型*/
    private Integer type;
    /** 流水列表 */
    private List<SerialRecordDo> serialRecordList;
    /** 操作类型列表*/
    private List<Integer> operateTypeList;
    /** 账户id列表*/
    private List<Long> accountIdList;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Integer getFundItem() {
        return fundItem;
    }

    public void setFundItem(Integer fundItem) {
        this.fundItem = fundItem;
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

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperateTimeStart() {
        return operateTimeStart;
    }

    public void setOperateTimeStart(String operateTimeStart) {
        this.operateTimeStart = operateTimeStart;
    }

    public String getOperateTimeEnd() {
        return operateTimeEnd;
    }

    public void setOperateTimeEnd(String operateTimeEnd) {
        this.operateTimeEnd = operateTimeEnd;
    }

    public Long getStartBalance() {
        return startBalance;
    }

    public void setStartBalance(Long startBalance) {
        this.startBalance = startBalance;
    }

    public Long getEndBalance() {
        return endBalance;
    }

    public void setEndBalance(Long endBalance) {
        this.endBalance = endBalance;
    }

    public Long getFirmId() {
        return firmId;
    }

    public void setFirmId(Long firmId) {
        this.firmId = firmId;
    }

    public Long getCycleNo() {
        return cycleNo;
    }

    public void setCycleNo(Long cycleNo) {
        this.cycleNo = cycleNo;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<SerialRecordDo> getSerialRecordList() {
        return serialRecordList;
    }

    public void setSerialRecordList(List<SerialRecordDo> serialRecordList) {
        this.serialRecordList = serialRecordList;
    }

    public List<Integer> getOperateTypeList() {
        return operateTypeList;
    }

    public void setOperateTypeList(List<Integer> operateTypeList) {
        this.operateTypeList = operateTypeList;
    }

    public List<Long> getAccountIdList() {
        return accountIdList;
    }

    public void setAccountIdList(List<Long> accountIdList) {
        this.accountIdList = accountIdList;
    }
}

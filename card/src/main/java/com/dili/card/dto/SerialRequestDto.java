package com.dili.card.dto;

import com.dili.card.entity.SerialRecordDo;
import java.util.List;

/**
 * 流水相关dto
 * @author xuliang
 */
public class SerialRequestDto {

    /** 资金项目*/
    private Integer fundItem;
    /** 客户id*/
    private Long customerId;
    /** 卡号*/
    private String cardNo;
    /** 操作员ID*/
    private Long operatorId;
    /** 操作开始时间*/
    private String operateTimeStart;
    /** 操作结束时间*/
    private String operateTimeEnd;
    /** 流水列表 */
    private List<SerialRecordDo> serialRecordList;

    public Integer getFundItem() {
        return fundItem;
    }

    public void setFundItem(Integer fundItem) {
        this.fundItem = fundItem;
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

    public List<SerialRecordDo> getSerialRecordList() {
        return serialRecordList;
    }

    public void setSerialRecordList(List<SerialRecordDo> serialRecordList) {
        this.serialRecordList = serialRecordList;
    }
}

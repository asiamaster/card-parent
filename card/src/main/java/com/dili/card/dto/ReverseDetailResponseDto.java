package com.dili.card.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 冲正详情
 * @Auther: miaoguoxin
 * @Date: 2020/11/25 10:05
 */
public class ReverseDetailResponseDto implements Serializable {
    /**卡账户信息*/
    private UserAccountCardResponseDto accountInfo;
    /**操作业务记录*/
    private BusinessRecordResponseDto bizSerial;
    /**费用项记录*/
    private List<SerialRecordResponseDto> feeSerials;

    public UserAccountCardResponseDto getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(UserAccountCardResponseDto accountInfo) {
        this.accountInfo = accountInfo;
    }

    public BusinessRecordResponseDto getBizSerial() {
        return bizSerial;
    }

    public void setBizSerial(BusinessRecordResponseDto bizSerial) {
        this.bizSerial = bizSerial;
    }

    public List<SerialRecordResponseDto> getFeeSerials() {
        return feeSerials;
    }

    public void setFeeSerials(List<SerialRecordResponseDto> feeSerials) {
        this.feeSerials = feeSerials;
    }
}

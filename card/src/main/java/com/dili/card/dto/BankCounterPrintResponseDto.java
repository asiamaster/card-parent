package com.dili.card.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.card.common.annotation.TextDisplay;
import com.dili.card.common.provider.BankCounterActionProvider;
import com.dili.card.common.provider.FenToYuanProvider;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/1/27 16:38
 * @Description: 银行存取款打印数据
 */
public class BankCounterPrintResponseDto implements Serializable {
    /**收款单位*/
    private String receiveCompany;
    /**收款账户号*/
    private String receiveAccountNo;
    /** 银行操作流水号 */
    private String serialNo;
    /** 操作金额（元） */
    private String amountText;
    /**大写金额*/
    private String amountCN;
    /**存取款类型 {@link com.dili.card.type.BankCounterAction}*/
    @TextDisplay(BankCounterActionProvider.class)
    private Integer action;
    /** 操作人员ID */
    private Long operatorId;
    /** 操作人员名称 */
    private String operatorName;
    /** 备注 */
    private String description;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyTime;
    /** 创建时间 */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    /**打印模板名称*/
    private String printTemplate;

    public String getPrintTemplate() {
        return printTemplate;
    }

    public void setPrintTemplate(String printTemplate) {
        this.printTemplate = printTemplate;
    }

    public String getReceiveCompany() {
        return receiveCompany;
    }

    public void setReceiveCompany(String receiveCompany) {
        this.receiveCompany = receiveCompany;
    }

    public String getReceiveAccountNo() {
        return receiveAccountNo;
    }

    public void setReceiveAccountNo(String receiveAccountNo) {
        this.receiveAccountNo = receiveAccountNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getAmountText() {
        return amountText;
    }

    public void setAmountText(String amountText) {
        this.amountText = amountText;
    }

    public String getAmountCN() {
        return amountCN;
    }

    public void setAmountCN(String amountCN) {
        this.amountCN = amountCN;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(LocalDateTime applyTime) {
        this.applyTime = applyTime;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}

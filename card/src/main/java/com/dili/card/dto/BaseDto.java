package com.dili.card.dto;

import java.io.Serializable;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/19 10:27
 * @Description:
 */
public class BaseDto implements Serializable {
    /**操作员id*/
    private Long opId;
    /**操作员名字*/
    private String opName;
    /** 操作员工号*/
    private String opNo;

    public Long getOpId() {
        return opId;
    }

    public void setOpId(Long opId) {
        this.opId = opId;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public String getOpNo() {
        return opNo;
    }

    public void setOpNo(String opNo) {
        this.opNo = opNo;
    }
}

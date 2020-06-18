package com.dili.card.dto;

import java.io.Serializable;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/17 16:42
 * @Description: 用于接收当前操作员信息
 */
public class OperatorRequestDto implements Serializable {
    /**操作员id*/
    private Long opId;
    /**操作员名字*/
    private String opName;

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
}

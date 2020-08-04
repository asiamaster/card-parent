package com.dili.tcc.bean;

import com.dili.tcc.common.TccStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/23 15:59
 * @Description:
 */
public class TccTransaction implements Serializable {
    /**事务id*/
    private String transId;
    /**{@link TccStatus}*/
    private Integer status;

    private Integer retriedCount;

    private Date createTime;

    private Date lastTime;

    private String targetClass;
    /**请求参数*/
    private Object requestDto;
    private Integer version;
    /**threadLocal传递的参数*/
    private Map<String, Object> attributes;
    /**远程事务节点*/
    private List<TccParticipant> participants;

    public TccTransaction() {
        this.version = 0;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setRetriedCount(Integer retriedCount) {
        this.retriedCount = retriedCount;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public void setRequestDto(Object requestDto) {
        this.requestDto = requestDto;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public void setParticipants(List<TccParticipant> participants) {
        this.participants = participants;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public Object getRequestDto() {
        return requestDto;
    }

    public String getTransId() {
        return transId;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getRetriedCount() {
        return retriedCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public List<TccParticipant> getParticipants() {
        return participants;
    }
}

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

    private Map<String, Object> attributes;
    /**远程事务节点*/
    private List<TccParticipant> participants;

    public TccTransaction(String transId, Integer status,
                          Integer retriedCount, Date createTime,
                          Date lastTime, String targetClass,
                          Object requestDto,
                          Map<String, Object> attributes,
                          List<TccParticipant> participants) {
        this.transId = transId;
        this.status = status;
        this.retriedCount = retriedCount;
        this.createTime = createTime;
        this.lastTime = lastTime;
        this.targetClass = targetClass;
        this.requestDto = requestDto;
        this.attributes = attributes;
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

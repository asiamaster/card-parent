package com.dili.card.dto;

import com.dili.card.type.OperateType;

import java.io.Serializable;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/4/8 09:21
 * @Description: 交易记录汇总统计dto
 */
public class BusinessRecordSummaryDto implements Serializable {
    /**操作类型 {@link com.dili.card.type.OperateType}*/
    private Integer operateType;
    private String operateTypeName;
    /**是否允许点击，前端显示需要 1:允许 0:不允许*/
    private Integer allowClick;
    /**统计数量*/
    private Long count;

    public BusinessRecordSummaryDto() {

    }

    public BusinessRecordSummaryDto(OperateType operateType, Integer allowClick) {
        this.operateType = operateType.getCode();
        this.operateTypeName = operateType.getName();
        this.allowClick = allowClick;
        this.count = 0L;
    }

    public BusinessRecordSummaryDto(OperateType operateType) {
        //默认不可点击
        this(operateType, 0);
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public String getOperateTypeName() {
        return operateTypeName;
    }

    public void setOperateTypeName(String operateTypeName) {
        this.operateTypeName = operateTypeName;
    }

    public Integer getAllowClick() {
        return allowClick;
    }

    public void setAllowClick(Integer allowClick) {
        this.allowClick = allowClick;
    }
}

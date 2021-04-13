package com.dili.card.entity;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/4/8 09:57
 * @Description: 操作记录汇总do
 */
public class BusinessRecordSummaryDo {
    /**操作类型{@link com.dili.card.type.OperateType}*/
    private Integer operateType;
    /**统计数*/
    private Long count;

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}

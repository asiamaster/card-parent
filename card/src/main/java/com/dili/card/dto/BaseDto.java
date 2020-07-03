package com.dili.card.dto;

import com.dili.card.common.annotation.IsOrderBy;
import com.dili.card.validator.ConstantValidator;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
    /** 市场ID*/
    private Long firmId;
    /**市场名称*/
    private String firmName;
    /**页码*/
    @NotNull(message = "页码不能为空", groups = ConstantValidator.Page.class)
    @Min(value = 1, message = "页码最小为1", groups = ConstantValidator.Page.class)
    private Integer page;
    /**页码*/
    @NotNull(message = "分页条数不能为空", groups = ConstantValidator.Page.class)
    @Range(min = 1, max = 100, message = "每页最少1条，最多100条", groups = ConstantValidator.Page.class)
    private Integer rows;
    /**顺序or降序 ASC、DESC*/
    @IsOrderBy(message = "排序不正确", groups = ConstantValidator.Page.class)
    private String order;
    /**排序字段*/
    private String sort;

    public BaseDto setDefSort(String defSort) {
        if (StringUtils.isBlank(this.getSort())) {
            this.setSort(defSort);
        }
        return this;
    }

    public BaseDto setDefOrder(String defColumn) {
        if (StringUtils.isBlank(this.getOrder())) {
            this.setOrder(defColumn);
        }
        return this;
    }

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

    public Long getFirmId() {
        return firmId;
    }

    public void setFirmId(Long firmId) {
        this.firmId = firmId;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }
}

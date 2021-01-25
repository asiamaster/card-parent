package com.dili.card.dto.pay;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/12/24 10:36
 * @Description:
 */
public class PipelineRecordParam {
    /** 交易类型*/
    private Integer type;
    /**提现状态 {@link com.dili.card.type.BankWithdrawState}*/
    private Integer state;
    /**开始日期*/
    private String startDate;
    /**资金账号id*/
    private Long accountId;
    /**结束日期*/
    private String endDate;
    /**页号*/
    private Integer pageNo;
    /**每页记录数*/
    private Integer pageSize;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}

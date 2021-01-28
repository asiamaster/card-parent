package com.dili.card.dto.pay;

import java.io.Serializable;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/1/21 16:58
 * @Description:
 */
public class MerAccountResponseDto implements Serializable {
    /**商户Id*/
    private Long mchId;
    /** 商户code*/
    private String code;
    /**商户名称*/
    private String name;
    /** 商户*/
    private Long parentId;
    /** 主商户ID（0或>0） */
    private Long profitAccount;
    /**收益账户ID*/
    private Long vouchAccount;
    /**担保账户ID */
    private Long pledgeAccount;

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getProfitAccount() {
        return profitAccount;
    }

    public void setProfitAccount(Long profitAccount) {
        this.profitAccount = profitAccount;
    }

    public Long getVouchAccount() {
        return vouchAccount;
    }

    public void setVouchAccount(Long vouchAccount) {
        this.vouchAccount = vouchAccount;
    }

    public Long getPledgeAccount() {
        return pledgeAccount;
    }

    public void setPledgeAccount(Long pledgeAccount) {
        this.pledgeAccount = pledgeAccount;
    }
}

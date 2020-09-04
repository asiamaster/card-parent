package com.dili.card.dto;

import java.io.Serializable;

/**
 * 单个查询账户dto
 * @Auther: miaoguoxin
 * @Date: 2020/8/10 13:45
 */
public class UserAccountSingleQueryDto implements Serializable {
    /** */
    private static final long serialVersionUID = 3420897440951735189L;
    /**account主键id*/
    private Long accountPkId;
    /**card主键id*/
    private Long cardPkId;
    /**账户ID */
    private Long accountId;
    /** 卡号 */
    private String cardNo;

    public Long getAccountPkId() {
        return accountPkId;
    }

    public void setAccountPkId(Long accountPkId) {
        this.accountPkId = accountPkId;
    }

    public Long getCardPkId() {
        return cardPkId;
    }

    public void setCardPkId(Long cardPkId) {
        this.cardPkId = cardPkId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
}

package com.dili.card.dto;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/3 09:38
 * @Description:
 */
public class ApplyRecordRequestDto extends CardRequestDto {
    /**申领人id*/
    private Long applyUserId;
    /**申领人名字*/
    private String applyUserName;
    /**申领人工号*/
    private String applyUserCode;
    /**申领卡集合string , 逗号隔开*/
    private String cardNos;
    /**申领数量*/
    private Integer amount;

    public Long getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(Long applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public String getApplyUserCode() {
        return applyUserCode;
    }

    public void setApplyUserCode(String applyUserCode) {
        this.applyUserCode = applyUserCode;
    }

    public String getCardNos() {
        return cardNos;
    }

    public void setCardNos(String cardNos) {
        this.cardNos = cardNos;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}

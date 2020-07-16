package com.dili.card.dto;

import com.dili.card.validator.ConstantValidator;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/3 09:38
 * @Description:
 */
public class ApplyRecordRequestDto extends CardRequestDto {
    /**申领人id*/
    @NotNull(message = "申领人id必填", groups = ConstantValidator.Insert.class)
    @Min(value = 1, message = "申领人id至少1", groups = ConstantValidator.Insert.class)
    private Long applyUserId;
    /**申领人名字*/
    @NotBlank(message = "申领人名字不能为空", groups = ConstantValidator.Insert.class)
    private String applyUserName;
    /**申领人工号*/
    @NotBlank(message = "申领人工号不能为空", groups = ConstantValidator.Insert.class)
    private String applyUserCode;
    /**申领卡集合string , 逗号隔开*/
    @NotBlank(message = "申领卡号不能为空", groups = ConstantValidator.Insert.class)
    private String cardNos;
    /**申领数量*/
    @NotNull(message = "申领数量不能为空", groups = ConstantValidator.Insert.class)
    @Min(value = 1, message = "申领数量至少1张", groups = ConstantValidator.Insert.class)
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

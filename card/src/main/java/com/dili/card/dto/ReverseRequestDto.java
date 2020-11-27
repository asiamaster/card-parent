package com.dili.card.dto;

import com.dili.card.validator.ConstantValidator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/11/25 14:55
 * @Description:
 */
public class ReverseRequestDto extends CardRequestDto {
    /**对应业务号*/
    @NotBlank(groups = ConstantValidator.Insert.class, message = "业务号不能为空")
    private String bizSerialNo;
    /**冲正金额（分），区分正负*/
    @NotNull(groups = ConstantValidator.Insert.class, message = "冲正金额不能为空")
    private Long amount;
    /**需要补正的手续费，区分正负*/
    private Long fee;

    public String getBizSerialNo() {
        return bizSerialNo;
    }

    public void setBizSerialNo(String bizSerialNo) {
        this.bizSerialNo = bizSerialNo;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }
}

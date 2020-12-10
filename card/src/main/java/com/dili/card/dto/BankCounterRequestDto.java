package com.dili.card.dto;

import com.dili.card.validator.ConstantValidator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/12/10 09:53
 * @Description:
 */
public class BankCounterRequestDto extends BaseDto {
    /**存取款金额(分)*/
    @NotNull(message = "金额不能为空", groups = ConstantValidator.Insert.class)
    private Long amount;
    /**类型 {@link com.dili.card.type.BankCounterAction}*/
    @NotNull(message = "类型参数缺失", groups = ConstantValidator.Insert.class)
    private Integer action;
    /**银行流水号*/
    @NotBlank(message = "流水号不能为空", groups = ConstantValidator.Insert.class)
    private String serialNo;
    /**存取款时间*/
    @NotNull(message = "存取款时间不能为空", groups = ConstantValidator.Insert.class)
    private LocalDateTime applyTime;
    /**备注*/
    private String description;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public LocalDateTime getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(LocalDateTime applyTime) {
        this.applyTime = applyTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

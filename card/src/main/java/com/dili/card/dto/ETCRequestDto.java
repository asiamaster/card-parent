package com.dili.card.dto;

import com.dili.card.validator.ConstantValidator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/4/27 09:14
 * @Description:
 */
public class ETCRequestDto extends CardRequestDto {
    @NotNull(message = "id缺失", groups = ConstantValidator.Update.class)
    private Long id;
    /**车牌号*/
    @NotBlank(message = "车牌号不能为空", groups = ConstantValidator.Insert.class)
    private String plateNo;
    /**交易密码*/
    @NotBlank(message = "交易密码缺失", groups = ConstantValidator.Insert.class)
    private String tradePwd;
    /**备注*/
    @Size(max = 50, message = "备注不能超过50个字符", groups = ConstantValidator.Insert.class)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTradePwd() {
        return tradePwd;
    }

    public void setTradePwd(String tradePwd) {
        this.tradePwd = tradePwd;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

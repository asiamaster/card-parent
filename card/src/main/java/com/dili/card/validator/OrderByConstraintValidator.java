package com.dili.card.validator;

import com.dili.card.common.annotation.IsOrderBy;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/23 13:33
 * @Description: 校验orderBy正确性(DESC 、 ASC)
 */
public class OrderByConstraintValidator implements ConstraintValidator<IsOrderBy, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return true;
        }
        return value.equalsIgnoreCase("ASC")
                || value.equalsIgnoreCase("DESC");
    }
}

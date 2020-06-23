package com.dili.card.util;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/23 13:33
 * @Description: 校验orderBy正确性(DESC、ASC)
 */
public class OrderByValidator implements ConstraintValidator<IsOrderBy,String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)){
            return true;
        }
        return value.equalsIgnoreCase("ASC")
                || value.equalsIgnoreCase("DESC");
    }
}

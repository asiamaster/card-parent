package com.dili.card.common.annotation;

import com.dili.ss.metadata.ValueProvider;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Map;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 字段展示，用于cardType、cardStatus这些状态枚举给前端展示转行
 * {@link com.dili.card.common.serializer.EnumTextDisplayAfterFilter}
 * @Auther: miaoguoxin
 * @Date: 2020/6/23 13:31
 */
@Target({FIELD})
@Retention(RUNTIME)
@Documented
public @interface TextDisplay {

    /**
     *  转换的provider
     * @author miaoguoxin
     * @date 2020/6/24
     */
    Class<? extends ValueProvider> value();

}

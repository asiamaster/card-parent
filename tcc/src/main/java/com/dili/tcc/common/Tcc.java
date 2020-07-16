package com.dili.tcc.common;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/23 13:31
 * @Description: 标记tcc的rpc feign服务方法
 */
@Target({METHOD})
@Retention(RUNTIME)
@Documented
public @interface Tcc {

}

package com.dili.tcc.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/7 16:07
 * @Description:
 */
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.applicationContext = applicationContext;
    }

    /**
    * 获取bean
    * @author miaoguoxin
    * @date 2020/7/7
    */
    public static <T> T getBean(Class<T> tClass){

        return applicationContext.getBean(tClass);
    }
}

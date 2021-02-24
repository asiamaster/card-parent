package com.dili.card.service;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/2/22 09:56
 * @Description: 杂项
 */
public interface IMiscService {

    /**
     * 获取单个字典值
     * @author miaoguoxin
     * @date 2021/2/22
     */
    String getSingleDictVal(String code, Long firmId);

    /**
     * 获取单个字典值
     * @author miaoguoxin
     * @date 2021/2/22
     */
    String getSingleDictVal(String code, Long firmId,String defaultVal);
}

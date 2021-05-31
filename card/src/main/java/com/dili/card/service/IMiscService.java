package com.dili.card.service;

import com.dili.card.common.constant.Constant;
import com.dili.card.common.constant.FirmIdConstant;
import com.dili.card.util.AssertUtils;

import javax.servlet.http.HttpServletRequest;

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

    /**
    * 设置子商户id（用于支付服务传递）
    * @author miaoguoxin
    * @date 2021/5/31
    */
    void setSubMarketIdToRequest(Long firmId, Long costFee) ;

}

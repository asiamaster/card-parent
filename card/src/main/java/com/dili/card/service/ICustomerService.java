package com.dili.card.service;

import com.dili.card.dto.CustomerResponseDto;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/2 13:15
 * @Description:
 */
public interface ICustomerService {

    /**
    * 根据卡号查询客户信息
    * @author miaoguoxin
    * @date 2020/7/2
    */
    CustomerResponseDto getByCardNoWithCache(String cardNo);
}

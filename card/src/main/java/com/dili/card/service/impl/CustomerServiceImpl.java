package com.dili.card.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.dili.card.common.constant.CacheKey;
import com.dili.card.dto.CustomerResponseDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.rpc.resolver.CustomerRpcResolver;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/2 13:16
 * @Description:
 */
@Service
public class CustomerServiceImpl implements ICustomerService {
    private final TimedCache<String, CustomerResponseDto> customerCache;

    public CustomerServiceImpl() {
        //默认30分钟过期
        customerCache = CacheUtil.newTimedCache(30 * 60 * 1000);
    }

    @Autowired
    private CustomerRpcResolver customerRpcResolver;
    @Autowired
    private IAccountQueryService accountQueryService;

    @Override
    public CustomerResponseDto getByCardNoWithCache(String cardNo) {
        String key = CacheKey.CUSTOMER_INFO_PREFIX + cardNo;
        CustomerResponseDto customerInfo = customerCache.get(key);
        if (customerInfo != null) {
            return customerInfo;
        }
        UserAccountCardResponseDto byCardNo = accountQueryService.getByCardNo(cardNo);
        CustomerResponseDto responseDto = customerRpcResolver.findCustomerByIdWithConvert(byCardNo.getCustomerId(), byCardNo.getFirmId());
        customerCache.put(key, responseDto);
        return responseDto;
    }
}

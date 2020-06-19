package com.dili.card.service;

import com.dili.customer.sdk.domain.Customer;

/**
 * 客户相关service接口
 */
public interface ICustomerService {

    /**
     * 查询客户信息
     * @param customerId
     * @param firmId
     * @return
     */
    Customer getWithNotNull(Long customerId, Long firmId);
}

package com.dili.card.service.impl;

import com.dili.card.service.ICustomerService;
import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 客户相关service实现类
 */
@Service
public class CustomerServiceImpl implements ICustomerService {

    @Resource
    private CustomerRpc customerRpc;

    public Customer getWithNotNull(Long customerId, Long firmId) {
        BaseOutput<Customer> baseOutput = customerRpc.get(customerId, firmId);
        if (!baseOutput.isSuccess()) {
            throw new BusinessException("", "查询客户信息失败");
        }
        Customer customer = baseOutput.getData();
        Optional.ofNullable(customer)
                .orElseThrow(() -> new BusinessException("", "客户不存在"));
        return customer;
    }
}

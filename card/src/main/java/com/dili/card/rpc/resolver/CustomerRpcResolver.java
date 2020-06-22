package com.dili.card.rpc.resolver;


import com.dili.card.dto.CustomerResponseDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.domain.dto.CustomerQueryInput;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 客户查询解析器
 * @author apache
 */
@Component
public class CustomerRpcResolver {

    @Autowired
    private CustomerRpc customerRpc;

    /**
     * 根据客户id查询客户信息
     */
    public Customer findCustomerById(Long id) {
        CustomerQueryInput customer = new CustomerQueryInput();
        customer.setId(id);
        BaseOutput<List<Customer>> baseOutput = customerRpc.list(customer);
        if (!baseOutput.isSuccess()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "远程调用客户服务失败");
        }
        if (CollectionUtils.isEmpty(baseOutput.getData())) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "客户信息不存在");
        }
        return baseOutput.getData().get(0);
    }

    /**
     * 根据id批量查询客户信息
     * @author miaoguoxin
     * @date 2020/6/22
     */
    public List<Customer> findCustomerByIds(List<Long> ids) {
        CustomerQueryInput customer = new CustomerQueryInput();
        customer.setIdList(ids);
        BaseOutput<List<Customer>> baseOutput = customerRpc.list(customer);
        if (!baseOutput.isSuccess()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "远程调用客户服务失败");
        }
        if (CollectionUtils.isEmpty(baseOutput.getData())) {
            return new ArrayList<>();
        }
        return baseOutput.getData();
    }

    /**
    *  根据id批量查客户然后转换一下
    * @author miaoguoxin
    * @date 2020/6/22
    */
    public List<CustomerResponseDto> findCustomerByIdsWithConvert(List<Long> ids) {
        List<Customer> customers = this.findCustomerByIds(ids);
        return customers.stream()
                .map(this::convertFromCustomer)
                .collect(Collectors.toList());
    }

    /**
     * 根据客户姓名查询客户信息
     */
    public List<Customer> findCustomerByName(String name) {
        CustomerQueryInput customer = new CustomerQueryInput();
        customer.setName(name);
        BaseOutput<List<Customer>> baseOutput = customerRpc.list(customer);
        if (!baseOutput.isSuccess()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "远程调用客户服务失败");
        }
        return baseOutput.getData();
    }


    /**
     * 获取非空客户
     * @param customerId
     * @param firmId
     * @return
     */
    public Customer getWithNotNull(Long customerId, Long firmId) {
        BaseOutput<Customer> baseOutput = customerRpc.get(customerId, firmId);
        if (!baseOutput.isSuccess()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "远程调用客户服务失败");
        }
        Customer customer = baseOutput.getData();
        Optional.ofNullable(customer)
                .orElseThrow(() -> new CardAppBizException(ResultCode.DATA_ERROR, "客户不存在"));
        return customer;
    }


    private CustomerResponseDto convertFromCustomer(Customer customer) {
        CustomerResponseDto customerResponseDto = new CustomerResponseDto();
        BeanUtils.copyProperties(customer, customerResponseDto);
        return customerResponseDto;
    }
}

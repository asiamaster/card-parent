package com.dili.card.rpc.resolver;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.dili.card.dto.CustomerResponseDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.domain.dto.CustomerQueryInput;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;

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
    public CustomerExtendDto findCustomerById(Long id,Long firmId) {
        CustomerQueryInput customer = new CustomerQueryInput();
        customer.setId(id);
        BaseOutput<List<CustomerExtendDto>> baseOutput = customerRpc.list(customer);
        List<CustomerExtendDto> customers = GenericRpcResolver.resolver(baseOutput,"customerRpc");
        if (CollectionUtils.isEmpty(customers)) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "客户信息不存在");
        }
        return baseOutput.getData().get(0);
    }

    /**
     * 通过账号批量查询客户map结构数据
     */
    public Map<Long, CustomerExtendDto> findCustomerMapByCustomerIds(List<Long> customerIds, Long firmId) {
        List<CustomerExtendDto> customers = findCustomerByIds(customerIds, firmId);
        return customers.stream()
                .collect(Collectors.toMap(CustomerExtendDto::getId,
                        a -> a,
                        (k1, k2) -> k1));
    }

    /**
     * 根据id批量查询客户信息
     * @author miaoguoxin
     * @date 2020/6/22
     */
    public List<CustomerExtendDto> findCustomerByIds(List<Long> ids, Long firmId) {
        CustomerQueryInput customer = new CustomerQueryInput();
        customer.setIdSet(new HashSet<>(ids));
        customer.setMarketId(firmId);
        BaseOutput<List<CustomerExtendDto>> baseOutput = customerRpc.list(customer);
        List<CustomerExtendDto> customers = GenericRpcResolver.resolver(baseOutput,"customer-service");
        if (CollectionUtils.isEmpty(customers)) {
            return new ArrayList<>();
        }
        return baseOutput.getData();
    }

    /**
     * 根据客户id查询然后转换一下
     * @param
     * @return
     * @author miaoguoxin
     * @date 2020/6/28
     */
    public CustomerResponseDto findCustomerByIdWithConvert(Long id, Long firmId) {
        Customer customer = this.getWithNotNull(id, firmId);
        return this.convertFromCustomer(customer);
    }

    /**
     *  根据id批量查客户然后转换一下
     * @author miaoguoxin
     * @date 2020/6/22
     */
    public List<CustomerResponseDto> findCustomerByIdsWithConvert(List<Long> ids, Long firmId) {
        List<CustomerExtendDto> customers = this.findCustomerByIds(ids, firmId);
        return customers.stream()
                .map(this::convertFromCustomer)
                .collect(Collectors.toList());
    }

    /**
     * 根据客户姓名查询客户信息
     */
    public List<CustomerExtendDto> findCustomerByName(String name) {
        CustomerQueryInput customer = new CustomerQueryInput();
        customer.setName(name);
        BaseOutput<List<CustomerExtendDto>> baseOutput = customerRpc.list(customer);
        if (!baseOutput.isSuccess()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, baseOutput.getMessage());
        }
        return baseOutput.getData();
    }


    /**
     * 获取非空客户
     * @param customerId
     * @param firmId
     * @return
     */
    public CustomerExtendDto getWithNotNull(Long customerId, Long firmId) {
        BaseOutput<CustomerExtendDto> baseOutput = customerRpc.get(customerId, firmId);
        if (!baseOutput.isSuccess()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "远程调用客户服务失败");
        }
        CustomerExtendDto customer = baseOutput.getData();
        Optional.ofNullable(customer)
                .orElseThrow(() -> new CardAppBizException(ResultCode.DATA_ERROR, "客户不存在"));
        return customer;
    }

    /**
     *
     * @param customer
     * @return
     */
    private CustomerResponseDto convertFromCustomer(Customer customer) {
        CustomerResponseDto customerResponseDto = new CustomerResponseDto();
        customerResponseDto.setCode(customer.getCode());
        customerResponseDto.setId(customer.getId());
        customerResponseDto.setName(customer.getName());
//        customerResponseDto.setCustomerType(customer.getc);
//        customerResponseDto.setCustomerTypeName(CustomerType.getTypeName(customer.getCustomerMarket().getType()));
        customerResponseDto.setCertificateAddr(customer.getCertificateAddr());
        customerResponseDto.setCertificateNumber(customer.getCertificateNumber());
        customerResponseDto.setCertificateType(customer.getCertificateType());
        customerResponseDto.setCustomerContactsPhone(customer.getContactsPhone());
        customerResponseDto.setOrganizationType(customer.getOrganizationType());
        return customerResponseDto;
    }

    /**
     * 根据条件查询客户列表
     * @param query
     * @return
     */
    public List<CustomerExtendDto> list(CustomerQueryInput query) {
        BaseOutput<List<CustomerExtendDto>> baseOutput = customerRpc.list(query);
        if (!baseOutput.isSuccess()) {
            throw new CardAppBizException("远程调用客户服务失败");
        }
        return baseOutput.getData();
    }
}

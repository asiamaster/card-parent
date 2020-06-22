package com.dili.card.rpc.resolver;


import com.dili.card.exception.CardAppBizException;
import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.domain.dto.CustomerQuery;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

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
    public Customer findCustomerById(Long id){
    	CustomerQuery customer = new CustomerQuery();
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
	 * 根据客户姓名查询客户信息
	 */
    public List<Customer> findCustomerByName(String name){
    	CustomerQuery customer = new CustomerQuery();
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
}

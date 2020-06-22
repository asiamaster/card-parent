package com.dili.card.rpc.resolver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.dili.card.dto.CustomerDto;
import com.dili.card.dto.CustomerQuery;
import com.dili.card.rpc.CustomerRpc;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;

/**
 * 客户查询解析器
 * @author apache
 */
public class CustomerRpcResolver {
	
	@Autowired
	private CustomerRpc customerRpc;
	
	/**
	 * 根据客户id查询客户信息
	 */
    public CustomerDto findCustomerById(Long id){
    	CustomerQuery customer = new CustomerQuery();
    	customer.setId(id);
    	BaseOutput<List<CustomerDto>> baseOutput = customerRpc.list(customer);
    	if (!baseOutput.isSuccess()) {
    		throw new BusinessException(ResultCode.DATA_ERROR, "远程调用客户服务失败");
		}
    	if (CollectionUtils.isEmpty(baseOutput.getData())) {
    		throw new BusinessException(ResultCode.DATA_ERROR, "客户信息不存在");
		}
		return baseOutput.getData().get(0);
    }
    
    /**
	 * 根据客户姓名查询客户信息
	 */
    public List<CustomerDto> findCustomerByName(String name){
    	CustomerQuery customer = new CustomerQuery();
    	customer.setName(name);
    	BaseOutput<List<CustomerDto>> baseOutput = customerRpc.list(customer);
    	if (!baseOutput.isSuccess()) {
    		throw new BusinessException(ResultCode.DATA_ERROR, "远程调用客户服务失败");
		}
		return baseOutput.getData();
    }
}

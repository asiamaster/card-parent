package com.dili.card.controller;

import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.rpc.resolver.CustomerRpcResolver;
import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.domain.dto.CustomerQueryInput;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 客户相关controller
 */
@Controller
@RequestMapping(value = "/customer")
public class CustomerController implements IControllerHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
    @Resource
    private CustomerRpcResolver customerRpcResolver;

    /**
     * 查询客户列表
     * @param keyword
     * @return
     */
    @RequestMapping(value = "/listByKeyword.action")
    @ResponseBody
    public BaseOutput<List<CustomerExtendDto>> listByKeyword(String keyword) {
        LOGGER.info("查询客户列表*****{}", keyword);
        CustomerQueryInput query = new CustomerQueryInput();
        UserTicket userTicket = getUserTicket();
        query.setMarketId(userTicket.getFirmId());
        query.setKeyword(keyword);
        List<CustomerExtendDto> itemList = customerRpcResolver.list(query);
        return BaseOutput.successData(itemList);
    }

    /**
     * 根据客户姓名查询客户列表
     * @param name
     * @return
     */
    @RequestMapping(value = "/listByName.action")
    @ResponseBody
    public BaseOutput<List<CustomerExtendDto>> listByName(String name) {
        LOGGER.info("查询客户列表*****{}", name);
        CustomerQueryInput query = new CustomerQueryInput();
        UserTicket userTicket = getUserTicket();
        query.setMarketId(userTicket.getFirmId());
        query.setName(name);
        List<CustomerExtendDto> itemList = customerRpcResolver.list(query);
        return BaseOutput.successData(itemList);
    }

}

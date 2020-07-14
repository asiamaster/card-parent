package com.dili.card.controller;

import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.CustomerResponseDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.CustomerRpcResolver;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.ICustomerService;
import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.domain.dto.CustomerQueryInput;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Autowired
    private IAccountQueryService accountQueryService;
    @Autowired
    private ICustomerService customerService;

    /**
     * 查询客户列表
     * @param keyword
     * @return
     */
    @RequestMapping(value = "/listByKeyword.action")
    @ResponseBody
    public BaseOutput<List<Customer>> listByKeyword(String keyword) {
        try {
            CustomerQueryInput query = new CustomerQueryInput();
            UserTicket userTicket = getUserTicket();
            query.setMarketId(userTicket.getFirmId());
            query.setKeyword(keyword);
            List<Customer> itemList = customerRpcResolver.list(query);
            return BaseOutput.success().setData(itemList);
        } catch (CardAppBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("listByKeyword", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 根据卡号查询客户信息
     * @author miaoguoxin
     * @date 2020/7/2
     */
    @RequestMapping(value = "/infoByCardNo.action")
    @ResponseBody
    public BaseOutput<CustomerResponseDto> getByCardNo(String cardNo) {
        return BaseOutput.successData(customerService.getByCardNoWithCache(cardNo));
    }
}

package com.dili.card.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.card.common.holder.IUserTicketHolder;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.CustomerRpcResolver;
import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.domain.dto.CustomerQueryInput;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;

/**
 * 客户相关controller
 */
@Controller
@RequestMapping(value = "/customer")
public class CustomerController implements IUserTicketHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Resource
    private CustomerRpcResolver customerRpcResolver;
    /**
     * 查询客户列表
     * @param query
     * @return
     */
    @RequestMapping(value = "/list.action")
    @ResponseBody
    public BaseOutput<List<Customer>> list(CustomerQueryInput query) {
        try {
            UserTicket userTicket = getUserTicket();
            query.setMarketId(userTicket.getFirmId());
            List<Customer> itemList = customerRpcResolver.list(query);
            return BaseOutput.success().setData(itemList);
        } catch (CardAppBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("list", e);
            return BaseOutput.failure();
        }
    }
}

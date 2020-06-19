package com.dili.card.rpc;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dili.card.dto.CustomerDto;
import com.dili.card.dto.CustomerQuery;
import com.dili.ss.domain.BaseOutput;

@FeignClient(name = "customer-service")
public interface CustomerRpc {

    /** 获取客户列表信息
     * @param customer
     * @return
             */
    @RequestMapping(value = "/api/customer/list", method = RequestMethod.POST)
    BaseOutput<List<CustomerDto>> list(CustomerQuery customer);

    /** 获取有效客户列表信息
     * @param customer
     * @return
             */
    @RequestMapping(value = "/api/customer/listNormalPage", method = RequestMethod.POST)
    BaseOutput<List<CustomerDto>> listNormalPage(CustomerQuery customer);

    /** 根据市场ID和客户ID，获取客户
     * @param id 客户ID
     * @param marketId 市场ID
     * @return
     */
    @RequestMapping(value = "/api/customer/get", method = RequestMethod.POST)
    BaseOutput<CustomerDto> get(@RequestParam("id") Long id, @RequestParam("marketId") Long marketId);


    /** 根据客户证件号，获取客户
     * @param id 客户ID
     * @param marketId 市场ID
     * @return
     */
    @RequestMapping(value = "/api/customer/getByCertificateNumber", method = RequestMethod.POST)
    BaseOutput<CustomerDto> getByCertificateNumber(@RequestParam("certificateNumber") String certificateNumber, @RequestParam("marketId") Long marketId);

}

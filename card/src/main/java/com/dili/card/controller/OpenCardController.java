package com.dili.card.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.CustomerResponseDto;
import com.dili.card.dto.OpenCardDto;
import com.dili.card.dto.OpenCardResponseDto;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.IOpenCardService;
import com.dili.card.type.CustomerType;
import com.dili.card.type.ServiceName;
import com.dili.card.util.AssertUtils;
import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.domain.dto.CustomerQueryInput;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

import cn.hutool.core.util.RandomUtil;

/**
 * @description： 开卡服务
 * 
 * @author ：WangBo
 * @time ：2020年6月30日上午11:28:51
 */
//@RestController
@Controller
@RequestMapping(value = "/card")
public class OpenCardController implements IControllerHandler {

	@Resource
	private IOpenCardService openCardService;
	@Resource
	CustomerRpc customerRpc;

	/**
	 * 主卡开卡
	 * 
	 * @throws InterruptedException
	 */
	@RequestMapping(value = "openMasterCard.html", method = { RequestMethod.GET, RequestMethod.POST })
	public String toOpenCardHtml() {
		return "clientTest/openCard";
	}

	/**
	 * 主卡开卡
	 * 
	 * @throws InterruptedException
	 */
	@RequestMapping(value = "getCustomerInfoTest.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<Customer> getCustomerInfoTest(Long customerId) {
		// 操作人信息
		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		if (customerId == null) {
			customerId = RandomUtil.getRandom().nextLong(210);
		}
		Customer customer = GenericRpcResolver.resolver(customerRpc.get(customerId, user.getFirmId()), "测试获取用户信息");
		return BaseOutput.successData(customer);
	}

	/**
	 * 主卡开卡
	 * 
	 * @throws InterruptedException
	 */
	@RequestMapping(value = "getCustomerInfo.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<CustomerResponseDto> getCustomerInfo(@RequestBody OpenCardDto openCardInfo) {
		// 操作人信息
		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		if (user == null) {
			return BaseOutput.failure("未获取到登录用户信息，请重新登录!");
		}
		AssertUtils.notEmpty(openCardInfo.getCertificateNumber(), "请输入证件号!");
		Customer customer = GenericRpcResolver
				.resolver(customerRpc.getByCertificateNumber(openCardInfo.getCertificateNumber(), user.getFirmId()), "开卡客户查询");
		CustomerResponseDto response = new CustomerResponseDto();
		BeanUtils.copyProperties(customer, response);
		response.setCustomerTypeName(CustomerType.getTypeName(customer.getCustomerMarket().getType()));
		return BaseOutput.successData(response);
	}

	/**
	 * 主卡开卡
	 * 
	 * @throws InterruptedException
	 */
	@PostMapping("openMasterCard.action")
	@ResponseBody
	public BaseOutput<?> openMasterCard(OpenCardDto openCardInfo) {
		// 主要参数校验
		checkMasterParam(openCardInfo);
		// 操作人信息
		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		openCardInfo.setCreator(user.getRealName());
		openCardInfo.setCreatorId(user.getId());
		openCardInfo.setCreatorCode(user.getUserName());
		openCardInfo.setFirmId(user.getFirmId());
		openCardInfo.setFirmName(user.getFirmName());
		// 开卡
		OpenCardResponseDto response = openCardService.openMasterCard(openCardInfo);
		return BaseOutput.success("success").setData(response);
	}

	/**
	 * 副卡开卡
	 */
	@PostMapping("openSlaveCard.action")
	@ResponseBody
	public BaseOutput<?> openSlaveCard(OpenCardDto openCardInfo) throws Exception {
		// 主要参数校验
		AssertUtils.notNull(openCardInfo.getParentAccountId(), "主卡信息不能为空!");
		// 操作人信息
		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		if (user == null) {
			return BaseOutput.failure("未获取到登录用户信息，请重新登录!");
		}
		openCardInfo.setCreator(user.getRealName());
		openCardInfo.setCreatorId(user.getId());
		openCardInfo.setCreatorCode(user.getUserName());
		openCardInfo.setFirmId(user.getFirmId());
		openCardInfo.setFirmName(user.getFirmName());
		OpenCardResponseDto response = openCardService.openSlaveCard(openCardInfo);
		return BaseOutput.success("success").setData(response);
	}

	/**
	 * 主卡参数校验
	 * 
	 * @param openCardInfo
	 */
	private void checkMasterParam(OpenCardDto openCardInfo) {
		AssertUtils.notEmpty(openCardInfo.getName(), "开卡用户名不能为空!");
		AssertUtils.notEmpty(openCardInfo.getCustomerNo(), "客户编号不能为空!");
		AssertUtils.notNull(openCardInfo.getCustomerId(), "客户ID不能为空!");
		AssertUtils.notEmpty(openCardInfo.getLoginPwd(), "账户密码不能为空!");
	}
}

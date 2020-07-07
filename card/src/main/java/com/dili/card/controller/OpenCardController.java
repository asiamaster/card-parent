package com.dili.card.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.OpenCardDto;
import com.dili.card.dto.OpenCardResponseDto;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.IOpenCardService;
import com.dili.card.type.ServiceName;
import com.dili.card.util.AssertUtils;
import com.dili.customer.sdk.domain.Customer;
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
	@RequestMapping(value = "getCustomerInfo.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<Customer> getCustomerInfo(Long customerId) {
		// 操作人信息
		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		if (customerId == null) {
			customerId = RandomUtil.getRandom().nextLong(210);
		}
		Customer customer = GenericRpcResolver.resolver(customerRpc.get(customerId, user.getFirmId()),
				ServiceName.CUSTOMER);
		return BaseOutput.successData(customer);
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
			return BaseOutput.failure("未获取到用户信息，请重新登录!");
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
//		AssertUtils.notEmpty(openCardInfo.getCredentialNo(), "开卡用户名证件号不能为空!");
		AssertUtils.notEmpty(openCardInfo.getMobile(), "开卡手机号不能为空!");
//		AssertUtils.notNull(openCardInfo.getFirmId(), "开卡市场编码不能为空!");
		AssertUtils.notEmpty(openCardInfo.getLoginPwd(), "账户密码不能为空!");
	}
}

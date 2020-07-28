package com.dili.card.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.AccountCustomerDto;
import com.dili.card.dto.CardStorageDto;
import com.dili.card.dto.CustomerResponseDto;
import com.dili.card.dto.OpenCardDto;
import com.dili.card.dto.OpenCardResponseDto;
import com.dili.card.rpc.AccountQueryRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.ICardStorageService;
import com.dili.card.service.IOpenCardService;
import com.dili.card.type.CardStorageState;
import com.dili.card.type.CardType;
import com.dili.card.type.CustomerType;
import com.dili.card.util.AssertUtils;
import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.util.MoneyUtils;
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

	private static final Logger log = LoggerFactory.getLogger(OpenCardController.class);

	@Resource
	private IOpenCardService openCardService;
	@Resource
	CustomerRpc customerRpc;
	@Resource
	AccountQueryRpc accountQueryRpc;
	@Resource
	IAccountQueryService accountQueryService;
	@Resource
	ICardStorageService cardStorageService;

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
	 * 主卡开卡测试用户查询
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
	 * 查询客户信息
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
		Customer customer = GenericRpcResolver.resolver(
				customerRpc.getByCertificateNumber(openCardInfo.getCertificateNumber(), user.getFirmId()), "开卡客户查询");
		CustomerResponseDto response = new CustomerResponseDto();
		if(customer != null) {
			BeanUtils.copyProperties(customer, response);
			response.setCustomerTypeName(CustomerType.getTypeName(customer.getCustomerMarket().getType()));
		}
		return BaseOutput.successData(response);
	}

	/**
	 * 根据主卡卡号，查询主卡信息和客户信息
	 * 
	 * @param openCardInfo
	 * @return
	 */
	@RequestMapping(value = "getAccountInfo.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<AccountCustomerDto> getAccountInfo(@RequestBody OpenCardDto openCardInfo) {
		log.info("开卡查询主卡信息:{}", JSONObject.toJSONString(openCardInfo));
		AssertUtils.notNull(openCardInfo.getCardNo(), "请输入主卡卡号!");
		AccountCustomerDto response = accountQueryService.getAccountCustomerByCardNo(openCardInfo.getCardNo());
		return BaseOutput.successData(response);
	}

	/**
	 * 检查新卡卡号状态
	 */
	@RequestMapping(value = "checkNewCardNo.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<?> checkNewCardNo(@RequestBody OpenCardDto openCardInfo) {
		AssertUtils.notNull(openCardInfo.getCardNo(), "请输入主卡卡号!");
		CardStorageDto cardStorage = cardStorageService.getCardStorageByCardNo(openCardInfo.getCardNo());
		if (cardStorage.getState() != CardStorageState.ACTIVE.getCode()) {
			return BaseOutput.failure("该卡状态为[" + CardStorageState.getName(cardStorage.getState()) + "],不能开卡!");
		}
		if (cardStorage.getType().intValue() != openCardInfo.getCardType()) {
			return BaseOutput.failure("请使用" + CardType.getName(openCardInfo.getCardType()) + "办理当前业务!");
		}
		return BaseOutput.success();
	}

	/**
	 * 查询开卡费用项
	 */
	@RequestMapping(value = "getOpenCardFee.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<String> getOpenCardFee() {
		// 操作人信息
		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		if (user == null) {
			return BaseOutput.failure("未获取到登录用户信息，请重新登录!");
		}
//		Long openCostFee = openCardService.getOpenCostFee(user.getFirmId());
		Long openCostFee = 1000L;
		return BaseOutput.successData(MoneyUtils.centToYuan(openCostFee));
	}

	/**
	 * 主卡开卡
	 * 
	 * @throws InterruptedException
	 */
	@PostMapping("openMasterCard.action")
	@ResponseBody
	public BaseOutput<?> openMasterCard(@RequestBody OpenCardDto openCardInfo) {
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
	public BaseOutput<?> openSlaveCard(@RequestBody OpenCardDto openCardInfo) throws Exception {
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
		AssertUtils.notEmpty(openCardInfo.getCardNo(), "开卡卡号不能为空!");
		AssertUtils.notEmpty(openCardInfo.getCustomerNo(), "客户编号不能为空!");
		AssertUtils.notNull(openCardInfo.getCustomerId(), "客户ID不能为空!");
		AssertUtils.notEmpty(openCardInfo.getLoginPwd(), "账户密码不能为空!");
	}
}

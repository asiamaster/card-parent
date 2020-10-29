package com.dili.card.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.Constant;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.CustomerResponseDto;
import com.dili.card.dto.OpenCardDto;
import com.dili.card.dto.OpenCardResponseDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.exception.ErrorCode;
import com.dili.card.rpc.AccountQueryRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IBusinessLogService;
import com.dili.card.service.ICardStorageService;
import com.dili.card.service.IOpenCardService;
import com.dili.card.type.CardType;
import com.dili.card.type.CustomerState;
import com.dili.card.type.OperateType;
import com.dili.card.util.AssertUtils;
import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;

/**
 * @description： 开卡服务
 * 
 * @author ：WangBo
 * @time ：2020年6月30日上午11:28:51
 */
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
	@Resource
	IBusinessLogService businessLogService;
	@Autowired
	private DataDictionaryRpc dataDictionaryRpc;

	/**
	 * 根据证件号查询客户信息（C）
	 */
	@RequestMapping(value = "getCustomerInfo.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<CustomerResponseDto> getCustomerInfo(@RequestBody OpenCardDto openCardInfo) {
		log.info("开卡证件号查询客户信息*****{}", JSONObject.toJSONString(openCardInfo));
		// 操作人信息
		UserTicket user = getUserTicket();
		AssertUtils.notEmpty(openCardInfo.getCustomerCertificateNumber(), "请输入证件号!");
		Customer customer = GenericRpcResolver.resolver(
				customerRpc.getByCertificateNumber(openCardInfo.getCustomerCertificateNumber(), user.getFirmId()),
				ServiceName.CUSTOMER);
		CustomerResponseDto response = new CustomerResponseDto();
		if (customer != null) {
			// 判断客户是否已禁用或注销
			if (!customer.getState().equals(CustomerState.VALID.getCode())) {
				throw new CardAppBizException(ResultCode.PARAMS_ERROR,
						"客户已" + CustomerState.getStateName(customer.getState()));
			}
			BeanUtils.copyProperties(customer, response);
			response.setCustomerContactsPhone(customer.getContactsPhone());
			response.setCustomerTypeName(getCustomerTypeName(customer.getCustomerMarket().getType(), user.getFirmId()));
			response.setCustomerType(customer.getCustomerMarket().getType());
		} else {
			return BaseOutput.failure(ErrorCode.CUSTOMER_NOT_EXIST, "未找到客户信息!");
		}
		log.info("开卡证件号查询客户信息完成*****{}", JSONObject.toJSONString(response));
		return BaseOutput.successData(response);
	}

	/**
	 * 根据主卡卡号，查询主卡信息和客户信息（C）
	 */
	@RequestMapping(value = "getAccountInfo.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<UserAccountCardResponseDto> getAccountInfo(@RequestBody OpenCardDto openCardInfo) {
		log.info("开卡查询主卡信息*****{}", JSONObject.toJSONString(openCardInfo));
		AssertUtils.notNull(openCardInfo.getCardNo(), "请输入主卡卡号!");
		UserAccountCardResponseDto userAccount = accountQueryService.getByCardNo(openCardInfo.getCardNo());
		log.info("开卡查询主卡信息完成*****{}", JSONObject.toJSONString(userAccount));
		return BaseOutput.successData(userAccount);
	}

	/**
	 * 检查新卡状态（C）
	 */
	@RequestMapping(value = "checkNewCardNo.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<?> checkNewCardNo(@RequestBody OpenCardDto openCardInfo) {
		log.info("开卡检查新卡状态*****{}", JSONObject.toJSONString(openCardInfo));
		AssertUtils.notNull(openCardInfo.getCardNo(), "请输入卡号!");
		cardStorageService.checkAndGetByCardNo(openCardInfo.getCardNo(), openCardInfo.getCardType(),
				openCardInfo.getCustomerType());
		return BaseOutput.success();
	}

	/**
	 * 查询开卡费用项（C）
	 */
	@RequestMapping(value = "getOpenCardFee.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<Long> getOpenCardFee() {
		log.info("查询开卡费用项");
		Long openCostFee = openCardService.getOpenCostFee();
		log.info("查询开卡费用项*****{}", openCostFee);
		return BaseOutput.successData(openCostFee);
	}

	/**
	 * 主卡开卡（C）
	 * 
	 * @throws InterruptedException
	 */
	@PostMapping("openMasterCard.action")
	@ResponseBody
	public BaseOutput<?> openMasterCard(@RequestBody OpenCardDto openCardInfo) {
		log.info("开卡主卡信息*****{}", JSONObject.toJSONString(openCardInfo));
		// 主要参数校验
		checkMasterParam(openCardInfo);
		// 设置操作人信息
		UserTicket user = getUserTicket();
		// 操作日志
		businessLogService.saveLog(OperateType.ACCOUNT_TRANSACT, user, "客户姓名:" + openCardInfo.getCustomerName(),
				"客户ID:" + openCardInfo.getCustomerCode(), "卡号:" + openCardInfo.getCardNo());
		setOpUser(openCardInfo, user);
		openCardInfo.setCardType(CardType.MASTER.getCode());
		// 开卡
		OpenCardResponseDto response = openCardService.openCard(openCardInfo);
		log.info("开卡主完成*****{}", JSONObject.toJSONString(response));
		return BaseOutput.success("success").setData(response);
	}

	/**
	 * 副卡开卡（C）
	 */
	@PostMapping("openSlaveCard.action")
	@ResponseBody
	public BaseOutput<?> openSlaveCard(@RequestBody OpenCardDto openCardInfo) throws Exception {
		log.info("开副卡信息*****{}", JSONObject.toJSONString(openCardInfo));
		// 主要参数校验
		AssertUtils.notNull(openCardInfo.getParentAccountId(), "主卡信息不能为空!");
		AssertUtils.notEmpty(openCardInfo.getParentLoginPwd(), "主卡密码不能为空!");
		// 设置操作人信息
		UserTicket user = getUserTicket();
		// 操作日志
		businessLogService.saveLog(OperateType.ACCOUNT_TRANSACT, user, "客户姓名:" + openCardInfo.getCustomerName(),
				"客户ID:" + openCardInfo.getCustomerCode(), "卡号:" + openCardInfo.getCardNo());
		setOpUser(openCardInfo, user);
		openCardInfo.setCardType(CardType.SLAVE.getCode());
		OpenCardResponseDto response = openCardService.openCard(openCardInfo);
		log.info("开副卡完成*****{}", JSONObject.toJSONString(response));
		return BaseOutput.success("success").setData(response);
	}

	/**
	 * 设置操作人信息
	 */
	private void setOpUser(OpenCardDto openCardInfo, UserTicket user) {
		openCardInfo.setCreator(user.getRealName());
		openCardInfo.setCreatorId(user.getId());
		openCardInfo.setCreatorCode(user.getUserName());
		openCardInfo.setFirmId(user.getFirmId());
		openCardInfo.setFirmName(user.getFirmName());
	}

	/**
	 * 主卡参数校验
	 * 
	 * @param openCardInfo
	 */
	private void checkMasterParam(OpenCardDto openCardInfo) {
		AssertUtils.notEmpty(openCardInfo.getCustomerName(), "开卡用户名不能为空!");
		AssertUtils.notEmpty(openCardInfo.getCardNo(), "开卡卡号不能为空!");
		AssertUtils.notEmpty(openCardInfo.getCustomerCertificateNumber(), "证件号不能为空!");
		AssertUtils.notEmpty(openCardInfo.getCustomerCode(), "客户编号不能为空!");
		AssertUtils.notNull(openCardInfo.getCustomerId(), "客户ID不能为空!");
		AssertUtils.notEmpty(openCardInfo.getLoginPwd(), "账户密码不能为空!");
	}

	/**
	 * 从数据字典中获取客户身份类型
	 */
	private String getCustomerTypeName(String code, Long firmId) {
		DataDictionaryValue ddv = DTOUtils.newInstance(DataDictionaryValue.class);
		ddv.setDdCode(Constant.CUS_CUSTOMER_TYPE);
		ddv.setCode(code);
		ddv.setFirmId(firmId);
		List<DataDictionaryValue> resolver = GenericRpcResolver.resolver(dataDictionaryRpc.listDataDictionaryValue(ddv),
				"DataDictionaryRpc");
		if (resolver == null || resolver.size() == 0) {
			throw new CardAppBizException("数据字典中没找到该客户类型" + code + "，是否已经删除!");
		}

		return resolver.get(0).getName();
	}
}

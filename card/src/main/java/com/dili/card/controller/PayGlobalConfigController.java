package com.dili.card.controller;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.serializer.EnumTextDisplayAfterFilter;
import com.dili.card.dto.OpenCardDto;
import com.dili.card.dto.pay.PayGlobalConfigDto;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.service.IBusinessLogService;
import com.dili.card.type.OperateType;
import com.dili.card.util.AssertUtils;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

import bsh.StringUtil;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description 支付全局配置
 * @author yangfan
 * @date 2021年3月9日
 */
@Controller
@RequestMapping("/pay")
public class PayGlobalConfigController {
	
	private static final Logger log = LoggerFactory.getLogger(PayGlobalConfigController.class);

	@Autowired
	private PayRpcResolver payRpcResolver; 
	
	@RequestMapping(value="/globalConfig.html", method = RequestMethod.GET)
    public String add(ModelMap modelMap,String marketId) {
    	UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket == null) {
			throw new BusinessException(ResultCode.CSRF_ERROR,"登录已过期!");
		}
		if (StringUtils.isEmpty(marketId)) {
			marketId = String.valueOf(userTicket.getFirmId());
		}
		modelMap.addAttribute("mchId", marketId);
		String json = JSON.toJSONString(payRpcResolver.getPayGlobal(marketId),
				new EnumTextDisplayAfterFilter());
		modelMap.addAttribute("config", JSON.parseObject(json));
		//TODO 根据市场获取配置信息
        return "pay/globalConfig";
    } 
	
	/**
	 * 获取账户限额设置
	 */
	@RequestMapping(value = "/globalConfig.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<?> globalConfig(String mchId) {
		log.info("获取账户限额设置*****{}", JSONObject.toJSONString(mchId));	
		return BaseOutput.successData(payRpcResolver.getPayGlobal(mchId));
	}
	
	/**
	 * 获取账户限额设置
	 */
	@RequestMapping(value = "/setGlobalConfig.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<?> setGlobalConfig(@RequestBody PayGlobalConfigDto payGlobalConfigDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket == null) {
			throw new BusinessException(ResultCode.CSRF_ERROR,"登录已过期!");
		}
		log.info("账户限额设置,操作id{}****{}",userTicket.getId(),JSONObject.toJSONString(payGlobalConfigDto));	
		return BaseOutput.successData(payRpcResolver.setPayGlobal(payGlobalConfigDto));
	}
	
}

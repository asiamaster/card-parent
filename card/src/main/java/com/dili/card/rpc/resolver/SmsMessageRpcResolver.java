package com.dili.card.rpc.resolver;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.util.CardNoUtil;
import com.dili.card.util.DateUtil;
import com.dili.ss.util.MoneyUtils;
import com.diligrp.message.sdk.domain.input.MessageInfoInput;
import com.diligrp.message.sdk.rpc.SmsMessageRpc;

/**
 * @description：短信中心统一处理
 * 
 * @author ：WangBo
 * @time ：2021年1月22日上午11:27:37
 */
@SuppressWarnings("unchecked")
@Component
public class SmsMessageRpcResolver {

	private static final Logger log = LoggerFactory.getLogger(SmsMessageRpcResolver.class);

	private static final String smsDateFormat = "MM月dd日  HH:mm";

	@Autowired
	private SmsMessageRpc smsMessageRpc;

	/**
	 * 存款发送短信
	 */
	@Async
	public void rechargeNotice(String cellphone, String cardNo, TradeResponseDto tradeDto) {
		Object resolver = null;
		MessageInfoInput message = new MessageInfoInput();
		try {
			LocalDateTime opTime = tradeDto.getWhen();
			Long amount = Math.abs(tradeDto.getAmount());
			Long balance = (tradeDto.getBalance() + tradeDto.getAmount())
					- (tradeDto.getFrozenAmount() + tradeDto.getFrozenBalance());

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("tailNumber", CardNoUtil.getTailNumber(cardNo));
			jsonObj.put("MMddhhmm", DateUtil.formatDateTime(opTime, smsDateFormat));
			jsonObj.put("amount", MoneyUtils.centToYuan(amount));
			jsonObj.put("balance", MoneyUtils.centToYuan(balance));

//    		Long firmId = SessionContext.getSessionContext().getUserTicket().getFirmId();
			message.setCellphone(cellphone);
			message.setMarketCode("group"); // 目前配置为集团
			message.setSceneCode("rechargeNotice"); // 应用场景
			message.setParameters(jsonObj.toJSONString());
			message.setSystemCode("card");
//        	message.setTemplateCode(templateCode); // 短信模板ID
			resolver = GenericRpcResolver.resolver(smsMessageRpc.receiveMessage(message), ServiceName.MESSAGE);
			log.info("短信发送成功************{}", cellphone);
		}catch (CardAppBizException e) {
			log.info(JSONObject.toJSONString(message));
			log.error("{}存款短信发送失败",JSONObject.toJSONString(resolver));
		}catch (Exception e) {
			log.info(JSONObject.toJSONString(message));
			log.error("存款短信发送失败",e);
		}
	}

	/**
	 * 取款发送短信
	 */
	@Async
	public void withdrawNotice(String cellphone, String cardNo, TradeResponseDto tradeDto) {
		Object resolver = null;
		MessageInfoInput message = new MessageInfoInput();
		try {
			LocalDateTime opTime = tradeDto.getWhen();
			Long amount = Math.abs(tradeDto.getAmount());
			Long balance = (tradeDto.getBalance() + tradeDto.getAmount())
					- (tradeDto.getFrozenAmount() + tradeDto.getFrozenBalance());

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("tailNumber", CardNoUtil.getTailNumber(cardNo));
			jsonObj.put("MMddhhmm", DateUtil.formatDateTime(opTime, smsDateFormat));
			jsonObj.put("amount", MoneyUtils.centToYuan(amount));
			jsonObj.put("balance", MoneyUtils.centToYuan(balance));

//    		Long firmId = SessionContext.getSessionContext().getUserTicket().getFirmId();
			message.setCellphone(cellphone);
			message.setMarketCode("group"); // 目前配置为集团
			message.setSceneCode("withdrawNotice"); // 应用场景
			message.setParameters(jsonObj.toJSONString());
			message.setSystemCode("card");
//        	message.setTemplateCode(templateCode); // 短信模板ID
			resolver = GenericRpcResolver.resolver(smsMessageRpc.receiveMessage(message), ServiceName.MESSAGE);
		}catch (CardAppBizException e) {
			log.info(JSONObject.toJSONString(message));
			log.error("{}取款短信发送失败",JSONObject.toJSONString(resolver));
		}catch (Exception e) {
			log.info(JSONObject.toJSONString(message));
			log.error("取款短信发送失败",e);
		}
	}
}

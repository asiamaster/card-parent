package com.dili.card.service.impl;

import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dili.card.common.constant.ServiceName;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.IBusinessLogService;
import com.dili.card.type.LogOperationType;
import com.dili.card.type.OperateType;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.rpc.BusinessLogRpc;
import com.dili.uap.sdk.domain.UserTicket;

@Service("businessLogService")
@SuppressWarnings("unchecked")
public class BusinessLogServiceImpl implements IBusinessLogService {

	/** 日志来源,日志服务校验格式*.diligrp.com */
	private static final String LOG_REFERER = "http://card.diligrp.com";

	/** 日志所属系统，UAP中系统管理 - 系统编码 */
	private static final String LOG_SYSTEM_CODE = "CARD";

	private static final Logger LOGGER = LoggerFactory.getLogger(BusinessLogServiceImpl.class);

	@Resource
	BusinessLogRpc businessLogRpc;

	
	@Override
	@Async
	public void saveLog(OperateType operateType, UserTicket userTicket, String... content) {
		try {
			saveLog(operateType, LogOperationType.SUBMIT, userTicket, content);
		} catch (Exception e) {
			LOGGER.error("{}保存操作日志失败,内容【{}】!", operateType.getName(), content);
			LOGGER.error("save log error!", e);
		}
	}

	@Override
	@Async
	public void saveLog(OperateType operateType, LogOperationType logOperationType, UserTicket userTicket,
			String... content) {
		try {
			BusinessLog log = new BusinessLog();
			log.setOperatorId(userTicket.getId());
			log.setOperatorName(userTicket.getRealName());
			log.setBusinessType(operateType.getCode() + "");
			log.setMarketId(userTicket.getFirmId());
			log.setCreateTime(LocalDateTime.now());
			log.setOperationType(logOperationType.getCode());
			if (content != null && content.length != 0) {
				log.setContent(String.join(",", content));
			}
			log.setSystemCode(LOG_SYSTEM_CODE);
			GenericRpcResolver.resolver(businessLogRpc.save(log, LOG_REFERER), ServiceName.LOGGER);
		} catch (Exception e) {
			LOGGER.error("{}保存操作日志失败,内容【{}】!", operateType.getName(), content);
			LOGGER.error("save log error!", e);
		}
	}
}

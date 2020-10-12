package com.dili.card.service.impl;

import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dili.card.service.IBusinessLogService;
import com.dili.card.type.LogOperationType;
import com.dili.card.type.OperateType;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.rpc.BusinessLogRpc;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

@Service("businessLogService")
public class BusinessLogServiceImpl implements IBusinessLogService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BusinessLogServiceImpl.class);

	@Resource
	BusinessLogRpc businessLogRpc;

	@Override
	public void saveLog(OperateType operateType, String content) {
		try {
			SessionContext sessionContext = SessionContext.getSessionContext();
			UserTicket userTicket = sessionContext.getUserTicket();
			if (userTicket != null) {
				BusinessLog log = new BusinessLog();
				log.setOperatorId(userTicket.getId());
				log.setOperatorName(userTicket.getRealName());
				log.setBusinessType(operateType.getCode() + "");
				log.setMarketId(userTicket.getFirmId());
				log.setCreateTime(LocalDateTime.now());
				log.setOperationType(LogOperationType.ADD.getCode());
				businessLogRpc.save(log, "card");
			} else {
				LOGGER.info("保存操作日志失败,无法获取用户信息!");
			}
		} catch (Exception e) {
			LOGGER.error("{}保存操作日志失败,内容【{}】!", operateType.getName(), content);
			LOGGER.error("save log error!", e);
		}
	}

	@Override
	public void saveLog(UserTicket user, OperateType operateType, String content) {
		// TODO Auto-generated method stub

	}

}

package com.dili.card.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSON;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.IBusinessLogService;
import com.dili.card.type.LogOperationType;
import com.dili.card.type.OperateType;
import com.dili.commons.rabbitmq.RabbitMQMessageService;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.logger.sdk.rpc.BusinessLogRpc;
import com.dili.ss.mvc.util.RequestUtils;
import com.dili.uap.sdk.domain.UserTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service("businessLogService")
@SuppressWarnings("unchecked")
public class BusinessLogServiceImpl implements IBusinessLogService {
    @Autowired
    private RabbitMQMessageService rabbitMQMessageService;
    /** 日志来源,日志服务校验格式*.diligrp.com */
    private static final String LOG_REFERER = "http://card.diligrp.com";

    /** 日志所属系统，UAP中系统管理 - 系统编码 */
    private static final String LOG_SYSTEM_CODE = "CARD";

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessLogServiceImpl.class);

    @Resource
    private BusinessLogRpc businessLogRpc;
    @Autowired
    private HttpServletRequest request;


    @Override
    public void saveLog(OperateType operateType, UserTicket userTicket, String... content) {
        try {
            saveLog(operateType, LogOperationType.SUBMIT, userTicket, content);
        } catch (Exception e) {
            LOGGER.error("{}保存操作日志失败,内容【{}】!", operateType.getName(), content);
            LOGGER.error("save log error!", e);
        }
    }

    @Override
    public void saveLog(OperateType operateType, LogOperationType logOperationType, UserTicket userTicket,
                        String... content) {
        String remoteIp = RequestUtils.getIpAddress(request);
        String serverIp = request.getLocalAddr();
        ThreadUtil.execute(() -> {
            try {
                BusinessLog log = new BusinessLog();
                log.setRemoteIp(remoteIp);
                log.setServerIp(serverIp);
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
                rabbitMQMessageService.send(LoggerConstant.MQ_LOGGER_TOPIC_EXCHANGE, LoggerConstant.MQ_LOGGER_ADD_BUSINESS_KEY, JSON.toJSONString(log));
                //GenericRpcResolver.resolver(businessLogRpc.save(log, LOG_REFERER), ServiceName.LOGGER);
            } catch (Exception e) {
                LOGGER.error("{}保存操作日志失败,内容【{}】!", operateType.getName(), content);
                LOGGER.error("save log error!", e);
            }
        });
    }

}

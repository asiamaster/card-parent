package com.dili.card.controller.test;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.OpenCardDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.rpc.AccountQueryRpc;
import com.dili.card.rpc.resolver.SerialRecordRpcResolver;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IBusinessLogService;
import com.dili.card.service.ICardStorageService;
import com.dili.card.service.IOpenCardService;
import com.dili.card.service.ISerialService;
import com.dili.card.service.print.PrintDispatcher;
import com.dili.card.util.AssertUtils;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.ss.domain.BaseOutput;

import cn.hutool.core.util.StrUtil;

/**
 * @description： 开卡服务
 * 
 * @author ：WangBo
 * @time ：2020年6月30日上午11:28:51
 */
@Controller
@RequestMapping(value = "/test")
public class TestController implements IControllerHandler {

	private static final Logger log = LoggerFactory.getLogger(TestController.class);

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
	@Resource
	private SerialRecordRpcResolver serialRecordRpcResolver;
	@Resource
	private ISerialService serialService;
	@Autowired
	private PrintDispatcher printDispatcher;

	@RequestMapping(value = "/business/getPrintData.action")
	@ResponseBody
	public BaseOutput<Map<String, Object>> getPrintData(String serialNo, boolean reprint) {
		log.info("根据操作流水号获取打印数据*****{},->>>>>{}", serialNo, reprint);
		if (StrUtil.isBlank(serialNo)) {
			return BaseOutput.failure("操作流水号为空");
		}
		BusinessRecordDo recordDo = serialService.findBusinessRecordBySerialNo(serialNo);
		if (recordDo == null) {
			return BaseOutput.failure("业务办理记录不存在");
		}
		recordDo.setFirmName(getUserTicket().getFirmName());
		return BaseOutput.successData(printDispatcher.create(recordDo, reprint));
	}

}

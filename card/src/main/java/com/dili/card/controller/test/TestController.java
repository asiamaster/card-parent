package com.dili.card.controller.test;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.dili.assets.sdk.dto.TypeMarketDto;
import com.dili.card.common.constant.Constant;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.service.ISerialService;
import com.dili.card.service.ITypeMarketService;
import com.dili.card.service.print.PrintDispatcher;
import com.dili.card.util.AssertUtils;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;

import cn.hutool.core.util.StrUtil;

/**
 * @description： 测试代码
 *          
 * @author     ：WangBo
 * @time       ：2021年1月18日上午10:12:06
 */
@Controller
@RequestMapping(value = "/api/test")
public class TestController implements IControllerHandler  {
	
	private static final Logger log = LoggerFactory.getLogger(TestController.class);

	@Autowired
	private ITypeMarketService typeMarketService;
	@Resource
    private ISerialService serialService;
    @Autowired
    private PrintDispatcher printDispatcher;
	
	
	/**
	 * http://card.diligrp.com:8386/test/getMarket.action CARD_INCOME ACCOUNT
	 * 测试代码 
	 * @return
	 */
	@RequestMapping(value = "/getMarket.action")
	@ResponseBody
	public BaseOutput<TypeMarketDto> typeMarketRpc() {
		log.info("查询配置的市场帐户信息*****");
		// 操作人信息
		UserTicket user = getUserTicket();
		TypeMarketDto getmarket = typeMarketService.getmarket(Constant.CARD_INCOME_ACCOUNT);
		log.info("查询配置的市场帐户信息*****{}", JSONObject.toJSONString(getmarket));
		AssertUtils.notNull(getmarket.getMarketId(), "沈阳市场需要配置收益账户!");
		return BaseOutput.successData(getmarket);
	}
	
	/**
     * 根据操作流水号获取打印数据
     * @param serialNo
     * @return
     */
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
        Map<String, Object> create = printDispatcher.create(recordDo, reprint);
        log.info("打印数据返回*****->>>>>{}", JSONObject.toJSONString(create));
        return BaseOutput.successData(create);
    }
}

package com.dili.card.controller;

import java.util.Date;
import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.card.schedule.ContractScheduleHandler;
import com.dili.ss.domain.BaseOutput;

import cn.hutool.core.date.DateUtil;

/**
*
* @author miaoguoxin
* @date 2020/9/3
*/
@Controller
@RequestMapping("/api")
public class ScheduleController {

	private static final Logger log = LoggerFactory.getLogger(ContractManagementController.class);

	@Autowired
	private ContractScheduleHandler contractScheduleHandler;

	/**
	 * 更新合同状态
	 */
	@PostMapping("/contract/updateStateTask")
	@ResponseBody
	public BaseOutput<Boolean> updateStateTask() {
		log.info("更新合同状态*****" + DateUtil.format(new Date(), "yyyy-MM-dd hh:mm:ss"));
		contractScheduleHandler.execute();
		return BaseOutput.success();
	}

}

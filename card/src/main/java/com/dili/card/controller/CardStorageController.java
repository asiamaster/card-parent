package com.dili.card.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.CardStorageDto;
import com.dili.card.service.ICardStorageService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;

/**
 * 卡仓库管理
 *
 * @Auther: miaoguoxin
 * @Date: 2020/7/1 16:11
 */
@Controller
@RequestMapping("cardStorage")
public class CardStorageController implements IControllerHandler {

	private static final Logger log = LoggerFactory.getLogger(CardStorageController.class);


	@Autowired
	private ICardStorageService cardStorageService;

	/**
	 * 卡库存页面
	 */
	@GetMapping("cardStorageList.html")
	public String cardStorageListView() {
		return "cardstorage/cardStorageList";
	}

	/**
	 * 卡片库存列表
	 */
	@PostMapping("queryCardStorageList.action")
	@ResponseBody
	public Map<String, Object> queryCardStorageList(CardStorageDto queryDto) {
		log.info("卡片库存列表 *****{}", JSONObject.toJSONString(queryDto));
		buildOperatorInfo(queryDto);
		PageOutput<List<CardStorageDto>> cardStorageList = cardStorageService.cardStorageList(queryDto);
		return successPage(cardStorageList);
	}


	/**
	 * 卡片作废
	 */
	@PostMapping("cardVoid.action")
	@ResponseBody
	public BaseOutput<?> cardVoid(CardStorageDto queryDto) {
		log.info("卡片作废*****" + JSONObject.toJSONString(queryDto));
		cardStorageService.voidCard(queryDto.getCardNo(), queryDto.getNotes());
		return BaseOutput.success();
	}

	/**
	 * 卡片库存列表导出
	 */
	@PostMapping("cardStorageListExport.action")
	@ResponseBody
	public BaseOutput<?> cardStorageListExport(CardStorageDto queryDto) {
		return BaseOutput.success();
	}
}

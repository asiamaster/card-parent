package com.dili.card.controller;

import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.CardStorageDto;
import com.dili.card.dto.CardStorageOutQueryDto;
import com.dili.card.dto.CardStorageOutRequestDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.ICardAddStorageService;
import com.dili.card.service.ICardStorageService;
import com.dili.card.validator.ConstantValidator;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 卡仓库管理
 * 
 * @Auther: miaoguoxin
 * @Date: 2020/7/1 16:11
 */
@Controller
@RequestMapping("cardStorage")
public class CardStorageController implements IControllerHandler {

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
		return successPage(cardStorageService.cardStorageList(queryDto));
	}

	
	/**
	 * 卡片作废
	 */
	@PostMapping("cardVoid.action")
	@ResponseBody
	public BaseOutput<?> cardVoid(CardStorageDto queryDto) {
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

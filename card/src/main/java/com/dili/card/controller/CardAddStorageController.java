package com.dili.card.controller;

import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.ApplyRecordQueryDto;
import com.dili.card.dto.ApplyRecordRequestDto;
import com.dili.card.dto.CardStorageDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.ICardAddStorageService;
import com.dili.card.service.ICardStorageService;
import com.dili.card.type.CardStorageState;
import com.dili.card.type.CardType;
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
 * @description： 卡片入库相关功能
 *
 * @author ：WangBo
 * @time ：2020年7月16日下午2:27:47
 */
@Controller
@RequestMapping("cardStorage/add")
public class CardAddStorageController implements IControllerHandler {

	@Autowired
	private ICardAddStorageService cardAddStorageService;

	/**
	 * 入库列表页
	 *
	 * @return
	 */
	@GetMapping("list.html")
	public String outListView(ModelMap modelMap) {
		modelMap.put("cardTypeList", CardType.getAll());
		modelMap.put("cardStorageStateList", CardStorageState.list());
		return "addStorage/list";
	}

	/**
	*  入库列表接口
	* @date 2020/7/20
	*/
	@PostMapping("queryList.action")
	@ResponseBody
	public Map<String, Object> queryList(CardStorageDto queryDto) {
		return successPage(cardAddStorageService.cardStorageList(queryDto));
	}

	/**
	 * 跳转到入库详情
	 */
	@GetMapping("outDetail.html")
	public String outDetailView(Long id, ModelMap modelMap) {
		if (id == null || id <= 0) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "id不能为空");
		}
//        modelMap.put("detail", cardStorageService.getById(id));
		return "addStorage/outDetail";
	}

	/**
	 * 跳转到添加页面
	 *
	 * @author miaoguoxin
	 * @date 2020/7/2
	 */
	@GetMapping("outAdd.html")
	public String outAddView() {
		return "addStorage/outAdd";
	}

}

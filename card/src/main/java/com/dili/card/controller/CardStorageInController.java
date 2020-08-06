package com.dili.card.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.CardStorageOutQueryDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.StorageInDo;
import com.dili.card.service.ICardStorageInService;
import com.dili.ss.domain.BaseOutput;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * @description： 卡片入库相关功能
 *
 * @author ：WangBo
 * @time ：2020年7月16日下午2:27:47
 */
@Controller
@RequestMapping("cardStorageIn")
public class CardStorageInController implements IControllerHandler {

	@Autowired
	private ICardStorageInService cardStorageInService;

	/**
	 * 批量入库列表页面
	 */
	@GetMapping("/list.html")
	public String outListView() {
		return "cardstorage/inList";
	}
	/**
	 * 采购批量入库页面
	 */
	@GetMapping("/inAdd.html")
	public String addView() {
		return "cardstorage/inAdd";
	}
	/**
	*  批量入库保存
	*/
	@PostMapping("/save.action")
	@ResponseBody
	public BaseOutput<?> save(StorageInDo storageInDo) {
		cardStorageInService.batchCardStorageIn(storageInDo);
		return BaseOutput.success();
	}
	
	/**
	*  批量入库记录查询
	*/
	@PostMapping("queryList.action")
	@ResponseBody
	public Map<String, Object> queryList(CardStorageOutQueryDto queryDto) {
		return successPage(cardStorageInService.list(queryDto));
	}

}

package com.dili.card.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.CardStorageOutQueryDto;
import com.dili.card.entity.StorageInDo;
import com.dili.card.service.ICardStorageInService;
import com.dili.card.service.ICardStorageService;
import com.dili.card.util.AssertUtils;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;

/**
 * @description： 卡片入库相关功能
 *
 * @author ：WangBo
 * @time ：2020年7月16日下午2:27:47
 */
@Controller
@RequestMapping("cardStorageIn")
public class CardStorageInController implements IControllerHandler {

	private static final Logger LOG = LoggerFactory.getLogger(CardStorageInController.class);

	@Autowired
	private ICardStorageInService cardStorageInService;
	@Autowired
	private ICardStorageService cardStorageService;

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
	 * 批量入库保存
	 */
	@PostMapping("/save.action")
	@ResponseBody
	public BaseOutput<?> save(@RequestBody StorageInDo storageInDo) {
		LOG.info("批量入库保存*****" + JSONObject.toJSONString(storageInDo));
		UserTicket userTicket = getUserTicket();
		storageInDo.setCreator(userTicket.getRealName());
		storageInDo.setCreatorId(userTicket.getId());
		storageInDo.setFirmId(userTicket.getFirmId());
		storageInDo.setFirmName(userTicket.getFirmName());
		cardStorageInService.batchCardStorageIn(storageInDo);
		LOG.info("批量入库保存*****END");
		return BaseOutput.success();
	}

	/**
	 * 批量入库记录查询
	 */
	@PostMapping("queryList.action")
	@ResponseBody
	public Map<String, Object> queryList(CardStorageOutQueryDto queryDto) {
		LOG.info("批量入库记录查询*****" + JSONObject.toJSONString(queryDto));
		UserTicket userTicket = getUserTicket();
		queryDto.setFirmId(userTicket.getFirmId());
		return successPage(cardStorageInService.list(queryDto));
	}

	/**
	 * 删除入库记录
	 */
	@PostMapping("del.action")
	@ResponseBody
	public BaseOutput<?> del(Long[] ids) {
		LOG.info("入库记录删除*****" + JSONObject.toJSONString(ids));
		AssertUtils.notEmpty(ids, "参数缺失：ids");
		UserTicket userTicket = getUserTicket();
		cardStorageInService.delStorageIn(ids, userTicket.getFirmId());
		return BaseOutput.success();
	}

	/**
	 * @return
	 */
	@GetMapping("cardFaceIsMust.action")
	@ResponseBody
	public BaseOutput<Boolean> cardFaceIsMust() {
		LOG.info("判断卡面信息是否为必须*****");
		boolean cardFaceIsMust = cardStorageService.cardFaceIsMust();
		return BaseOutput.successData(cardFaceIsMust);
	}
	
}

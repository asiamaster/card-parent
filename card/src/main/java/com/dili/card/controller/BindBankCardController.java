package com.dili.card.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.common.serializer.EnumTextDisplayAfterFilter;
import com.dili.card.dto.BindBankCardDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IBindBankCardService;
import com.dili.card.type.CardType;
import com.dili.card.util.AssertUtils;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.util.SpringUtil;
import com.esotericsoftware.minlog.Log;

@Controller
@RequestMapping(value = "/bindBankCard")
public class BindBankCardController implements IControllerHandler {

	private static final Logger LOG = LoggerFactory.getLogger(BindBankCardController.class);

	@Autowired
	private IAccountQueryService accountQueryService;
	@Autowired
	private IBindBankCardService bindBankCardService;

	/**
	 * 进入绑定首页
	 */
	@GetMapping("/toQueryCard.html")
	public ModelAndView toQueryCard(ModelAndView pageView) {
		pageView.addObject("cardInfo", null);
		pageView.setViewName("bindBankCard/accountInfo");
		return pageView;
	}

	/**
	 * 查询卡信息
	 */
	@GetMapping("/queryCard.html")
	public ModelAndView queryCard(String cardNo, ModelAndView pageView) {
		LOG.info("绑定银行卡查询账户信息*****" + cardNo);
		AssertUtils.notEmpty(cardNo,"卡号不能为空");
		
		UserAccountCardResponseDto cardAccountDto = accountQueryService.getByCardNo(cardNo);
		if(cardAccountDto==null) {
			throw new CardAppBizException("未找到账户或者账户状态异常");
		}
		if(!CardType.isMaster(cardAccountDto.getCardType())) {
			throw new CardAppBizException("请使用主卡绑定银行卡");
		}
		pageView.addObject("cardInfo",
				JSON.parseObject(JSONObject.toJSONString(cardAccountDto, new EnumTextDisplayAfterFilter())));
		pageView.addObject("cardNo", cardNo);
		
		pageView.setViewName("bindBankCard/accountInfo");
		return pageView;
	}
	
	/**
	 * 查询已绑定的银行卡列表
	 */
	@RequestMapping("/queryList.action")
	public PageOutput<List<BindBankCardDto>> bindBankCardList(BindBankCardDto bankCardDto) {
		LOG.info("绑定银行卡查询银行卡列表*****" + JSONObject.toJSONString(bankCardDto));
		PageOutput<List<BindBankCardDto>> list = bindBankCardService.list(bankCardDto);
		return list;
	}
	
	/**
	 *  跳转到绑定银行卡页面
	 */
	@GetMapping("/toAddBankCard.html")
	public String addView() {
		return "bindBankCard/addBankCard";
	}
	
	/**
	 * 添加绑定的银行卡
	 */
	@PostMapping("/addBind.action")
	public BaseOutput<?> addBind(@RequestBody BindBankCardDto bankCardDto) {
		LOG.info("绑定新银行卡*****" + JSONObject.toJSONString(bankCardDto));
		return BaseOutput.success();
	}
	
	/**
	 * 解绑银行卡
	 */
	@PostMapping("/unBind.action")
	public BaseOutput<?> unBind(@RequestBody BindBankCardDto bankCardDto) {
		LOG.info("绑定新银行卡*****" + JSONObject.toJSONString(bankCardDto));
		return BaseOutput.success();
	}
}

package com.dili.card.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.common.serializer.EnumTextDisplayAfterFilter;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.service.IAccountManageService;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.util.AssertUtils;
import com.dili.ss.domain.BaseOutput;

/**
 * 卡账户管理操作
 * @Auther: miaoguoxin
 * @Date: 2020/6/29 14:39
 */
@Controller
@RequestMapping("/account")
public class AccountManagementController implements IControllerHandler{

	@Resource
	private IAccountManageService accountManageService;
	@Resource
	private IAccountQueryService accountQueryService;

	/**
	 * 冻结账户页面
	 */
	@GetMapping("/frozenAccount.html")
	public String frozenAccountView(String cardNo, Long accountId, ModelMap map) {
		AssertUtils.notEmpty(cardNo, "卡号不能为空");
		AssertUtils.notNull(accountId, "账户id不能为空");
		String json = JSON.toJSONString(accountQueryService.getDetail(cardNo, accountId),
				new EnumTextDisplayAfterFilter());
		map.put("detail", JSON.parseObject(json));
		return "accountquery/frozenAccount";
	}

	/**
	 * 解冻账户页面
	 */
	@GetMapping("/unfrozenAccount.html")
	public String unfrozenAccountView(String cardNo,Long accountId, ModelMap map) {
		AssertUtils.notEmpty(cardNo, "卡号不能为空");
		AssertUtils.notNull(accountId, "账户id不能为空");
		String json = JSON.toJSONString(accountQueryService.getDetail(cardNo,accountId),
				new EnumTextDisplayAfterFilter());
		map.put("detail", JSON.parseObject(json));
		return "accountquery/unfrozenAccount";
	}

	/**
	 * 冻结账户
	 */
	@PostMapping("/frozen.action")
	public BaseOutput<String> frozen(@RequestBody CardRequestDto cardRequestDto) {
		buildOperatorInfo(cardRequestDto);
		accountManageService.frozen(cardRequestDto);
		return BaseOutput.success("账户冻结成功");
	}

	/**
	 * 解冻账户
	 */
	@PostMapping("/unfrozen.action")
	public BaseOutput<String> unfrozen(@RequestBody CardRequestDto cardRequestDto) {
		buildOperatorInfo(cardRequestDto);
		accountManageService.unfrozen(cardRequestDto);
		return BaseOutput.success("账户解冻成功");
	}
}

package com.dili.card.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dili.card.dto.CardRequestDto;
import com.dili.card.service.IAccountManageService;

/**
 * 卡账户管理操作
 * @Auther: miaoguoxin
 * @Date: 2020/6/29 14:39
 */
@Controller
@RequestMapping("/account")
public class AccountManagementController {

	@Resource
	private IAccountManageService accountManageService;
	
	/**
	 * 冻结账户页面
	 */
	@GetMapping("/frozenAccount.html")
	public String frozenAccountView(Long accountId, ModelMap map) {
		map.put("accountId", accountId);
		return "accountquery/frozenAccount";
	}
	
	/**
	 * 解冻账户页面
	 */
	@GetMapping("/unfrozenAccount.html")
	public String unfrozenAccountView(Long accountId, ModelMap map) {
		map.put("accountId", accountId);
		return "accountquery/unfrozenAccount";
	}
	
	/**
	 * 冻结账户
	 */
	@GetMapping("/frozenAccount.action")
	public String frozenAccount(CardRequestDto cardRequestDto) {
		accountManageService.frozen(cardRequestDto);
		return "fund/unfrozenFund";
	}
	
	/**
	 * 解冻账户
	 */
	@GetMapping("/unfrozenAccount.action")
	public String unfrozenAccount(CardRequestDto cardRequestDto) {
		accountManageService.unfrozen(cardRequestDto);
		return "fund/unfrozenFund";
	}
}

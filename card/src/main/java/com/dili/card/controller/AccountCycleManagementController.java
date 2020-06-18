package com.dili.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dili.card.service.IAccountCycleService;

/**
 * 账务管理
 */
@RestController
@RequestMapping(value = "/financial")
public class AccountCycleManagementController {
	
	@Autowired
	private IAccountCycleService iAccountCycleService;
	
}

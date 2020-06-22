package com.dili.card.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dili.card.dto.AccountCycleDto;
import com.dili.card.service.IAccountCycleService;
import com.dili.ss.domain.BaseOutput;

/**
 * 账务管理
 */
@RestController
@RequestMapping(value = "/cycle")
public class AccountCycleManagementController {
	
	@Autowired
	private IAccountCycleService iAccountCycleService;
	
	/**
	 * 对账
	 */
	@PostMapping("/checkAccount")
	public BaseOutput<Boolean> save(@RequestBody AccountCycleDto accountCycleDto) {
		iAccountCycleService.checkAccount(accountCycleDto.getId());
		return BaseOutput.success();
	}
	
	/**
	 * 账务列表
	 */
	@PostMapping("/list")
	public BaseOutput<List<AccountCycleDto>> list(@RequestBody AccountCycleDto accountCycleDto) {
		return BaseOutput.successData(iAccountCycleService.list(accountCycleDto));
	}
	
	/**
	 * 账务详情
	 */
	@PostMapping("/detail")
	public BaseOutput<AccountCycleDto> detail(@RequestBody AccountCycleDto accountCycleDto) {
		return BaseOutput.successData(iAccountCycleService.detail(accountCycleDto.getId()));
	}
	
}

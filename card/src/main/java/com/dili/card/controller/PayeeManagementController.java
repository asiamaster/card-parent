package com.dili.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dili.card.dto.UserCashDto;
import com.dili.card.service.IUserCashService;
import com.dili.ss.domain.BaseOutput;

/**
 * 领款管理
 */
@RestController
@RequestMapping(value = "/payee")
public class PayeeManagementController {
	
	@Autowired
	private IUserCashService iUserCashService;
	
	/**
	 * 新增领款
	 */
	@PostMapping("/save")
	public BaseOutput<Boolean> save(@RequestBody UserCashDto userCashDto) {
		iUserCashService.savePayee(userCashDto);
		return BaseOutput.success();
	}
	
	/**
	 * 列表领款
	 */
	@PostMapping("/list")
	public BaseOutput<Boolean> list(@RequestBody UserCashDto userCashDto) {
		iUserCashService.listPayee(userCashDto);
		return BaseOutput.success();
	}
	
	/**
	 * 删除领款
	 */
	@PostMapping("/delete")
	public BaseOutput<Boolean> delete(@RequestBody UserCashDto userCashDto) {
		iUserCashService.delete(userCashDto.getId());
		return BaseOutput.success();
	}
	
}

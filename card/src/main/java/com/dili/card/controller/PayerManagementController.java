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
 * 交款管理
 */
@RestController
@RequestMapping(value = "/payer")
public class PayerManagementController {
	
	@Autowired
	private IUserCashService iUserCashService;
	
	/**
	 * 新增交款
	 */
	@PostMapping("/save")
	public BaseOutput<Boolean> save(@RequestBody UserCashDto userCashDto) {
		iUserCashService.savePayer(userCashDto);
		return BaseOutput.success();
	}
	
	/**
	 * 列表交款
	 */
	@PostMapping("/list")
	public BaseOutput<Boolean> list(@RequestBody UserCashDto userCashDto) {
		iUserCashService.listPayer(userCashDto);
		return BaseOutput.success();
	}
	
	/**
	 * 删除交款
	 */
	@PostMapping("/delete")
	public BaseOutput<Boolean> delete(@RequestBody UserCashDto userCashDto) {
		iUserCashService.delete(userCashDto.getId());
		return BaseOutput.success();
	}
	
	/**
	 * 获取详情
	 */
	@PostMapping("/detail")
	public BaseOutput<UserCashDto> detail(@RequestBody UserCashDto userCashDto) {
		return BaseOutput.successData(iUserCashService.findById(userCashDto.getId()));
	}
	
	/**
	 * 获取详情
	 */
	@PostMapping("/modify")
	public BaseOutput<UserCashDto> modify(@RequestBody UserCashDto userCashDto) {
		iUserCashService.modify(userCashDto);
		return BaseOutput.success();
	}
	
}

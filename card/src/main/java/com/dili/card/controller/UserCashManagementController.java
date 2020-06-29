package com.dili.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dili.card.dto.UserCashDto;
import com.dili.card.service.IUserCashService;
import com.dili.card.validator.ConstantValidator;
import com.dili.ss.domain.BaseOutput;

/**
 * 现金管理
 */
@RestController
@RequestMapping(value = "/cash")
public class UserCashManagementController {
	
	@Autowired
	private IUserCashService iUserCashService;
	
	/**
	 * 新增领款
	 */
	@PostMapping("/savePayee.action")
	public BaseOutput<Boolean> savePayee(@RequestBody @Validated(value = ConstantValidator.Insert.class)UserCashDto userCashDto) {
		iUserCashService.savePayee(userCashDto);
		return BaseOutput.success();
	}
	
	/**
	 * 列表领款
	 */
	@PostMapping("/listPayee.action")
	public BaseOutput<Boolean> listPayee(@RequestBody UserCashDto userCashDto) {
		iUserCashService.listPayee(userCashDto);
		return BaseOutput.success();
	}
	
	/**
	 * 新增交款
	 */
	@PostMapping("/savePayer.action")
	public BaseOutput<Boolean> savePayer(@RequestBody UserCashDto userCashDto) {
		iUserCashService.savePayer(userCashDto);
		return BaseOutput.success();
	}
	
	/**
	 * 列表交款
	 */
	@PostMapping("/listPayer.action")
	public BaseOutput<Boolean> listPayer(@RequestBody UserCashDto userCashDto) {
		iUserCashService.listPayer(userCashDto);
		return BaseOutput.success();
	}
	
	
	/**
	 * 删除领款
	 */
	@PostMapping("/delete.action")
	public BaseOutput<Boolean> delete(@RequestBody UserCashDto userCashDto) {
		iUserCashService.delete(userCashDto.getId());
		return BaseOutput.success();
	}
	
	/**
	 * 获取详情
	 */
	@PostMapping("/detail")
	public BaseOutput<UserCashDto> detail(@RequestBody UserCashDto userCashDto) {
		return BaseOutput.successData(iUserCashService.detail(userCashDto.getId()));
	}
	
	/**
	 * 修改
	 */
	@PostMapping("/modify")
	public BaseOutput<UserCashDto> modify(@RequestBody UserCashDto userCashDto) {
		iUserCashService.modify(userCashDto);
		return BaseOutput.success();
	}
	
}

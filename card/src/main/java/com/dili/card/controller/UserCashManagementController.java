package com.dili.card.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.UserCashDto;
import com.dili.card.service.IUserCashService;
import com.dili.card.type.CashAction;
import com.dili.card.util.CurrencyUtils;
import com.dili.card.validator.ConstantValidator;
import com.dili.ss.domain.BaseOutput;

/**
 * 现金管理
 */
@Controller
@RequestMapping(value = "/cash")
public class UserCashManagementController implements IControllerHandler {

	@Autowired
	private IUserCashService iUserCashService;

	/**
	 * 领款列表页面
	 */
	@GetMapping("/payeeList.html")
	public String payeeList() {
		return "usercash/payeeList";
	}

	/**
	 * 交款列表页面
	 */
	@GetMapping("/payerList.html")
	public String payerList() {
		return "usercash/payerList";
	}

	/**
	 * 领款列表tab
	 */
	@GetMapping("/payeeListTab.html")
	public String payeeListTab() {
		return "usercash/payeeListTab";
	}

	/**
	 * 交款列表页面tab
	 */
	@GetMapping("/payerListTab.html")
	public String payerListTab() {
		return "usercash/payerListTab";
	}

	/**
	 * 领款新增
	 */
	@GetMapping("/addPayee.html")
	public String addPayee(ModelMap modelMap) {
		UserCashDto userCashDto = new UserCashDto();
		userCashDto.setAction(CashAction.PAYEE.getCode());
		modelMap.put("usercash", userCashDto);
		return "usercash/add";
	}

	/**
	 * 取款新增iUserCashService.detail(userCashDto.getId())
	 */
	@GetMapping("/addPayer.html")
	public String addPayer(ModelMap modelMap) {
		UserCashDto userCashDto = new UserCashDto();
		userCashDto.setAction(CashAction.PAYER.getCode());
		modelMap.put("usercash", userCashDto);
		return "usercash/add";
	}

	/**
	 * 修改数据页面
	 */
	@GetMapping("/modify.html")
	public String modify(Long id, ModelMap modelMap) {
		modelMap.put("usercash", iUserCashService.detail(id));
		return "usercash/modify";
	}

	/**
	 * 删除数据页面
	 */
	@GetMapping("/delete.html")
	public String delete(Long id, ModelMap modelMap) {
		modelMap.put("usercash", iUserCashService.detail(id));
		return "usercash/delete";
	}

	/**
	 * 新增领款
	 */
	@PostMapping("/save.action")
	@ResponseBody
	public BaseOutput<Boolean> savePayee(
			@RequestBody @Validated(value = ConstantValidator.Insert.class) UserCashDto userCashDto) {
		iUserCashService.save(userCashDto);
		return BaseOutput.success("新增领款成功");
	}

	/**
	 * 列表领款
	 */
	@PostMapping("/payeeList.action")
	@ResponseBody
	public Map<String, Object> listPayee(@Validated(ConstantValidator.Page.class) UserCashDto userCashDto) {
		Map<String, Object> response = successPage(iUserCashService.listPayee(userCashDto));
		if (userCashDto.getIsStatistic()) {
			response.put("totalAmount", CurrencyUtils
					.toYuanWithStripTrailingZeros(iUserCashService.findTotalAmountByUserId(userCashDto, CashAction.PAYER)));
		}
		return response;
	}

	/**
	 * 列表交款
	 */
	@PostMapping("/payerList.action")
	@ResponseBody
	public Map<String, Object> listPayer(@Validated(ConstantValidator.Page.class) UserCashDto userCashDto) {
		Map<String, Object> response = successPage(iUserCashService.listPayer(userCashDto));
		if (userCashDto.getIsStatistic()) {
			response.put("totalAmount", CurrencyUtils
					.toYuanWithStripTrailingZeros(iUserCashService.findTotalAmountByUserId(userCashDto, CashAction.PAYER)));
		}
		return response;
	}

	/**
	 * 删除领款
	 */
	@PostMapping("/delete.action")
	@ResponseBody
	public BaseOutput<Boolean> delete(@RequestBody @Validated(value = {ConstantValidator.Delete.class}) UserCashDto userCashDto) {
		iUserCashService.delete(userCashDto.getId());
		return BaseOutput.success("删除领款成功");
	}

	/**
	 * 修改
	 */
	@PostMapping("/modify.action")
	@ResponseBody
	public BaseOutput<UserCashDto> modify(
			@RequestBody @Validated(value = {ConstantValidator.Update.class}) UserCashDto userCashDto) {
		iUserCashService.modify(userCashDto);
		return BaseOutput.success("修改领款成功");
	}

}

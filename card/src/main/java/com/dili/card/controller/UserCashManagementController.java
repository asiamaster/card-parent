package com.dili.card.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.common.serializer.EnumTextDisplayAfterFilter;
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
	
	private static final Logger log = LoggerFactory.getLogger(UserCashManagementController.class);

	
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
		modelMap.put("actionText", "领款");
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
		modelMap.put("actionText", "交款");
		return "usercash/add";
	}

	/**
	 * 修改数据页面
	 */
	@GetMapping("/modify.html")
	public String modify(Long id, ModelMap modelMap) {
		 String json = JSON.toJSONString(iUserCashService.detail(id),
	                new EnumTextDisplayAfterFilter());
		 modelMap.put("usercash", JSON.parseObject(json));
		return "usercash/modify";
	}

	/**
	 * 删除数据页面
	 */
	@GetMapping("/delete.html")
	public String delete(Long id, ModelMap modelMap) {
		 String json = JSON.toJSONString(iUserCashService.detail(id),
	                new EnumTextDisplayAfterFilter());
		 modelMap.put("usercash", JSON.parseObject(json));
		return "usercash/delete";
	}

	/**
	 * 新增领款
	 */
	@PostMapping("/save.action")
	@ResponseBody
	public BaseOutput<Boolean> savePayee(
			@RequestBody @Validated(value = ConstantValidator.Insert.class) UserCashDto userCashDto) {
		log.info("新增领款*****{}", JSONObject.toJSONString(userCashDto));
		iUserCashService.save(userCashDto);
		return BaseOutput.success("新增成功");
	}

	/**
	 * 列表领款
	 */
	@PostMapping("/payeeList.action")
	@ResponseBody
	public Map<String, Object> listPayee(@Validated(ConstantValidator.Page.class) UserCashDto userCashDto) {
		log.info("列表领款*****{}", JSONObject.toJSONString(userCashDto));
		return successPage(iUserCashService.listPayee(userCashDto));
	}
	
	/**
	 * 统计数据交款领款总金额
	 */
	@PostMapping("/statistic.action")
	@ResponseBody
	public BaseOutput<String> statistic(@RequestBody UserCashDto userCashDto) {
		log.info("统计数据交款领款总金额*****{}", JSONObject.toJSONString(userCashDto));
		String amount = CurrencyUtils
				.toYuanWithStripTrailingZeros(iUserCashService.findTotalAmountByUserId(userCashDto));
		return BaseOutput.successData(amount == null ? "0" : amount);
	}
	
	
	/**
	 * 列表交款
	 */
	@PostMapping("/payerList.action")
	@ResponseBody
	public Map<String, Object> listPayer(@Validated(ConstantValidator.Page.class) UserCashDto userCashDto) {
		log.info("列表交款*****{}", JSONObject.toJSONString(userCashDto));
		return successPage(iUserCashService.listPayer(userCashDto));
	}

	/**
	 * 删除领款
	 */
	@PostMapping("/delete.action")
	@ResponseBody
	public BaseOutput<Boolean> delete(@RequestBody @Validated(value = {ConstantValidator.Delete.class}) UserCashDto userCashDto) {
		log.info("删除领款*****{}", JSONObject.toJSONString(userCashDto));
		iUserCashService.delete(userCashDto.getId());
		return BaseOutput.success("删除成功");
	}

	/**
	 * 修改
	 */
	@PostMapping("/modify.action")
	@ResponseBody
	public BaseOutput<UserCashDto> modify(
			@RequestBody @Validated(value = {ConstantValidator.Update.class}) UserCashDto userCashDto) {
		log.info("修改领款*****{}", JSONObject.toJSONString(userCashDto));
		iUserCashService.modify(userCashDto);
		return BaseOutput.success("修改成功");
	}

}

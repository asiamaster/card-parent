package com.dili.card.controller;

import java.util.Map;

import javax.annotation.Resource;

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
import com.dili.card.entity.UserCashDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.IBusinessLogService;
import com.dili.card.service.IUserCashService;
import com.dili.card.type.CashAction;
import com.dili.card.type.OperateType;
import com.dili.card.util.AssertUtils;
import com.dili.card.util.CurrencyUtils;
import com.dili.card.validator.ConstantValidator;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.util.MoneyUtils;

/**
 * 现金管理
 */
@Controller
@RequestMapping(value = "/cash")
public class UserCashManagementController implements IControllerHandler {

	private static final Logger log = LoggerFactory.getLogger(UserCashManagementController.class);

	@Autowired
	private IUserCashService iUserCashService;
	@Resource
	IBusinessLogService businessLogService;

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
	public String payeeListTab(Long cycleNo) {
		return "usercash/payeeListTab";
	}

	/**
	 * 交款列表页面tab
	 */
	@GetMapping("/payerListTab.html")
	public String payerListTab(Long cycleNo) {
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
		if (id == null || id < 0L) {
			throw new CardAppBizException(ResultCode.PARAMS_ERROR, "账务周期详情请求参数错误");
		}
		String json = JSON.toJSONString(iUserCashService.detail(id), new EnumTextDisplayAfterFilter());
		modelMap.put("usercash", JSON.parseObject(json));
		return "usercash/modify";
	}

	/**
	 * 删除数据页面
	 */
	@GetMapping("/delete.html")
	public String delete(Long id, ModelMap modelMap) {
		if (id == null || id < 0L) {
			throw new CardAppBizException(ResultCode.PARAMS_ERROR, "账务周期详情请求参数错误");
		}
		String json = JSON.toJSONString(iUserCashService.detailAllState(id), new EnumTextDisplayAfterFilter());
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
		// 操作日志
		OperateType type = OperateType.PAYER;
		if (CashAction.PAYEE.getCode() == userCashDto.getAction()) {
			type = OperateType.PAYEE;
		}
		businessLogService.saveLog(type, getUserTicket(), "领/交款人:" + userCashDto.getUserName(),
				"金额:" + MoneyUtils.centToYuan(userCashDto.getAmount()));
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
	public BaseOutput<Boolean> delete(
			@RequestBody @Validated(value = { ConstantValidator.Delete.class }) UserCashDto userCashDto) {
		log.info("删除领款*****{}", JSONObject.toJSONString(userCashDto));
		iUserCashService.delete(userCashDto.getId());
		return BaseOutput.success("删除成功");
	}

	/**
	 * 详情
	 */
	@GetMapping("/detail.action")
	@ResponseBody
	public BaseOutput<UserCashDo> detail(UserCashDto userCashDto) {
		log.info("详情*****{}", JSONObject.toJSONString(userCashDto));
		AssertUtils.notNull(userCashDto.getId(), "请求参数不能为空");
		return BaseOutput.successData(iUserCashService.getByIdAllState(userCashDto.getId()));
	}

	/**
	 * 修改
	 */
	@PostMapping("/modify.action")
	@ResponseBody
	public BaseOutput<UserCashDto> modify(
			@RequestBody @Validated(value = { ConstantValidator.Update.class }) UserCashDto userCashDto) {
		log.info("修改领款*****{}", JSONObject.toJSONString(userCashDto));
		iUserCashService.modify(userCashDto);
		return BaseOutput.success("修改成功");
	}

}

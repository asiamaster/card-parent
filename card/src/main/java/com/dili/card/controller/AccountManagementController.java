package com.dili.card.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.common.serializer.EnumTextDisplayAfterFilter;
import com.dili.card.dto.AccountPermissionRequestDto;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.IAccountManageService;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IBusinessLogService;
import com.dili.card.type.OperateType;
import com.dili.card.util.AssertUtils;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 卡账户管理操作
 * 
 * @Auther: miaoguoxin
 * @Date: 2020/6/29 14:39
 */
@Controller
@RequestMapping("/account")
public class AccountManagementController implements IControllerHandler {

	private static final Logger log = LoggerFactory.getLogger(AccountManagementController.class);

	@Resource
	private IAccountManageService accountManageService;
	@Resource
	private IAccountQueryService accountQueryService;
	@Resource
	IBusinessLogService businessLogService;

	/**
	 * 冻结账户页面
	 */
	@GetMapping("/frozenAccount.html")
	public String frozenAccountView(Long cardPkId, Long accountPkId, ModelMap map) {
		AssertUtils.notNull(cardPkId, "cardPkId不能为空");
		AssertUtils.notNull(accountPkId, "accountPkId不能为空");

		String json = JSON.toJSONString(accountQueryService.getDetailEx(cardPkId, accountPkId),
				new EnumTextDisplayAfterFilter());
		map.put("detail", JSON.parseObject(json));
		return "accountquery/frozenAccount";
	}

	/**
	 * 解冻账户页面
	 */
	@GetMapping("/unfrozenAccount.html")
	public String unfrozenAccountView(Long cardPkId, Long accountPkId, ModelMap map) {
		AssertUtils.notNull(cardPkId, "cardPkId不能为空");
		AssertUtils.notNull(accountPkId, "accountPkId不能为空");

		String json = JSON.toJSONString(accountQueryService.getDetail(cardPkId, accountPkId),
				new EnumTextDisplayAfterFilter());
		map.put("detail", JSON.parseObject(json));
		return "accountquery/unfrozenAccount";
	}

	/**
	 * 冻结账户
	 */
	@PostMapping("/frozen.action")
	@ResponseBody
	public BaseOutput<String> frozen(@RequestBody CardRequestDto cardRequestDto) {
		log.info("冻结账户*****{}", JSONObject.toJSONString(cardRequestDto));
		// 操作日志
		businessLogService.saveLog(OperateType.FROZEN_ACCOUNT, getUserTicket(), "业务卡号:" + cardRequestDto.getCardNo());
		buildOperatorInfo(cardRequestDto);
		accountManageService.frozen(cardRequestDto);
		return BaseOutput.success("账户冻结成功");
	}

	/**
	 * 解冻账户
	 */
	@PostMapping("/unfrozen.action")
	@ResponseBody
	public BaseOutput<String> unfrozen(@RequestBody CardRequestDto cardRequestDto) {
		log.info("解冻账户*****{}", JSONObject.toJSONString(cardRequestDto));
		// 操作日志
		businessLogService.saveLog(OperateType.UNFROZEN_ACCOUNT, getUserTicket(), "业务卡号:" + cardRequestDto.getCardNo());
		buildOperatorInfo(cardRequestDto);
		accountManageService.unfrozen(cardRequestDto);
		return BaseOutput.success("账户解冻成功");
	}

	/**
	 * 预设置卡账户权限
	 */
	@PostMapping("/presetCardPermission.action")
	@ResponseBody
	public BaseOutput presetPermissionByCard(@RequestParam("cardNo") String cardNo) {
		try {
			return accountQueryService.presetPermissionByCard(cardNo);
		} catch (CardAppBizException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			log.error(String.format("卡[%s]账户权限获取异常:%s", cardNo, e.getMessage()), e);
			return BaseOutput.failure("卡账户权限获取异常");
		}
	}
	/**
	 * 保存卡账户权限
	 */
	@PostMapping("/saveCardPermission.action")
	@ResponseBody
	@BusinessLogger(systemCode = "CARD")
	public BaseOutput saveCardPermission(@Validated @RequestBody AccountPermissionRequestDto requestDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
		}
		try {
			Optional<String> s = accountManageService.saveCardPermission(requestDto);
			if (s.isPresent()) {
				return BaseOutput.failure(s.get());
			}
			return BaseOutput.success();
		} catch (CardAppBizException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			log.error(String.format("卡[%s]账户权限[%s]获取异常:%s", requestDto.getAccountId(), JSONUtil.toJsonStr(requestDto.getPermission()), e.getMessage()), e);
			return BaseOutput.failure("卡账户权限设置异常");
		}
	}

}

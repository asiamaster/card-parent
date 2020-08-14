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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.common.serializer.EnumTextDisplayAfterFilter;
import com.dili.card.dto.AccountCycleDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.type.CashAction;
import com.dili.card.type.CycleState;
import com.dili.card.validator.ConstantValidator;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

/**
 * 账务管理
 */
@Controller
@RequestMapping(value = "/cycle")
public class AccountCycleManagementController implements IControllerHandler {

	@Autowired
	private IAccountCycleService iAccountCycleService;

	/**
	 * 列表页面
	 */
	@GetMapping("/list.html")
	public String listView() {
		return "cycle/list";
	}

	/**
	 * 跳转详情
	 *
	 * @date 2020/7/6
	 */
	@GetMapping("/detail.html")
	public String detailFacadeView(@RequestParam("id") Long id, ModelMap map) {
		if (id == null || id < 0L) {
			throw new CardAppBizException(ResultCode.PARAMS_ERROR, "账务周期详情请求参数错误");
		}
		String json = JSON.toJSONString(iAccountCycleService.detail(id), new EnumTextDisplayAfterFilter());
		map.put("detail", JSON.parseObject(json));
		map.put("settled", CycleState.SETTLED.getCode());
		return "cycle/detail";
	}
	
    /**
     * 跳转到操作流水页面
     * @return
     */
    @RequestMapping(value = "/serialTab.html")
    public String serialTab() {
        return "cycle/serialTab";
    }

	/**
	 * 跳转结账申请
	 *
	 * @date 2020/7/6
	 */
	@GetMapping("/applyDetail.html")
	public String applyDetail(ModelMap map) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		AccountCycleDto accountCycleDto = iAccountCycleService.applyDetail(userTicket.getId());
		String json = JSON.toJSONString(accountCycleDto, new EnumTextDisplayAfterFilter());
		map.put("detail", JSON.parseObject(json));
		map.put("settled", CycleState.ACTIVE.getCode());
		return "cycle/detail";
	}

	/**
	 * 跳转平账页面
	 *
	 * @date 2020/7/6
	 */
	@GetMapping("/flated.html")
	public String flatedHtml(Long id, ModelMap map) {
		if (id == null || id < 0L) {
			throw new CardAppBizException(ResultCode.PARAMS_ERROR, "账务周期对账请求参数错误");
		}
		map.put("detail", iAccountCycleService.findById(id));
		return "cycle/flated";
	}

	/**
	 * 跳转发起交款页面
	 *
	 * @date 2020/7/6
	 */
	@GetMapping("/addPayer.html")
	public String addPayer(Long id, ModelMap map) {
		if (id == null || id < 0L) {
			throw new CardAppBizException(ResultCode.PARAMS_ERROR, "账务周期发起交款请求参数错误");
		}
		map.put("detail", iAccountCycleService.findById(id));
		map.put("action", CashAction.PAYER.getCode());
		return "cycle/addPayer";
	}

	/**
	 * 结账申请对账
	 */
	@PostMapping("/applySettle.action")
	@ResponseBody
	public BaseOutput<Boolean> applySettle(@RequestBody @Validated(value = {ConstantValidator.Update.class}) AccountCycleDto accountCycleDto) {
		iAccountCycleService.settle(accountCycleDto);
		return BaseOutput.success("结账申请成功!");
	}

	/**
	 * 平账
	 */
	@PostMapping("/flated.action")
	@ResponseBody
	public BaseOutput<Boolean> flated(@RequestBody @Validated(value = {ConstantValidator.Default.class}) AccountCycleDto accountCycleDto) {
		iAccountCycleService.flated(accountCycleDto.getId());
		return BaseOutput.success("平账成功！");
	}

	/**
	 * 账务列表
	 */
	@PostMapping("/page.action")
	@ResponseBody
	public Map<String,Object> page(@Validated(ConstantValidator.Page.class) AccountCycleDto accountCycleDto) {
		return successPage(iAccountCycleService.page(accountCycleDto));
	}

	/**
	 * 账务详情
	 */
	@PostMapping("/detail.action")
	@ResponseBody
	public BaseOutput<AccountCycleDto> detail(@RequestBody @Validated(value = {ConstantValidator.Default.class}) AccountCycleDto accountCycleDto) {
		return BaseOutput.successData(iAccountCycleService.detail(accountCycleDto.getId()));
	}
	
	/**
	 * 校验是否存在活跃的账务周期
	 */
	@PostMapping("/checkExistActiveCycle.action")
	@ResponseBody
	public BaseOutput<Boolean> checkExistActiveCycle(@RequestBody @Validated(value = {ConstantValidator.Check.class}) AccountCycleDto accountCycleDto) {
		return BaseOutput.successData(iAccountCycleService.checkExistActiveCycle(accountCycleDto.getUserId()));
	}

}

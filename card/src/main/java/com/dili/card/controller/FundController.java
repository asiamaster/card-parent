package com.dili.card.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.annotation.ForbidDuplicateCommit;
import com.dili.card.common.constant.Constant;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.common.serializer.EnumTextDisplayAfterFilter;
import com.dili.card.dto.FundFrozenRecordParamDto;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.UnfreezeFundDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.UserAccountSingleQueryDto;
import com.dili.card.dto.pay.FreezeFundRecordParam;
import com.dili.card.rpc.resolver.AccountQueryRpcResolver;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IBusinessLogService;
import com.dili.card.service.IFundService;
import com.dili.card.service.IRuleFeeService;
import com.dili.card.service.withdraw.WithdrawDispatcher;
import com.dili.card.type.OperateType;
import com.dili.card.type.PayFreezeFundType;
import com.dili.card.type.RuleFeeBusinessType;
import com.dili.card.type.SystemSubjectType;
import com.dili.card.util.AssertUtils;
import com.dili.card.util.CurrencyUtils;
import com.dili.card.validator.FundValidator;
import com.dili.ss.domain.BaseOutput;
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

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 资金操作相关controller
 *
 * @author xuliang
 */
@Controller
@RequestMapping(value = "/fund")
public class FundController implements IControllerHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(FundController.class);

	@Resource
	private IFundService fundService;
	@Autowired
	private IAccountQueryService accountQueryService;
	@Resource
	private WithdrawDispatcher withdrawDispatcher;
	@Resource
	private IRuleFeeService ruleFeeService;
	@Autowired
	private AccountQueryRpcResolver accountQueryRpcResolver;
	@Resource
	IBusinessLogService businessLogService;

	/**
	 * 跳转冻结资金页面
	 *
	 * @author miaoguoxin
	 * @date 2020/6/29
	 */
	@GetMapping("/frozen.html")
	public String frozenFundView(Long cardPkId, Long accountPkId, ModelMap map) {
		AssertUtils.notNull(cardPkId, "cardPkId不能为空");
		AssertUtils.notNull(accountPkId, "accountPkId不能为空");

		String json = JSON.toJSONString(accountQueryService.getDetail(cardPkId, accountPkId),
				new EnumTextDisplayAfterFilter());
		map.put("detail", JSON.parseObject(json));
		return "fund/frozen";
	}

	/**
	 * 跳转解冻资金页面
	 */
	@GetMapping("/unfrozenFund.html")
	public String unfrozenFundView(Long cardPkId, Long accountPkId, ModelMap map) {
		LOGGER.info("跳转解冻资金页面*****{} --- {}", cardPkId, accountPkId);
		AssertUtils.notNull(cardPkId, "cardPkId不能为空");
		AssertUtils.notNull(accountPkId, "accountPkId不能为空");

		UserAccountSingleQueryDto queryDto = new UserAccountSingleQueryDto();
		queryDto.setAccountPkId(accountPkId);
		queryDto.setCardPkId(cardPkId);
		UserAccountCardResponseDto account = accountQueryRpcResolver.findSingleWithoutValidate(queryDto);
		map.put("account", account);
		return "fund/unfrozenFund";
	}

	/**
	 * 跳转解冻资金modal框
	 * 
	 * @author miaoguoxin
	 * @date 2020/7/31
	 */
	@GetMapping("/unfrozenFundModal.html")
	public String unfrozenFundModalView(String frozenIds, Long accountId, ModelMap map) {
		map.put("frozenIds", frozenIds);
		map.put("accountId", accountId);
		return "fund/unfrozenModal";
	}

	/**
	 * 提现
	 * 
	 * @param fundRequestDto
	 * @return
	 */
	@RequestMapping(value = "/withdraw.action")
	@ResponseBody
	@ForbidDuplicateCommit
	public BaseOutput<String> withdraw(@RequestBody FundRequestDto fundRequestDto) {
		LOGGER.info("提现*****{}", JSONObject.toJSONString(fundRequestDto));
		validateCommonParam(fundRequestDto);
		buildOperatorInfo(fundRequestDto);
		String serialNo = withdrawDispatcher.dispatch(fundRequestDto);
		return BaseOutput.success().setData(serialNo);
	}

	/**
	 * 提现手续费
	 *
	 * @param amount
	 * @return
	 */
	@RequestMapping(value = "/withdrawServiceFee.action")
	@ResponseBody
	public BaseOutput<Long> withdrawServiceFee(Long amount) {
		LOGGER.info("获取提现手续费*****{}", amount);
		if (amount == null || amount < Constant.MIN_AMOUNT || amount > Constant.MAX_AMOUNT) {
			return BaseOutput.failure("请正确输入提现金额");
		}
		BigDecimal decimal = ruleFeeService.getRuleFee(amount, RuleFeeBusinessType.CARD_WITHDRAW_EBANK,
				SystemSubjectType.CARD_WITHDRAW_EBANK_FEE);
		return BaseOutput.success().setData(CurrencyUtils.yuan2Cent(decimal));
	}

	/**
	 * 冻结资金
	 *
	 * @author miaoguoxin
	 * @date 2020/6/30
	 */
	@PostMapping("frozen.action")
	@ResponseBody
	public BaseOutput<?> frozen(@RequestBody @Validated(FundValidator.FrozenFund.class) FundRequestDto requestDto) {
		LOGGER.info("冻结资金*****{}", JSONObject.toJSONString(requestDto));
		AssertUtils.notNull(requestDto.getAmount(), "冻结金额不能为空");
		AssertUtils.isTrue(1L <= requestDto.getAmount() && requestDto.getAmount() <= 999999999L,
				"冻结金额最少0.01元，最多9999999.99元");
//		businessLogService.saveLog(OperateType.FROZEN_FUND, getUserTicket(),
//				"业务卡号:" + requestDto.getCardNo(),
//				"冻结金额:" + requestDto.getAmount());
		this.validateCommonParam(requestDto);
		this.buildOperatorInfo(requestDto);
		fundService.frozen(requestDto);
		return BaseOutput.success();
	}

	/**
	 * 未解冻记录
	 */
	@PostMapping("unfrozenRecord.action")
	@ResponseBody
	public Map<String, Object> unfrozenRecord(FundFrozenRecordParamDto queryParam) {
		LOGGER.info("查询未解冻记录*****{}", JSONObject.toJSONString(queryParam));
		AssertUtils.notNull(queryParam.getFundAccountId(), "参数校验失败：缺少资金账户ID!");
		FreezeFundRecordParam payServiceParam = new FreezeFundRecordParam();
		payServiceParam.setAccountId(queryParam.getFundAccountId());
		payServiceParam.setPageNo(queryParam.getPage());
		payServiceParam.setPageSize(queryParam.getRows());
		// 冻结状态
		payServiceParam.setState(PayFreezeFundType.FREEZE_FUND.getCode());

		DateTime startDate = DateUtil.offset(new Date(), DateField.YEAR, -1);
		payServiceParam.setStartTime(DateUtil.beginOfDay(startDate).toString());
		payServiceParam.setEndTime(DateUtil.endOfDay(new Date()).toString());
		return successPage(fundService.frozenRecord(payServiceParam));
	}

	/**
	 * 冻结和未冻结记录列表
	 * 
	 * @author miaoguoxin
	 * @date 2020/7/31
	 */
	@PostMapping("allRecord.action")
	@ResponseBody
	public Map<String, Object> allRecord(FundFrozenRecordParamDto queryParam) {
		LOGGER.info("查询冻结和未冻结记录列表*****{}", JSONObject.toJSONString(queryParam));
		AssertUtils.notNull(queryParam.getFundAccountId(), "参数校验失败：缺少资金账户ID!");
		FreezeFundRecordParam payServiceParam = new FreezeFundRecordParam();
		payServiceParam.setAccountId(queryParam.getFundAccountId());
		payServiceParam.setPageNo(queryParam.getPage());
		payServiceParam.setPageSize(queryParam.getRows());

		DateTime startDate = DateUtil.offset(new Date(), DateField.YEAR, -1);
		payServiceParam.setStartTime(DateUtil.beginOfDay(startDate).toString());
		payServiceParam.setEndTime(DateUtil.endOfDay(new Date()).toString());
		return successPage(fundService.frozenRecord(payServiceParam));
	}

	/**
	 * 解冻资金
	 */
	@PostMapping("unfrozen.action")
	@ResponseBody
	public BaseOutput<?> unfrozen(@RequestBody UnfreezeFundDto unfreezeFundDto) {
		LOGGER.info("解冻资金*****{}", JSONObject.toJSONString(unfreezeFundDto));
		AssertUtils.notNull(unfreezeFundDto.getAccountId(), "参数校验失败：缺少账户ID!");
		AssertUtils.notNull(unfreezeFundDto.getFrozenIds(), "参数校验失败：缺少冻结ID!");
//		businessLogService.saveLog(OperateType.UNFROZEN_FUND, getUserTicket(),
//				"支付冻结ID:" + unfreezeFundDto.getFrozenId());
		this.buildOperatorInfo(unfreezeFundDto);
		fundService.unfrozen(unfreezeFundDto);
		return BaseOutput.success();
	}

	/**
	 * 充值
	 *
	 * @author miaoguoxin
	 * @date 2020/7/6
	 */
	@PostMapping("recharge.action")
	@ResponseBody
	@ForbidDuplicateCommit
	public BaseOutput<String> recharge(
			@RequestBody @Validated({ FundValidator.Trade.class }) FundRequestDto requestDto) {
		LOGGER.info("充值请求参数:{}", JSON.toJSONString(requestDto));
		this.validateCommonParam(requestDto);
		this.buildOperatorInfo(requestDto);
		long beginTime = System.currentTimeMillis();
		String serialNo = fundService.recharge(requestDto);
		LOGGER.info("充值耗费时间：{}ms", System.currentTimeMillis() - beginTime);
		return BaseOutput.successData(serialNo);
	}

	/**
	 * 获取充值手续费(目前只支持pos)
	 * 
	 * @author miaoguoxin
	 * @date 2020/8/5
	 */
	@GetMapping("rechargeFee.action")
	@ResponseBody
	public BaseOutput<Long> getRechargeFee(Long amount) {
		LOGGER.info("获取充值手续费*****{}", amount);
		AssertUtils.notNull(amount, "金额不能为空");
		BigDecimal ruleFee = ruleFeeService.getRuleFee(amount, RuleFeeBusinessType.CARD_RECHARGE_POS,
				SystemSubjectType.CARD_RECHARGE_POS_FEE);
		return BaseOutput.successData(CurrencyUtils.yuan2Cent(ruleFee));
	}

}

package com.dili.card.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.annotation.ForbidDuplicateCommit;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.common.serializer.EnumTextDisplayAfterFilter;
import com.dili.card.dto.FundFrozenRecordParamDto;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.UnfreezeFundDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.FreezeFundRecordParam;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IFundService;
import com.dili.card.service.IRuleFeeService;
import com.dili.card.service.withdraw.WithdrawDispatcher;
import com.dili.card.type.PayFreezeFundType;
import com.dili.card.type.RuleFeeBusinessType;
import com.dili.card.type.SystemSubjectType;
import com.dili.card.util.AssertUtils;
import com.dili.card.util.CurrencyUtils;
import com.dili.card.validator.ConstantValidator;
import com.dili.card.validator.FundValidator;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import org.apache.commons.lang3.StringUtils;
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

    /**
     * 跳转冻结资金页面
     *
     * @author miaoguoxin
     * @date 2020/6/29
     */
    @GetMapping("/frozen.html")
    public String frozenFundView(String cardNo, Long accountId, ModelMap map) {
        if (StringUtils.isBlank(cardNo)) {
            throw new CardAppBizException(ResultCode.PARAMS_ERROR, "卡号不能为空");
        }
        String json = JSON.toJSONString(accountQueryService.getDetail(cardNo, accountId),
                new EnumTextDisplayAfterFilter());
        map.put("detail", JSON.parseObject(json));
        return "fund/frozen";
    }

    /**
     * 跳转解冻资金页面
     */
    @GetMapping("/unfrozenFund.html")
    public String unfrozenFundView(Long accountId, ModelMap map) {
        UserAccountCardResponseDto account = accountQueryService.getByAccountIdWithoutValidate(accountId);
        map.put("account", account);
        return "fund/unfrozenFund";
    }

    /**
     * 跳转解冻资金modal框
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
     * @param fundRequestDto
     * @return
     */
    @RequestMapping(value = "/withdraw.action")
    @ResponseBody
    @ForbidDuplicateCommit
    public BaseOutput<?> withdraw(@RequestBody FundRequestDto fundRequestDto) {
        validateCommonParam(fundRequestDto);
        buildOperatorInfo(fundRequestDto);
        withdrawDispatcher.dispatch(fundRequestDto);
        return BaseOutput.success();
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
        BigDecimal decimal = ruleFeeService.getRuleFee(amount, RuleFeeBusinessType.CARD_WITHDRAW_EBANK, SystemSubjectType.CARD_WITHDRAW_EBANK_FEE);
        if (decimal != null) {
            return BaseOutput.success().setData(CurrencyUtils.toYuanWithStripTrailingZeros(decimal.longValue()));
        }
        return BaseOutput.failure("未查询到费用");
    }

    /**
     * 冻结资金
     *
     * @author miaoguoxin
     * @date 2020/6/30
     */
    @PostMapping("frozen.action")
    @ResponseBody
    public BaseOutput<?> frozen(@RequestBody @Validated(ConstantValidator.Update.class)
                                        FundRequestDto requestDto) {
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
        //冻结状态
        payServiceParam.setState(PayFreezeFundType.FREEZE_FUND.getCode());

        DateTime startDate = DateUtil.offset(new Date(), DateField.YEAR, -1);
        payServiceParam.setStartTime(DateUtil.beginOfDay(startDate).toString());
        payServiceParam.setEndTime(DateUtil.endOfDay(new Date()).toString());
        return successPage(fundService.frozenRecord(payServiceParam));
    }

    /**
     * 冻结和未冻结记录列表
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
    public BaseOutput<?> recharge(@RequestBody @Validated({FundValidator.Trade.class})
                                          FundRequestDto requestDto) {
        LOGGER.info("充值请求参数:{}", JSON.toJSONString(requestDto));
        this.validateCommonParam(requestDto);
        this.buildOperatorInfo(requestDto);
        fundService.recharge(requestDto);
        return BaseOutput.success();
    }

    /**
     * 获取充值手续费(目前只支持pos)
     * @author miaoguoxin
     * @date 2020/8/5
     */
    @GetMapping("rechargeFee.action")
    @ResponseBody
    public BaseOutput<Long> getRechargeFee(Long amount) {
        AssertUtils.notNull(amount, "金额不能为空");
        BigDecimal ruleFee = ruleFeeService.getRuleFee(amount, RuleFeeBusinessType.CARD_RECHARGE_POS, SystemSubjectType.CARD_RECHARGE_POS_FEE);
        return BaseOutput.successData(ruleFee.longValue());
    }

}

package com.dili.card.controller;

import com.alibaba.fastjson.JSON;
import com.dili.card.common.annotation.ForbidDuplicateCommit;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.common.serializer.EnumTextDisplayAfterFilter;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.UnfreezeFundDto;
import com.dili.card.dto.pay.FreezeFundRecordParam;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IFundService;
import com.dili.card.service.tcc.RechargeTccTransactionManager;
import com.dili.card.service.withdraw.WithdrawDispatcher;
import com.dili.card.util.AssertUtils;
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
    @Autowired
    private RechargeTccTransactionManager rechargeTccTransactionManager;
    @Resource
    private WithdrawDispatcher withdrawDispatcher;


    /**
     * 跳转冻结资金页面
     *
     * @author miaoguoxin
     * @date 2020/6/29
     */
    @GetMapping("/frozen.html")
    public String frozenFundView(String cardNo, ModelMap map) {
        if (StringUtils.isBlank(cardNo)) {
            throw new CardAppBizException(ResultCode.PARAMS_ERROR, "卡号不能为空");
        }
        String json = JSON.toJSONString(accountQueryService.getDetailByCardNo(cardNo),
                new EnumTextDisplayAfterFilter());
        map.put("detail", JSON.parseObject(json));
        return "fund/frozen";
    }

    /**
     * 跳转解冻资金页面
     */
    @GetMapping("/unfrozenFund.html")
    public String unfrozenFundView(Long accountId, ModelMap map) {
        map.put("accountId", accountId);
        return "fund/unfrozenFund";
    }

    /**
     * 提现
     *
     * @param fundRequestDto
     * @return
     */
    @RequestMapping(value = "/withdraw.action")
    @ResponseBody
    public BaseOutput<?> withdraw(@RequestBody FundRequestDto fundRequestDto) {
        validateCommonParam(fundRequestDto);
        buildOperatorInfo(fundRequestDto);
        withdrawDispatcher.dispatch(fundRequestDto);
        return BaseOutput.success();
    }

    /**
     * 提现手续费
     *
     * @param tradeChannel
     * @return
     */
    @RequestMapping(value = "/withdrawServiceFee.action")
    @ResponseBody
    public BaseOutput<Long> withdrawServiceFee(Integer tradeChannel) {
        return BaseOutput.success().setData(3L);
    }

    /**
     * 冻结资金
     *
     * @author miaoguoxin
     * @date 2020/6/30
     */
    @PostMapping("frozen.action")
    @ResponseBody
    public BaseOutput<?> frozen(@RequestBody @Validated(ConstantValidator.Update.class) FundRequestDto requestDto) {
        this.validateCommonParam(requestDto);
        this.buildOperatorInfo(requestDto);
        try {
            fundService.frozen(requestDto);
            return BaseOutput.success();
        } catch (CardAppBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("冻结资金失败", e);
            LOGGER.error("冻结资金请求参数:{}", JSON.toJSONString(requestDto));
            return BaseOutput.failure();
        }
    }

    /**
     * 未解冻记录
     */
    @PostMapping("unfrozenRecord.action")
    @ResponseBody
    public Map<String, Object> unfrozenRecord(FreezeFundRecordParam queryParam) {
//        AssertUtils.notNull(unfreezeFundDto.getAccountId(), "参数校验失败：缺少账户ID!");
        return successPage(fundService.frozenRecord(queryParam));
    }

    /**
     * 解冻资金
     */
    @PostMapping("unfrozen.action")
    @ResponseBody
    public BaseOutput<?> unfrozen(UnfreezeFundDto unfreezeFundDto) {
        AssertUtils.notNull(unfreezeFundDto.getAccountId(), "参数校验失败：缺少账户ID!");
        AssertUtils.notNull(unfreezeFundDto.getTradeNos(), "参数校验失败：缺少冻结ID!");
        buildOperatorInfo(unfreezeFundDto);
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
    public BaseOutput<?> recharge(@RequestBody @Validated({ConstantValidator.Update.class,
            FundValidator.Trade.class}) FundRequestDto requestDto) {
        this.validateCommonParam(requestDto);
        this.buildOperatorInfo(requestDto);
        try {
            rechargeTccTransactionManager.doTcc(requestDto);
        } catch (CardAppBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("充值失败", e);
            LOGGER.error("充值请求参数:{}", JSON.toJSONString(requestDto));
            return BaseOutput.failure();
        }
        return BaseOutput.success();
    }
}

package com.dili.card.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.dili.card.common.annotation.ForbidDuplicateCommit;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.common.serializer.EnumTextDisplayAfterFilter;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IFundService;
import com.dili.card.service.recharge.TradeContextHolder;
import com.dili.card.type.TradeChannel;
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

/**
 * 资金操作相关controller
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


    /**
     * 跳转冻结资金页面
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
     * 提现
     * @param fundRequestDto
     * @return
     */
    @RequestMapping(value = "/withdraw.action")
    @ResponseBody
    public BaseOutput<?> withdraw(@RequestBody FundRequestDto fundRequestDto) {
        try {
            validateCommonParam(fundRequestDto);
            validateWithdrawParam(fundRequestDto);
            buildOperatorInfo(fundRequestDto);
            fundService.withdraw(fundRequestDto);
            return BaseOutput.success();
        } catch (CardAppBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("withdraw", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 冻结资金
     * @author miaoguoxin
     * @date 2020/6/30
     */
    @PostMapping("frozen.action")
    @ResponseBody
    public BaseOutput<?> frozen(@RequestBody @Validated(ConstantValidator.Update.class)
                                        FundRequestDto requestDto) {
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
     * 充值
     * @author miaoguoxin
     * @date 2020/7/6
     */
    @PostMapping("recharge.action")
    @ResponseBody
    @ForbidDuplicateCommit
    public BaseOutput<?> recharge(@RequestBody @Validated({ConstantValidator.Update.class, FundValidator.Trade.class})
                                          FundRequestDto requestDto) {
        this.validateCommonParam(requestDto);
        this.buildOperatorInfo(requestDto);
        //由于需要两阶段提交，所以这里的充值逻辑需要分成两个事务
        UserAccountCardResponseDto userAccount = accountQueryService.getByAccountIdForRecharge(requestDto);
        try {
            //传递线程变量
            TradeContextHolder.putVal(TradeContextHolder.USER_ACCOUNT, userAccount);
            fundService.createRecharge(requestDto);
            fundService.recharge(requestDto);
        } catch (CardAppBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("充值失败", e);
            LOGGER.error("充值请求参数:{}", JSON.toJSONString(requestDto));
            return BaseOutput.failure();
        } finally {
            TradeContextHolder.remove();
        }
        return BaseOutput.success();
    }

    /**
     * 验证提现参数
     * @param fundRequestDto
     */
    private void validateWithdrawParam(FundRequestDto fundRequestDto) {
        if (fundRequestDto.getTradeChannel() == null) {
            throw new CardAppBizException("", "请选择提款方式");
        }
        if (fundRequestDto.getAmount() == null || fundRequestDto.getAmount() <= 0L) {
            throw new CardAppBizException("", "请正确输入提现金额");
        }
        if (StrUtil.isBlank(fundRequestDto.getTradePwd())) {
            throw new CardAppBizException("", "请输入密码");
        }
        TradeChannel tradeChannel = TradeChannel.getByCode(fundRequestDto.getTradeChannel());
        if (tradeChannel == null) {
            throw new CardAppBizException("", "不支持该提款方式");
        }
        switch (tradeChannel) {
            case CASH:
                break;
            case E_BANK:
                if (fundRequestDto.getServiceCost() == null || fundRequestDto.getServiceCost() < 0L) {
                    throw new CardAppBizException("", "请正确输入手续费");
                }
                break;
            default:
                throw new CardAppBizException("", "不支持该提款方式");
        }
    }
}

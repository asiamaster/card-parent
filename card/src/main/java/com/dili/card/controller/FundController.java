package com.dili.card.controller;

import cn.hutool.core.util.StrUtil;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.IFundService;
import com.dili.card.type.TradeChannel;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
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

    /**
     * 提现
     * @param fundRequestDto
     * @return
     */
    @RequestMapping(value = "/withdraw.action")
    @ResponseBody
    public BaseOutput<?> withdraw(FundRequestDto fundRequestDto) {
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
        switch (tradeChannel) {
            case CASH:break;
            case E_BANK:
                if (fundRequestDto.getServiceCost() == null || fundRequestDto.getServiceCost() < 0L) {
                    throw new CardAppBizException("", "请正确输入手续费");
                }
                break;
            default: throw new CardAppBizException("", "不支持该提款方式");
        }
    }
}

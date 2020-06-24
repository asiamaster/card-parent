package com.dili.card.controller;

import cn.hutool.core.util.StrUtil;
import com.dili.card.common.holder.IUserTicketHolder;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.ICardManageService;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 卡片管理服务，退卡、挂失、解挂、补卡等
 * @author ：WangBo
 * @time ：2020年4月28日下午4:04:46
 */
@RestController
@RequestMapping(value = "/card")
public class CardManageController implements IUserTicketHolder {
    private static Logger LOGGER = LoggerFactory.getLogger(CardManageController.class);

	@Resource
	private ICardManageService cardManageService;
	
	   /**
     * 退卡
     */
    @PostMapping("/returnCard.action")
    public BaseOutput<Boolean> returnCard(@RequestBody CardRequestDto cardRequest) {
        cardManageService.returnCard(cardRequest);
        return BaseOutput.success();
    }

    /**
     * 解挂卡片
     */
    @PostMapping("/unLostCard.action")
    public BaseOutput<?> unLostCard(@RequestBody CardRequestDto cardParam) {
        try {
            validateCommonParam(cardParam);
            if (StrUtil.isBlank(cardParam.getLoginPwd())) {
                return BaseOutput.failure("密码为空");
            }
            buildOperatorInfo(cardParam);
            cardManageService.unLostCard(cardParam);
            return BaseOutput.success();
        } catch (CardAppBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("unLostCard", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 构建操作人相关信息
     * @param cardParam
     */
    private void buildOperatorInfo(CardRequestDto cardParam) {
        UserTicket userTicket = getUserTicket();
        cardParam.setOpId(userTicket.getId());
        cardParam.setOpName(userTicket.getRealName());
        cardParam.setOpNo(userTicket.getUserName());
        cardParam.setFirmId(userTicket.getFirmId());
    }

    /**
     * 验证公共参数 针对卡片操作三要素
     * @param cardParam
     */
    private void validateCommonParam(CardRequestDto cardParam) {
        if (cardParam.getAccountId() == null) {
            throw new CardAppBizException("账户ID为空");
        }
        if (cardParam.getCustomerId() == null) {
            throw new CardAppBizException("客户ID为空");
        }
        if (StrUtil.isBlank(cardParam.getCardNo())) {
            throw new CardAppBizException("卡号为空");
        }
    }
}

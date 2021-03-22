package com.dili.card.common.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.dili.card.common.constant.Constant;
import com.dili.card.dto.BaseDto;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.PageOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

import cn.hutool.core.util.StrUtil;

/**
 * 用于获取用户session、验证公共参数、赋值操作员信息
 * @author xuliang
 */
public interface IControllerHandler {

    default Map<String, Object> successPage(PageOutput<?> page) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", ResultCode.OK);
        result.put("rows", page.getData());
        result.put("total", page.getTotal());
        return result;
    }

    /**
     * 获取登录用户信息 如为null则new一个，以免空指针
     * @return
     */
    default UserTicket getUserTicket() {
        SessionContext sessionContext = SessionContext.getSessionContext();
        UserTicket userTicket = sessionContext.getUserTicket();
        if(userTicket == null) {
        	throw new CardAppBizException("无法获取用户信息，请重新登录!");
        }
        return userTicket;
    }

    /**
     * 验证公共字段 针对卡片操作三要素
     * @param cardRequestDto
     */
    default void validateCommonParam(CardRequestDto cardRequestDto) {
        if (cardRequestDto.getAccountId() == null) {
            throw new CardAppBizException("账户ID为空");
        }
        if (cardRequestDto.getCustomerId() == null) {
            throw new CardAppBizException("客户ID为空");
        }
        if (StrUtil.isBlank(cardRequestDto.getCardNo())) {
            throw new CardAppBizException("卡号为空");
        }
    }

    /**
     * 赋值操作员信息
     * @param cardRequestDto
     */
    default void buildOperatorInfo(CardRequestDto cardRequestDto) {
        UserTicket userTicket = getUserTicket();
        cardRequestDto.setOpId(userTicket.getId());
        cardRequestDto.setOpName(userTicket.getRealName());
        cardRequestDto.setOpNo(userTicket.getUserName());
        cardRequestDto.setFirmId(userTicket.getFirmId());
        cardRequestDto.setFirmName(userTicket.getFirmName());
        cardRequestDto.setFirmCode(userTicket.getFirmCode());
    }

    /**
     * 赋值操作员信息
     * @param cardRequestDto
     */
    default void buildOperatorInfo(BaseDto cardRequestDto) {
        UserTicket userTicket = this.getUserTicket();
        cardRequestDto.setOpId(userTicket.getId());
        cardRequestDto.setOpName(userTicket.getRealName());
        cardRequestDto.setOpNo(userTicket.getUserName());
        cardRequestDto.setFirmId(userTicket.getFirmId());
        cardRequestDto.setFirmName(userTicket.getFirmName());
        cardRequestDto.setFirmCode(userTicket.getFirmCode());
    }
}

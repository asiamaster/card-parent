package com.dili.card.common.handler;

import cn.hutool.core.util.StrUtil;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

/**
 * 用于获取用户session、验证公共参数、赋值操作员信息
 * @author xuliang
 */
public interface IControllerHandler {

    /**
     * 获取登录用户信息 如为null则new一个，以免空指针
     * @return
     */
    default UserTicket getUserTicket() {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        return userTicket != null ? userTicket : DTOUtils.newInstance(UserTicket.class);
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
    }
}

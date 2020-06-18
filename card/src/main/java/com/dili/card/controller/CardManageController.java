package com.dili.card.controller;

import cn.hutool.core.util.StrUtil;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.OperatorRequestDto;
import com.dili.card.rpc.CardManageRpc;
import com.dili.card.service.ICardManageService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
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
@RequestMapping(value = "/account")
public class CardManageController {
    private static Logger LOGGER = LoggerFactory.getLogger(CardManageController.class);

	@Resource
	private ICardManageService cardManageService;

    /**
     * 解挂卡片
     */
    @PostMapping("/unLostCard.action")
    public BaseOutput<?> unLostCard(@RequestBody CardRequestDto cardParam) {
        try {
            if (cardParam.getAccountId() == null) {
                return BaseOutput.failure("账户ID为空");
            }
            if (StrUtil.isBlank(cardParam.getLoginPwd())) {
                return BaseOutput.failure("密码为空");
            }
            UserTicket userTicket = getUserTicket();
            cardParam.setOperator(new OperatorRequestDto(userTicket.getId(), userTicket.getRealName(), userTicket.getUserName()));
            cardManageService.unLostCard(cardParam);
            return BaseOutput.success();
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            LOGGER.error("unLostCard", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 获取登录用户信息 如为null则new一个，以免空指针
     * @return
     */
    private UserTicket getUserTicket() {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        return userTicket != null ? userTicket : DTOUtils.newInstance(UserTicket.class);
    }
}

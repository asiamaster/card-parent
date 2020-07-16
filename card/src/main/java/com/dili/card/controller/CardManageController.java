package com.dili.card.controller;

import cn.hutool.core.util.StrUtil;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.ICardManageService;
import com.dili.card.util.AssertUtils;
import com.dili.ss.domain.BaseOutput;
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
public class CardManageController implements IControllerHandler {
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
        validateCommonParam(cardParam);
        if (StrUtil.isBlank(cardParam.getLoginPwd())) {
            return BaseOutput.failure("密码为空");
        }
        buildOperatorInfo(cardParam);
        cardManageService.unLostCard(cardParam);
        return BaseOutput.success();
    }

    /**
     * 解锁卡片
     */
    @PostMapping("/unLockCard.action")
    public BaseOutput<?> unLockCard(@RequestBody CardRequestDto cardParam) {
        validateCommonParam(cardParam);
        if (StrUtil.isBlank(cardParam.getLoginPwd())) {
            return BaseOutput.failure("密码为空");
        }
        buildOperatorInfo(cardParam);
        cardManageService.unLockCard(cardParam);
        return BaseOutput.success();
    }

    /**
     * 换卡
     * @author miaoguoxin
     * @date 2020/7/14
     */
    @PostMapping("/changeCard.action")
    public BaseOutput<?> changeCard(@RequestBody CardRequestDto cardParam) {
        AssertUtils.notEmpty(cardParam.getLoginPwd());
        AssertUtils.notEmpty(cardParam.getNewCardNo());
        this.validateCommonParam(cardParam);
        this.buildOperatorInfo(cardParam);
        cardManageService.changeCard(cardParam);
        return BaseOutput.success();
    }

    /**
     * 挂失
     * @author miaoguoxin
     * @date 2020/7/14
     */
    @PostMapping("/reportLossCard.action")
    public BaseOutput<?> reportLoss(@RequestBody CardRequestDto cardParam) {
        AssertUtils.notEmpty(cardParam.getLoginPwd());
        this.validateCommonParam(cardParam);
        this.buildOperatorInfo(cardParam);
        cardManageService.reportLossCard(cardParam);
        return BaseOutput.success();
    }
}

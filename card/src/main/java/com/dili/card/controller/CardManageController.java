package com.dili.card.controller;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.tcc.ChangeCardTccTransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.service.ICardManageService;
import com.dili.card.util.AssertUtils;
import com.dili.card.validator.CardValidator;
import com.dili.ss.domain.BaseOutput;

import cn.hutool.core.util.StrUtil;

/**
 * 卡片管理服务，退卡、挂失、解挂、补卡等
 * @author ：WangBo
 * @time ：2020年4月28日下午4:04:46
 */
@RestController
@RequestMapping(value = "/card")
public class CardManageController implements IControllerHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CardManageController.class);

    @Resource
    private ICardManageService cardManageService;
    @Autowired
    private ChangeCardTccTransactionManager changeCardTccTransactionManager;

    /**
     * 重置登陆密码
     */
    @RequestMapping(value = "/resetLoginPwd.action", method = RequestMethod.POST)
    public BaseOutput<Boolean> resetLoginPassword(@RequestBody @Validated(value = {CardValidator.Generic.class, CardValidator.ResetPassword.class}) CardRequestDto cardRequest) throws Exception {
    	buildOperatorInfo(cardRequest);
    	cardManageService.resetLoginPwd(cardRequest);
        return BaseOutput.success();
    }

    /**
     * 退卡
     */
    @PostMapping("/returnCard.action")
    public BaseOutput<Boolean> returnCard(@RequestBody @Validated(value = {CardValidator.Generic.class})CardRequestDto cardRequest) {
    	buildOperatorInfo(cardRequest);
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
     * 换卡(C)
     * @author miaoguoxin
     * @date 2020/7/14
     */
    @PostMapping("/changeCard.action")
    public BaseOutput<?> changeCard(@RequestBody CardRequestDto cardParam) {
        AssertUtils.notEmpty(cardParam.getLoginPwd(),"密码不能为空");
        AssertUtils.notEmpty(cardParam.getNewCardNo(),"新开卡号不能为空");
        AssertUtils.notNull(cardParam.getServiceFee(),"工本费不能为空");
        this.validateCommonParam(cardParam);
        this.buildOperatorInfo(cardParam);
        try {
            cardManageService.changeCard(cardParam);
        } catch (CardAppBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("换卡失败", e);
            LOGGER.error("换卡请求参数:{}", JSON.toJSONString(cardParam));
            return BaseOutput.failure();
        }
        return BaseOutput.success();
    }

    /**
     * 挂失(C)
     * @author miaoguoxin
     * @date 2020/7/14
     */
    @PostMapping("/reportLossCard.action")
    public BaseOutput<?> reportLoss(@RequestBody CardRequestDto cardParam) {
        LOGGER.error("挂失请求参数:{}", JSON.toJSONString(cardParam));
        AssertUtils.notEmpty(cardParam.getLoginPwd(),"密码不能为空");
        this.validateCommonParam(cardParam);
        this.buildOperatorInfo(cardParam);
        cardManageService.reportLossCard(cardParam);
        return BaseOutput.success();
    }
}

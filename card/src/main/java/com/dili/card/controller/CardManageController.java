package com.dili.card.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.UserAccountSingleQueryDto;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.ICardManageService;
import com.dili.card.util.AssertUtils;
import com.dili.card.validator.CardValidator;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 卡片管理
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
    private IAccountQueryService accountQueryService;

    /**
     * 重置登陆密码
     */
    @RequestMapping(value = "/resetLoginPwd.action", method = RequestMethod.POST)
    public BaseOutput<Boolean> resetLoginPassword(@RequestBody @Validated(value = {CardValidator.Generic.class, CardValidator.ResetPassword.class}) CardRequestDto cardRequest) throws Exception {
        LOGGER.info("重置登陆密码*****{}", JSONObject.toJSONString(cardRequest));
        buildOperatorInfo(cardRequest);
        cardManageService.resetLoginPwd(cardRequest);
        return BaseOutput.success();
    }

    /**
     * 退卡
     */
    @PostMapping("/returnCard.action")
    public BaseOutput<String> returnCard(@RequestBody @Validated(value = {CardValidator.Generic.class}) CardRequestDto cardRequest) {
        LOGGER.info("退卡*****{}", JSONObject.toJSONString(cardRequest));
        buildOperatorInfo(cardRequest);
        return BaseOutput.successData(cardManageService.returnCard(cardRequest));
    }

    /**
     * 解挂卡片
     */
    @PostMapping("/unLostCard.action")
    public BaseOutput<?> unLostCard(@RequestBody CardRequestDto cardParam) {
        LOGGER.info("解挂卡片*****{}", JSONObject.toJSONString(cardParam));
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
        LOGGER.info("解锁卡片*****{}", JSONObject.toJSONString(cardParam));
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
    public BaseOutput<String> changeCard(@RequestBody CardRequestDto cardParam) {
        LOGGER.info("换卡*****{}", JSONObject.toJSONString(cardParam));
        AssertUtils.notEmpty(cardParam.getLoginPwd(), "密码不能为空");
        AssertUtils.notEmpty(cardParam.getNewCardNo(), "新开卡号不能为空");
        AssertUtils.notNull(cardParam.getServiceFee(), "工本费不能为空");
        this.validateCommonParam(cardParam);
        this.buildOperatorInfo(cardParam);
        return BaseOutput.successData(cardManageService.changeCard(cardParam));
    }

    /**
     * 查询换卡费用项(返回金额：分)
     */
    @GetMapping(value = "/getChangeCardFee.action")
    public BaseOutput<Long> getChangeCardFee() {
        LOGGER.info("查询换卡费用项 *****");
        CardRequestDto cardRequestDto = new CardRequestDto();
        this.buildOperatorInfo(cardRequestDto);
        return BaseOutput.successData(cardManageService.getChangeCardCostFee());
    }

    /**
     * 挂失(C)
     * @author miaoguoxin
     * @date 2020/7/14
     */
    @PostMapping("/reportLossCard.action")
    public BaseOutput<String> reportLoss(@RequestBody CardRequestDto cardParam) {
        LOGGER.info("挂失请求参数:{}", JSON.toJSONString(cardParam));
        AssertUtils.notEmpty(cardParam.getLoginPwd(), "密码不能为空");
        this.validateCommonParam(cardParam);
        this.buildOperatorInfo(cardParam);
        return BaseOutput.successData(cardManageService.reportLossCard(cardParam));
    }

    /**
     * 解挂操作的时候查询(C端)
     * @author miaoguoxin
     * @date 2020/8/6
     */
    @GetMapping("getByCardForUnLost.action")
    public BaseOutput<UserAccountCardResponseDto> getByCardNoForUnLost(String cardNo) {
        LOGGER.info("解挂查询*****{}", cardNo);
        AssertUtils.notEmpty(cardNo, "卡号不能为空");
        UserAccountSingleQueryDto query = new UserAccountSingleQueryDto();
        query.setCardNo(cardNo);
        return BaseOutput.successData(accountQueryService.getForUnLostCard(query));
    }
}

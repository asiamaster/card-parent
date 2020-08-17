package com.dili.card.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.service.ICardManageService;
import com.dili.card.service.tcc.ChangeCardTccTransactionManager;
import com.dili.card.util.AssertUtils;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

/**
 * @description：
 *          换卡页面由C端实现，Controller提供相应的数据查询及业务处理
 * @author ：WangBo
 * @time ：2020年7月27日下午2:35:58
 */
@RestController
@RequestMapping(value = "/card/change")
public class ChangeCardController implements IControllerHandler {

	
	private static final Logger log = LoggerFactory.getLogger(ChangeCardController.class);

    @Resource
    private ICardManageService cardManageService;
    @Autowired
    private ChangeCardTccTransactionManager changeCardTccTransactionManager;



    /**
     * 换卡 C端
     */
    @PostMapping("/changeCard.action")
    public BaseOutput<?> changeCard(@RequestBody CardRequestDto cardParam) {
    	log.info("换卡 *****{}", JSONObject.toJSONString(cardParam));
//        AssertUtils.notEmpty(cardParam.getLoginPwd(),"密码不能为空");
//        AssertUtils.notEmpty(cardParam.getNewCardNo(),"新开卡号不能为空");
//        AssertUtils.notNull(cardParam.getServiceFee(),"工本费不能为空");
//        this.validateCommonParam(cardParam);
//        this.buildOperatorInfo(cardParam);
//        changeCardTccTransactionManager.doTcc(cardParam);
        return BaseOutput.success();
    }

    /**
	 * 查询换卡费用项
	 */
	@RequestMapping(value = "getChangeCardFee.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<String> getChangeCardFee() {
		log.info("查询换卡费用项 *****");
//		// 操作人信息
//		UserTicket user = SessionContext.getSessionContext().getUserTicket();
//		if (user == null) {
//			return BaseOutput.failure("未获取到登录用户信息，请重新登录!");
//		}
////		Long openCostFee = openCardService.getOpenCostFee(user.getFirmId());
		Long openCostFee = 1000L;
		return BaseOutput.successData(MoneyUtils.centToYuan(openCostFee));
	}
}

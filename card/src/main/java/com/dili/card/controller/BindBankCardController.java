package com.dili.card.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.JsonExcludeFilter;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.common.serializer.EnumTextDisplayAfterFilter;
import com.dili.card.dto.BindBankCardDto;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.CardManageRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IBindBankCardService;
import com.dili.card.service.ICustomerService;
import com.dili.card.type.CardType;
import com.dili.card.util.AssertUtils;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;


/**
 * @description： 银行卡绑定功能操作
 *
 * @author ：WangBo
 * @time ：2020年12月3日上午9:42:16
 */
@Controller
@RequestMapping(value = "/bindBankCard")
public class BindBankCardController implements IControllerHandler {

	private static final Logger LOG = LoggerFactory.getLogger(BindBankCardController.class);

	@Autowired
	private IAccountQueryService accountQueryService;
	@Autowired
	private IBindBankCardService bindBankCardService;
	@Resource
	private CardManageRpc cardManageRpc;
	@Resource
	private CustomerRpc customerRpc;
	@Resource
	private ICustomerService customerService;

	/**
	 * 进入绑定首页
	 */
	@GetMapping("/toQueryCard.html")
	public ModelAndView toQueryCard(ModelAndView pageView) {
		pageView.addObject("cardInfo", null);
		pageView.setViewName("bindBankCard/accountInfo");
		return pageView;
	}

	/**
	 * 查询卡信息
	 */
	@GetMapping("/queryCard.html")
	public ModelAndView queryCard(String cardNo, ModelAndView pageView) {
		LOG.info("绑定银行卡查询账户信息*****" + cardNo);
			try {
				
			AssertUtils.notEmpty(cardNo, "卡号不能为空");
	
			UserAccountCardResponseDto account = accountQueryService.getByCardNo(cardNo);
			if (account == null) {
				throw new CardAppBizException("未找到账户或者账户状态异常");
			}
			if (!CardType.isMaster(account.getCardType())) {
				throw new CardAppBizException("请使用主卡绑定银行卡");
			}
			pageView.addObject("cardInfo",
					JSON.parseObject(JSONObject.toJSONString(account, new EnumTextDisplayAfterFilter())));
			pageView.addObject("cardNo", cardNo);
			String subTypeNames = customerService.getSubTypeNames(account.getCustomerId(), account.getFirmId());
			pageView.addObject("subTypeName", subTypeNames);
		} catch (CardAppBizException e) {
			LOG.error("绑定银行卡号查询账户信息出错,", e);
			pageView.addObject("errorMsg", e.getMessage());
		} catch (Exception e) {
			LOG.error("绑定银行卡号查询账户信息出错,", e);
			pageView.addObject("errorMsg", "未知错误");
		}
		pageView.setViewName("bindBankCard/accountInfo");
		return pageView;
	}

	/**
	 * 查询已绑定的银行卡列表
	 */
	@RequestMapping("/queryList.action")
	@ResponseBody
	public Map<String, Object> bindBankCardList(BindBankCardDto bankCardDto) {
		LOG.info("绑定银行卡查询银行卡列表*****" + JSONObject.toJSONString(bankCardDto));
		PageOutput<List<BindBankCardDto>> list = bindBankCardService.list(bankCardDto);
		return successPage(list);
	}

	/**
	 * 跳转到绑定银行卡页面
	 */
	@GetMapping("/toAddBankCard.html")
	public String addView() {
		return "bindBankCard/addBankCard";
	}

	/**
	 * 校验账户密码
	 */
	@PostMapping("/checkPwd.action")
	@ResponseBody
	public BaseOutput<?> checkPwd(@RequestBody CardRequestDto cardParam) {
		LOG.info("绑定新银行卡校验密码*****" + JSONObject.toJSONString(cardParam, JsonExcludeFilter.PWD_FILTER));
		AssertUtils.notNull(cardParam, "没有任何参数");
		AssertUtils.notNull(cardParam.getAccountId(), "账户ID不能为空");
		AssertUtils.notEmpty(cardParam.getLoginPwd(), "密码不能为空");
		GenericRpcResolver.resolver(cardManageRpc.checkPassword(cardParam), ServiceName.ACCOUNT);
		return BaseOutput.success();
	}

	/**
	 * 添加绑定的银行卡
	 */
	@PostMapping("/addBind.action")
	public BaseOutput<?> addBind(@RequestBody BindBankCardDto bankCardDto) {
		LOG.info("绑定新银行卡*****" + JSONObject.toJSONString(bankCardDto));
		return BaseOutput.success();
	}

	/**
	 * 解绑银行卡
	 */
	@PostMapping("/unBind.action")
	public BaseOutput<?> unBind(@RequestBody BindBankCardDto bankCardDto) {
		LOG.info("绑定新银行卡*****" + JSONObject.toJSONString(bankCardDto));
		return BaseOutput.success();
	}
}

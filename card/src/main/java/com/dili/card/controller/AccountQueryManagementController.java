package com.dili.card.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.common.serializer.EnumTextDisplayAfterFilter;
import com.dili.card.dto.AccountListResponseDto;
import com.dili.card.dto.AccountSimpleResponseDto;
import com.dili.card.dto.UserAccountCardQuery;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.UserAccountSingleQueryDto;
import com.dili.card.rpc.resolver.AccountQueryRpcResolver;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.type.CardType;
import com.dili.card.util.AssertUtils;
import com.dili.card.validator.ConstantValidator;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;

/**
 * 卡账户查询
 * @Auther: miaoguoxin
 * @Date: 2020/6/22 10:37
 */
@Controller
@RequestMapping("/accountQuery")
public class AccountQueryManagementController implements IControllerHandler {
    protected static Logger LOGGER = LoggerFactory.getLogger(AccountQueryManagementController.class);
    @Autowired
    private IAccountQueryService accountQueryService;
    @Autowired
    private AccountQueryRpcResolver accountQueryRpcResolver;


    /**
     * 跳转列表页面
     * @author miaoguoxin
     * @date 2020/6/22
     */
    @GetMapping("/list.html")
    public String listView() {
        return "accountquery/list";
    }

    /**
     * 跳转卡详情的tab页签入口
     * @author miaoguoxin
     * @date 2020/6/28
     */
    @GetMapping("/detailTab.html")
    public String detailFacadeView(Long cardPkId, Long accountPkId, ModelMap map) {
        AssertUtils.notNull(cardPkId, "cardPkId不能为空");
        AssertUtils.notNull(accountPkId, "accountPkId不能为空");

        UserAccountSingleQueryDto query = new UserAccountSingleQueryDto();
        query.setAccountPkId(accountPkId);
        query.setCardPkId(cardPkId);
        UserAccountCardResponseDto userAccount = accountQueryRpcResolver.findSingleWithoutValidate(query);
        map.put("isMaster", CardType.isMaster(userAccount.getCardType()));
        map.put("cardState", userAccount.getCardState());
        map.put("disabledState", userAccount.getDisabledState());
        return "accountquery/detailTab";
    }

    /**
     * 跳转卡账户详情页面
     * @author miaoguoxin
     * @date 2020/6/28
     */
    @GetMapping("/accountDetail.html")
    public String accountDetailView(Long cardPkId, Long accountPkId, ModelMap map) {
        AssertUtils.notNull(cardPkId, "cardPkId不能为空");
        AssertUtils.notNull(accountPkId, "accountPkId不能为空");

        String json = JSON.toJSONString(accountQueryService.getDetail(cardPkId, accountPkId),
                new EnumTextDisplayAfterFilter());
        map.put("detail", JSON.parseObject(json));
        return "accountquery/accountDetail";
    }

    /**
     * 分页查询
     * @author miaoguoxin
     * @date 2020/6/22
     */
    @PostMapping("/page.action")
    @ResponseBody
    public Map<String, Object> page(@Validated(ConstantValidator.Page.class)
                                            UserAccountCardQuery param) {
    	LOGGER.info("分页查询卡账户列表*****{}", JSONObject.toJSONString(param));
        this.buildOperatorInfo(param);
        //不要根据客户名字来查询
        param.setCustomerName(null);
        PageOutput<List<AccountListResponseDto>> page = accountQueryService.getPage(param);
        return successPage(page);

    }

    /**
     * 账户信息，包含余额
     * @author miaoguoxin
     * @date 2020/7/7
     */
    @GetMapping("/simpleInfo.action")
    @ResponseBody
    @Deprecated
    public BaseOutput<AccountSimpleResponseDto> getInfoByCardNo(String cardNo) {
        LOGGER.info("simpleInfo.action请求参数:{}",cardNo);
        AssertUtils.notEmpty(cardNo, "卡号不能为空");
        return BaseOutput.successData(accountQueryService.getByCardNoWithBalance(cardNo));
    }


    /**
     * 查询列表(c端)
     * @author miaoguoxin
     * @date 2020/7/28
     */
    @PostMapping("/list.action")
    @ResponseBody
    public BaseOutput<List<UserAccountCardResponseDto>> getList(@RequestBody UserAccountCardQuery param) {
    	LOGGER.info("条件查询卡账户列表*****{}", JSONObject.toJSONString(param));
    	AssertUtils.notNull(param.getFirmId(), "市场id不能为空");
        return BaseOutput.successData(accountQueryService.getList(param));
    }

    /**
     * 单个查询(不校验状态)
     * @author miaoguoxin
     * @date 2020/8/4
     */
    @GetMapping("/singleWithoutValidate.action")
    @ResponseBody
    public BaseOutput<UserAccountCardResponseDto> getSingleWithoutValidate(UserAccountSingleQueryDto query) {
    	LOGGER.info("单个查询getSingleWithoutValidate*****{}", JSONObject.toJSONString(query));
    	return BaseOutput.successData(accountQueryRpcResolver.findSingleWithoutValidate(query));
    }


    /**
     * 单个查询
     * @author miaoguoxin
     * @date 2020/8/4
     */
    @GetMapping("/single.action")
    @ResponseBody
    public BaseOutput<UserAccountCardResponseDto> getSingle(UserAccountSingleQueryDto query) {
    	LOGGER.info("单个查询getSingle*****{}", JSONObject.toJSONString(query));
        UserAccountCardResponseDto userAccountCardResponseDto = accountQueryRpcResolver.findSingle(query);
        return BaseOutput.successData(userAccountCardResponseDto);
    }
}


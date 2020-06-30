package com.dili.card.controller;

import com.alibaba.fastjson.JSON;
import com.dili.card.common.serializer.EnumTextDisplayAfterFilter;
import com.dili.card.dto.AccountListResponseDto;
import com.dili.card.dto.UserAccountCardQuery;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.validator.ConstantValidator;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.PageOutput;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 卡账户查询
 * @Auther: miaoguoxin
 * @Date: 2020/6/22 10:37
 */
@Controller
@RequestMapping("/accountQuery")
public class AccountQueryManagementController {
    @Autowired
    private IAccountQueryService accountQueryService;

    /**
     * 列表页面
     * @param
     * @return
     * @author miaoguoxin
     * @date 2020/6/22
     */
    @GetMapping("/list.html")
    public String listView() {
        return "accountquery/list";
    }

    /**
     * 跳转卡详情的tab页签入口
     * @param
     * @return
     * @author miaoguoxin
     * @date 2020/6/28
     */
    @GetMapping("/detailTab.html")
    public String detailFacadeView() {
        return "accountquery/detailTab";
    }

    /**
     * 跳转卡账户详情页面
     * @author miaoguoxin
     * @date 2020/6/28
     */
    @GetMapping("/accountDetail.html")
    public String accountDetailView(String cardNo, ModelMap map) {
        if (StringUtils.isBlank(cardNo)) {
            throw new CardAppBizException(ResultCode.PARAMS_ERROR, "卡号不能为空");
        }
        String json = JSON.toJSONString(accountQueryService.getDetailByCardNo(cardNo),
                new EnumTextDisplayAfterFilter());
        map.put("detail", JSON.parseObject(json));
        return "accountquery/accountDetail";
    }




    /**
     * 分页查询
     * @param
     * @return
     * @author miaoguoxin
     * @date 2020/6/22
     */
    @PostMapping("/page.action")
    @ResponseBody
    public PageOutput<List<AccountListResponseDto>> page(@RequestBody @Validated(ConstantValidator.Page.class)
                                                                 UserAccountCardQuery param) {
        return accountQueryService.getPage(param);
    }


}


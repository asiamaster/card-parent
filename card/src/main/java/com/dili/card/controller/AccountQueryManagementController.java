package com.dili.card.controller;

import com.dili.card.dto.AccountListResponseDto;
import com.dili.card.dto.UserAccountCardQuery;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.validator.ConstantValidator;
import com.dili.ss.domain.PageOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
     * 分页查询
     * @param
     * @return
     * @author miaoguoxin
     * @date 2020/6/22
     */
    @PostMapping("/page.action")
    @ResponseBody
    public PageOutput<List<AccountListResponseDto>> getPage(@RequestBody @Validated(ConstantValidator.Page.class)
                                                                    UserAccountCardQuery param) {
        return accountQueryService.getPage(param);
    }
}

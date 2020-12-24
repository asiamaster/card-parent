package com.dili.card.controller;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.PipelineRecordQueryDto;
import com.dili.card.dto.pay.PipelineRecordResponseDto;
import com.dili.card.service.IFundService;
import com.dili.card.validator.ConstantValidator;
import com.dili.ss.domain.PageOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/11/27 16:57
 * @Description:
 */
@Controller
@RequestMapping("/bankWithdraw")
public class BankWithdrawController implements IControllerHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(BankWithdrawController.class);

    @Autowired
    private IFundService fundService;

    /**
     * 列表页
     * @author miaoguoxin
     * @date 2020/11/27
     */
    @GetMapping("/list.html")
    public String listView() {
        return "bankwithdraw/list";
    }

    /**
     * 分页查询
     * @author miaoguoxin
     * @date 2020/6/22
     */
    @PostMapping("/page.action")
    @ResponseBody
    public Map<String, Object> page(@Validated(ConstantValidator.Page.class) PipelineRecordQueryDto param) {
        LOGGER.info("分页查询卡账户列表*****{}", JSONObject.toJSONString(param));
        buildOperatorInfo(param);
        PageOutput<List<PipelineRecordResponseDto>> result = fundService.bankWithdrawPage(param);
        return successPage(result);

    }
}

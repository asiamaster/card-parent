package com.dili.card.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.common.serializer.EnumTextDisplayAfterFilter;
import com.dili.card.dto.FirmWithdrawInitResponseDto;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.PipelineRecordQueryDto;
import com.dili.card.dto.pay.PipelineRecordResponseDto;
import com.dili.card.service.IFirmWithdrawService;
import com.dili.card.util.AssertUtils;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.uap.sdk.domain.UserTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @description：市场圈提
 *
 * @author     ：WangBo
 * @time       ：2021年1月14日上午9:43:02
 */
@Controller
@RequestMapping("/firmWithdraw")
public class FirmWithdrawController implements IControllerHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(FirmWithdrawController.class);

    @Autowired
    private IFirmWithdrawService firmWithdrawService;

    /**
     * 初始化圈提页面数据
     */
    @GetMapping("/init.html")
    public String listView(ModelMap modelMap) {
        UserTicket userTicket = getUserTicket();
        FirmWithdrawInitResponseDto result = firmWithdrawService.init(userTicket.getFirmId());
        String json = JSON.toJSONString(result, new EnumTextDisplayAfterFilter());
        modelMap.put("result", JSON.parseObject(json));
        return "firmwithdraw/init";
    }


    /**
     * 市场圈提历史记录
     */
    @PostMapping("/withdrawPage.action")
    @ResponseBody
    public Map<String, Object> page(PipelineRecordQueryDto param) {
        LOGGER.info("分页查询市场圈提历史记录列表*****{}", JSONObject.toJSONString(param));
        AssertUtils.notNull(param.getAccountId(), "市场账号不能为空");
        buildOperatorInfo(param);
        PageOutput<List<PipelineRecordResponseDto>> result = firmWithdrawService.bankWithdrawPage(param);
        return successPage(result);
    }

    /**
     *  市场圈提
     * @author miaoguoxin
     * @date 2021/1/25
     */
    @PostMapping("/withdraw.action")
    @ResponseBody
    public BaseOutput<?> merWithdraw(@RequestBody FundRequestDto requestDto) {
        buildOperatorInfo(requestDto);
        firmWithdrawService.doMerWithdraw(requestDto);
        return BaseOutput.success();
    }
}

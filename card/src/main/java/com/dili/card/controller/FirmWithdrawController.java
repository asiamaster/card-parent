package com.dili.card.controller;

import com.alibaba.fastjson.JSONObject;
import com.dili.assets.sdk.rpc.CityRpc;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.PipelineRecordQueryDto;
import com.dili.card.dto.pay.PipelineRecordResponseDto;
import com.dili.card.service.IFundService;
import com.dili.card.validator.ConstantValidator;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.FirmRpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
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
    private IFundService fundService;
    
    FirmRpc firmRpc;
    /**
     * 初始化圈提页面数据
     */
    @GetMapping("/init.html")
    public String listView() {
    	UserTicket userTicket = getUserTicket();
    	// 查询市场信息
    	BaseOutput<Firm> byId = firmRpc.getById(userTicket.getFirmId());
    	// 查询余额
    	
    	// 查询绑定列表
    	
        return "firmwithdraw/accountInfo";
    }

    /**
     * 市场圈提历史记录
     */
    @PostMapping("/withdraw.action")
    @ResponseBody
    public Map<String, Object> withdraw(@RequestBody PipelineRecordQueryDto param) {
        LOGGER.info("分页查询卡账户列表*****{}", JSONObject.toJSONString(param));
        buildOperatorInfo(param);
        PageOutput<List<PipelineRecordResponseDto>> result = fundService.bankWithdrawPage(param);
        return successPage(result);
    }
    
    /**
     * 市场圈提历史记录
     */
    @RequestMapping("/withdrawPage.action")
    @ResponseBody
    public Map<String, Object> page(PipelineRecordQueryDto param) {
        LOGGER.info("分页查询卡账户列表*****{}", JSONObject.toJSONString(param));
        buildOperatorInfo(param);
        PageOutput<List<PipelineRecordResponseDto>> result = fundService.bankWithdrawPage(param);
        return successPage(result);
    }
}

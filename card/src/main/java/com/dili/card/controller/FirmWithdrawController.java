package com.dili.card.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.JsonExcludeFilter;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.common.serializer.EnumTextDisplayAfterFilter;
import com.dili.card.dto.BindBankCardDto;
import com.dili.card.dto.FirmWithdrawAuthRequestDto;
import com.dili.card.dto.FirmWithdrawInitResponseDto;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.PipelineRecordQueryDto;
import com.dili.card.dto.pay.PipelineRecordResponseDto;
import com.dili.card.entity.bo.MessageBo;
import com.dili.card.service.IBusinessLogService;
import com.dili.card.service.IFirmWithdrawService;
import com.dili.card.type.OperateType;
import com.dili.card.util.AssertUtils;
import com.dili.card.validator.ConstantValidator;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.uap.sdk.domain.UserTicket;
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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
    @Resource
    private IBusinessLogService businessLogService;

    /**
     * 初始化圈提页面数据
     */
    @GetMapping("/init.html")
    public String listView(ModelMap modelMap) {
//        UserTicket userTicket = getUserTicket();
//        FirmWithdrawInitResponseDto result = firmWithdrawService.init(userTicket.getFirmId());
//        String json = JSON.toJSONString(result, new EnumTextDisplayAfterFilter());
//        modelMap.put("result", JSON.parseObject(json));
        return "firmwithdraw/init";
    }

    /**
     * 跳转到绑定银行卡页面
     */
    @GetMapping("/toAddBankCard.html")
    public String addView() {
    	LOGGER.info("为商户绑定新卡");
        return "firmwithdraw/addBankCard";
    }
    
    /**
     * 授权绑定银行卡校验
     */
     @RequestMapping("/getFirmInfo.action")
     @ResponseBody
     public BaseOutput<FirmWithdrawInitResponseDto> getFirmInfo(Long firmId){
    	 LOGGER.info("获取市场详情>"+firmId);
    	 FirmWithdrawInitResponseDto firmInfo = firmWithdrawService.init(firmId);
         return BaseOutput.successData(firmInfo);
     }
    
    
    /**
    * 授权绑定银行卡校验
    * @author miaoguoxin
    * @date 2021/1/26
    */
    @PostMapping("/authCheck.action")
    @ResponseBody
    public BaseOutput<?> checkAuthPassword(@RequestBody @Validated(ConstantValidator.Check.class)
                                                       FirmWithdrawAuthRequestDto requestDto){
    	LOGGER.info("市场密码校验>"+ JSONObject.toJSONString(requestDto, JsonExcludeFilter.PWD_FILTER));
        firmWithdrawService.checkAuth(requestDto);
        return BaseOutput.success();
    }

    
    /**
     * 添加新的银行卡
     */
    @PostMapping("/addBind.action")
    @ResponseBody
    public BaseOutput<?> addBind(@RequestBody BindBankCardDto bankCardDto) {
    	LOGGER.info("市场绑定新银行卡*****" + JSONObject.toJSONString(bankCardDto, JsonExcludeFilter.PWD_FILTER));
        // 设置操作人信息
        UserTicket user = getUserTicket();
        // 操作日志
        businessLogService.saveLog(OperateType.BANKCARD_BIND, user, "客户姓名:" + bankCardDto.getCustomerName(),
                "客户ID:" + bankCardDto.getCustomerCode(), "银行卡号:" + bankCardDto.getBankNo());
        buildOperatorInfo(bankCardDto);
        firmWithdrawService.addBind(bankCardDto);
        return BaseOutput.success();
    }
    

    /**
     * 市场圈提历史记录
     */
    @PostMapping("/withdrawPage.action")
    @ResponseBody
    public Map<String, Object> page(PipelineRecordQueryDto param) {
        LOGGER.info("分页查询市场圈提历史记录列表*****{}", JSONObject.toJSONString(param));
        AssertUtils.notNull(param.getFundAccountId(), "市场账号不能为空"); // 市场帐户没有园区卡账户，只有支付系统有帐户信息
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
    	LOGGER.info("市场圈提*****{}", JSONObject.toJSONString(requestDto, JsonExcludeFilter.PWD_FILTER));
        buildOperatorInfo(requestDto);
        MessageBo<String> messageBo = firmWithdrawService.doMerWithdraw(requestDto);
        BaseOutput<String> result = new BaseOutput<>();
        result.setCode(messageBo.getCode());
        result.setMessage(messageBo.getMessage());
        result.setData(messageBo.getData());
        return result;
    }
}

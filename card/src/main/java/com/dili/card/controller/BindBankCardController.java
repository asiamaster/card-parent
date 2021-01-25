package com.dili.card.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.JsonExcludeFilter;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.common.serializer.EnumTextDisplayAfterFilter;
import com.dili.card.dto.BindBankCardDto;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.PayBankDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.CardManageRpc;
import com.dili.card.rpc.PayRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IBindBankCardService;
import com.dili.card.service.IBusinessLogService;
import com.dili.card.service.ICustomerService;
import com.dili.card.type.CardType;
import com.dili.card.type.OperateType;
import com.dili.card.util.AssertUtils;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.uap.sdk.domain.UserTicket;
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

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private PayRpc payRpc;
    @Resource
    private ICustomerService customerService;
    @Resource
    private IBusinessLogService businessLogService;

    /**
     * 进入绑定首页
     */
    @GetMapping("/toQueryCard.html")
    public ModelAndView toQueryCard(ModelAndView pageView) {
        LOG.info("绑定银行卡查询");
        pageView.addObject("cardInfo", null);
        pageView.setViewName("bindbankcard/accountInfo");
        return pageView;
    }

    /**
     * 跳转到绑定银行卡页面
     */
    @GetMapping("/toAddBankCard.html")
    public String addView() {
        LOG.info("绑定页面");
        return "bindbankcard/addBankCard";
    }


    /**
     * 查询卡信息
     */
    @RequestMapping("/queryCard.action")
    @ResponseBody
    public BaseOutput<Map<String, Object>> queryCard(String cardNo) {
        LOG.info("绑定银行卡查询账户信息*****" + cardNo);
        Map<String, Object> returnData = new HashMap<String, Object>();
        try {
            AssertUtils.notEmpty(cardNo, "卡号不能为空");
            UserAccountCardResponseDto account = accountQueryService.getByCardNo(cardNo);
            AssertUtils.notNull(account, "未找到账户或者账户状态异常");

            if (!CardType.isMaster(account.getCardType())) {
                throw new CardAppBizException("请使用主卡绑定银行卡");
            }
            // 为了使用TextDisplay注解将类型转为显示文字
            String jsonString = JSONObject.toJSONString(account, new EnumTextDisplayAfterFilter());
            returnData.put("cardInfo", JSON.parseObject(jsonString));

            String subTypeNames = customerService.getSubTypeNames(account.getCustomerId(), account.getFirmId());
            returnData.put("subTypeName", subTypeNames);
        } catch (CardAppBizException e) {
            LOG.error("绑定银行卡号查询账户信息出错,", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOG.error("绑定银行卡号查询账户信息出错,", e);
            return BaseOutput.failure("绑定银行卡号查询账户信息出错");
        }
        return BaseOutput.successData(returnData);
    }

    /**
     * 查询已绑定的银行卡列表
     */
    @RequestMapping("/queryList.action")
    @ResponseBody
    public Map<String, Object> bindBankCardList(BindBankCardDto bankCardDto) {
        LOG.info("绑定银行卡查询银行卡分页列表*****" + JSONObject.toJSONString(bankCardDto));
        AssertUtils.notNull(bankCardDto.getRows(), "缺失分页参数：rows");
        AssertUtils.notNull(bankCardDto.getPage(), "缺失分页参数：page");
        PageOutput<List<BindBankCardDto>> list = bindBankCardService.page(bankCardDto);
        return successPage(list);
    }

    /**
     * 列表接口
     * @author miaoguoxin
     * @date 2021/1/13
     */
    @GetMapping("/list.action")
    @ResponseBody
    public BaseOutput<List<BindBankCardDto>> list(BindBankCardDto bankCardDto) {
        LOG.info("绑定银行卡查询银行卡列表*****" + JSONObject.toJSONString(bankCardDto));
        return BaseOutput.successData(bindBankCardService.getList(bankCardDto));
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
        // 用于校验密码次数过多导致卡片锁定后，输入一次正确的还是能执行操作
        accountQueryService.getByAccountId(cardParam.getAccountId());
        GenericRpcResolver.resolver(cardManageRpc.checkPassword(cardParam), ServiceName.ACCOUNT);
        return BaseOutput.success();
    }

    /**
     * 个人根据卡号获取银行名称
     */
    @RequestMapping("/getBankInfo.action")
    @ResponseBody
    public BaseOutput<PayBankDto> getBankInfo(PayBankDto payBankDto) {
        LOG.info("根据卡号获取银行信息*****" + JSONObject.toJSONString(payBankDto));
        PayBankDto data = GenericRpcResolver.resolver(payRpc.getBankInfo(payBankDto), ServiceName.PAY);
        LOG.info("支付返回*****" + JSONObject.toJSONString(data));
//		bindBankCardService.existsBankNo(payBankDto.getCardNo(), payBankDto.get)
        return BaseOutput.successData(data);
    }

    /**
     * 根据关键字搜索完整的开户行名称
     */
    @RequestMapping("/getOpeningBankName.action")
    @ResponseBody
    public BaseOutput<List<PayBankDto>> getOpeningBankName(@RequestBody PayBankDto payBankDto) {
        LOG.info("关键字搜索开户行*****" + JSONObject.toJSONString(payBankDto));
        payBankDto.setBankName(payBankDto.getKeyword());
        List<PayBankDto> data = GenericRpcResolver.resolver(payRpc.searchOpeningBank(payBankDto), ServiceName.PAY);
        LOG.info("支付返回*****" + JSONObject.toJSONString(data));
        return BaseOutput.successData(data);
    }

    /**
     * 查询市场支持的银行渠道
     */
    @RequestMapping("/getBankChannels.action")
    @ResponseBody
    public BaseOutput<List<PayBankDto>> getBankChannels(PayBankDto payBankDto) {
        LOG.info("查询市场支持的银行渠道*****" + JSONObject.toJSONString(payBankDto));
        payBankDto.setMchId(this.getUserTicket().getFirmId());
        List<PayBankDto> data = GenericRpcResolver.resolver(payRpc.getBankChannels(payBankDto), ServiceName.PAY);
        LOG.info("支付返回*****" + JSONObject.toJSONString(data));
        return BaseOutput.successData(data);
    }

    /**
     * 添加绑定的银行卡
     */
    @PostMapping("/addBind.action")
    @ResponseBody
    public BaseOutput<?> addBind(@RequestBody BindBankCardDto bankCardDto) {
        LOG.info("绑定新银行卡*****" + JSONObject.toJSONString(bankCardDto));
        // 设置操作人信息
        UserTicket user = getUserTicket();
        // 操作日志
        businessLogService.saveLog(OperateType.BANKCARD_BIND, user, "客户姓名:" + bankCardDto.getCustomerName(),
                "客户ID:" + bankCardDto.getCustomerCode(), "卡号:" + bankCardDto.getCardNo());
        buildOperatorInfo(bankCardDto);
        bindBankCardService.addBind(bankCardDto);
        return BaseOutput.success();
    }

    /**
     * 解绑银行卡
     */
    @PostMapping("/unBind.action")
    @ResponseBody
    public BaseOutput<?> unBind(@RequestBody BindBankCardDto bankCardDto) {
        LOG.info("解绑银行卡*****" + JSONObject.toJSONString(bankCardDto));
        AssertUtils.notNull(bankCardDto.getAccountId(), "账户ID不能为空");
        AssertUtils.notEmpty(bankCardDto.getLoginPwd(), "密码不能为空");

        // 设置操作人信息
        UserTicket user = getUserTicket();
        // 操作日志
        businessLogService.saveLog(OperateType.BANKCARD_REMOVE, user, "客户姓名:" + bankCardDto.getCustomerName(),
                "客户ID:" + bankCardDto.getCustomerCode(), "卡号:" + bankCardDto.getCardNo());
        bindBankCardService.unBind(bankCardDto);
        return BaseOutput.success();
    }
}

package com.dili.card.controller;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.JsonExcludeFilter;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.CustomerResponseDto;
import com.dili.card.dto.OpenCardDto;
import com.dili.card.dto.OpenCardResponseDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IBusinessLogService;
import com.dili.card.service.ICardStorageService;
import com.dili.card.service.IMiscService;
import com.dili.card.service.IOpenCardService;
import com.dili.card.type.CardType;
import com.dili.card.type.OperateType;
import com.dili.card.util.AssertUtils;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @description： 开卡服务
 *
 * @author ：WangBo
 * @time ：2020年6月30日上午11:28:51
 */
@Controller
@RequestMapping(value = "/card")
public class OpenCardController implements IControllerHandler {

    private static final Logger log = LoggerFactory.getLogger(OpenCardController.class);

    @Resource
    private IOpenCardService openCardService;
    @Resource
    private CustomerRpc customerRpc;
    @Resource
    private IAccountQueryService accountQueryService;
    @Resource
    private ICardStorageService cardStorageService;
    @Resource
    private IBusinessLogService businessLogService;
    @Autowired
    private IMiscService miscService;


    /**
     * 根据证件号查询客户信息（C）
     */
    @RequestMapping(value = "getCustomerInfo.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput<CustomerResponseDto> getCustomerInfo(@RequestBody OpenCardDto openCardInfo) {
        log.info("开卡证件号查询客户信息*****{}", JSONObject.toJSONString(openCardInfo));
        // 操作人信息
        UserTicket user = getUserTicket();
        CustomerResponseDto customerInfo = openCardService
                .getCustomerInfoByCertificateNumber(openCardInfo.getCustomerCertificateNumber(), user.getFirmId());
        // 判断开卡数量
        openCardService.checkCardNum(customerInfo);
        log.info("开卡证件号查询客户信息完成*****{}", JSONObject.toJSONString(customerInfo));
        return BaseOutput.successData(customerInfo);
    }

    /**
     * 根据主卡卡号，查询主卡信息和客户信息（C）
     */
    @RequestMapping(value = "getAccountInfo.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput<UserAccountCardResponseDto> getAccountInfo(@RequestBody OpenCardDto openCardInfo) {
        log.info("开卡查询主卡信息*****{}", JSONObject.toJSONString(openCardInfo));
        AssertUtils.notNull(openCardInfo.getCardNo(), "请输入主卡卡号!");
        UserAccountCardResponseDto userAccount = accountQueryService.getByCardNo(openCardInfo.getCardNo());

        // 客户个人还是企业，用于前端限制持卡人姓名长度
        CustomerExtendDto customer = GenericRpcResolver.resolver(customerRpc.getByCertificateNumber(userAccount.getCustomerCertificateNumber(), userAccount.getFirmId()), ServiceName.CUSTOMER);
        userAccount.setOrganizationType(customer.getOrganizationType());

        log.info("开卡查询主卡信息完成*****{}", JSONObject.toJSONString(userAccount));
        return BaseOutput.successData(userAccount);
    }

    /**
     * 检查新卡状态（C）
     */
    @RequestMapping(value = "checkNewCardNo.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput<?> checkNewCardNo(@RequestBody OpenCardDto openCardInfo) {
        log.info("开卡检查新卡状态*****{}", JSONObject.toJSONString(openCardInfo));
        AssertUtils.notNull(openCardInfo.getCardNo(), "请输入卡号!");
        cardStorageService.checkAndGetByCardNo(openCardInfo.getCardNo(), openCardInfo.getCardType(),
                openCardInfo.getCustomerId());
        return BaseOutput.success();
    }

    /**
     * 查询开卡费用项（C）
     */
    @RequestMapping(value = "getOpenCardFee.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput<Long> getOpenCardFee(HttpServletRequest request) {
        log.info("查询开卡费用项");
        Long openCostFee = openCardService.getOpenCostFee();
        log.info("查询开卡费用项*****{}", openCostFee);

        return BaseOutput.successData(openCostFee);
    }

    /**
     * 主卡开卡（C）
     *
     * @throws InterruptedException
     */
    @PostMapping("openMasterCard.action")
    @ResponseBody
    public BaseOutput<?> openMasterCard(@RequestBody OpenCardDto openCardInfo, HttpServletRequest request) {
        log.info("开卡主卡信息*****{}", JSONObject.toJSONString(openCardInfo, JsonExcludeFilter.PWD_FILTER));
        UserTicket user = getUserTicket();
        // 主要参数校验
        checkMasterParam(openCardInfo);

        // 判断主卡开卡数量
        CustomerResponseDto customerInfo = new CustomerResponseDto();
        customerInfo.setId(openCardInfo.getCustomerId());
        customerInfo.setFirmId(openCardInfo.getFirmId());
        openCardService.checkCardNum(customerInfo);

        // 设置有主子商户的市场的收益帐户,feign调用支付接口时通过PayServiceFeignConfig将该值替换原mchId
        miscService.setSubMarketIdToRequest(user.getFirmId(), openCardInfo.getCostFee());
        // 操作日志
        businessLogService.saveLog(OperateType.ACCOUNT_TRANSACT, user, "客户姓名:" + openCardInfo.getCustomerName(),
                "客户ID:" + openCardInfo.getCustomerCode(), "卡号:" + openCardInfo.getCardNo());
        setOpUser(openCardInfo, user);
        openCardInfo.setCardType(CardType.MASTER.getCode());
        // 开卡
        OpenCardResponseDto response = openCardService.openCard(openCardInfo);
        log.info("开卡主完成*****{}", JSONObject.toJSONString(response));
        return BaseOutput.success("success").setData(response);
    }

    /**
     * 副卡开卡（C）
     */
    @PostMapping("openSlaveCard.action")
    @ResponseBody
    public BaseOutput<?> openSlaveCard(@RequestBody OpenCardDto openCardInfo, HttpServletRequest request) throws Exception {
        log.info("开副卡信息*****{}", JSONObject.toJSONString(openCardInfo, JsonExcludeFilter.PWD_FILTER));
        UserTicket user = getUserTicket();
        // 主要参数校验
        AssertUtils.notNull(openCardInfo.getParentAccountId(), "主卡信息不能为空!");
        AssertUtils.notEmpty(openCardInfo.getParentLoginPwd(), "主卡密码不能为空!");
        // 设置有主子商户的市场的收益帐户,feign调用支付接口时通过PayServiceFeignConfig将该值替换原mchId
        miscService.setSubMarketIdToRequest(user.getFirmId(), openCardInfo.getCostFee());

        // 操作日志
        businessLogService.saveLog(OperateType.ACCOUNT_TRANSACT, user, "客户姓名:" + openCardInfo.getCustomerName(),
                "客户ID:" + openCardInfo.getCustomerCode(), "卡号:" + openCardInfo.getCardNo());
        setOpUser(openCardInfo, user);
        openCardInfo.setCardType(CardType.SLAVE.getCode());
        OpenCardResponseDto response = openCardService.openCard(openCardInfo);
        log.info("开副卡完成*****{}", JSONObject.toJSONString(response));
        return BaseOutput.success("success").setData(response);
    }

    /**
     * 设置操作人信息
     */
    private void setOpUser(OpenCardDto openCardInfo, UserTicket user) {
        openCardInfo.setCreator(user.getRealName());
        openCardInfo.setCreatorId(user.getId());
        openCardInfo.setCreatorCode(user.getUserName());
        openCardInfo.setFirmId(user.getFirmId());
        openCardInfo.setFirmName(user.getFirmName());
        openCardInfo.setFirmCode(user.getFirmCode());
    }


    /**
     * 主卡参数校验
     *
     * @param openCardInfo
     */
    private void checkMasterParam(OpenCardDto openCardInfo) {
        AssertUtils.notEmpty(openCardInfo.getCustomerName(), "开卡用户名不能为空!");
        AssertUtils.notEmpty(openCardInfo.getCardNo(), "开卡卡号不能为空!");
        AssertUtils.notEmpty(openCardInfo.getCustomerCertificateNumber(), "证件号不能为空!");
        AssertUtils.notEmpty(openCardInfo.getCustomerCode(), "客户编号不能为空!");
        AssertUtils.notNull(openCardInfo.getCustomerId(), "客户ID不能为空!");
        AssertUtils.notEmpty(openCardInfo.getLoginPwd(), "账户密码不能为空!");
    }

}

package com.dili.card.controller;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.common.serializer.EnumTextDisplayAfterFilter;
import com.dili.card.dto.AccountCycleDetailDto;
import com.dili.card.dto.AccountCycleDto;
import com.dili.card.dto.AccountCyclePrintlDto;
import com.dili.card.dto.AccountSettlePrintPrintlDto;
import com.dili.card.entity.AccountCycleDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.type.CashAction;
import com.dili.card.type.CycleState;
import com.dili.card.util.CurrencyUtils;
import com.dili.card.validator.ConstantValidator;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.FirmRpc;
import com.dili.uap.sdk.session.SessionContext;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 账务管理
 */
@Controller
@RequestMapping(value = "/cycle")
public class AccountCycleManagementController implements IControllerHandler {

    private static final Logger log = LoggerFactory.getLogger(AccountCycleManagementController.class);


    @Autowired
    private IAccountCycleService iAccountCycleService;
    @Autowired
    private FirmRpc firmRpc;

    /**
     * 列表页面
     */
    @GetMapping("/list.html")
    public String listView(ModelMap map) {
        map.put("state", CycleState.SETTLED.getCode());
        return "cycle/list";
    }

    /**
     * 跳转详情
     *
     * @date 2020/7/6
     */
    @GetMapping("/detail.html")
    public String detailFacadeView(@RequestParam("id") Long id, ModelMap map) {
        log.info("财务对帐详情detail>{}", id);
        if (id == null || id < 0L) {
            throw new CardAppBizException(ResultCode.PARAMS_ERROR, "账务周期详情请求参数错误");
        }
        AccountCycleDto detail = iAccountCycleService.detail(id);
        String json = JSON.toJSONString(detail, new EnumTextDisplayAfterFilter());
        log.info("财务对帐详情detail数据>{}", json);
        map.put("detail", JSON.parseObject(json));
        map.put("settled", CycleState.SETTLED.getCode());
        map.put("showSettlePrintBtn", "false");
        map.put("showPrintBtn", "false");
        if (detail.getState() == CycleState.FLATED.getCode()) {
//			map.put("showPrintBtn", "true");
            map.put("showSettlePrintBtn", "true");
        }
        return "cycle/detail";
    }

    /**
     * 跳转到操作流水页面
     * @return
     */
    @RequestMapping(value = "/serialTab.html")
    public String serialTab() {
        return "cycle/serialTab";
    }

    /**
     * 跳转结账申请
     *
     * @date 2020/7/6
     */
    @GetMapping("/applyDetail.html")
    public String applyDetail(ModelMap map) {
        log.info("柜员结帐详情applyDetail>{}", map);
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        AccountCycleDto accountCycleDto = iAccountCycleService.applyDetail(userTicket.getId());
        String json = JSON.toJSONString(accountCycleDto, new EnumTextDisplayAfterFilter());
        map.put("detail", JSON.parseObject(json));
        map.put("settled", CycleState.ACTIVE.getCode());
        map.put("showPrintBtn", "false");
        map.put("showSettlePrintBtn", "false");
        if (accountCycleDto.getState() == CycleState.SETTLED.getCode()) {
            map.put("showPrintBtn", "true");
        }
        return "cycle/detail";
    }

    /**
     * 跳转平账页面
     *
     * @date 2020/7/6
     */
    @GetMapping("/flated.html")
    public String flatedHtml(Long id, ModelMap map) {
        log.info("跳转平账页面flatedHtml{}>{}", id, map);
        if (id == null || id < 0L) {
            throw new CardAppBizException(ResultCode.PARAMS_ERROR, "账务周期对账请求参数错误");
        }
        map.put("detail", iAccountCycleService.findById(id));
        return "cycle/flated";
    }

    /**
     * 跳转发起交款页面
     *
     * @date 2020/7/6
     */
    @GetMapping("/addPayer.html")
    public String addPayer(Long id, ModelMap map) {
        if (id == null || id < 0L) {
            throw new CardAppBizException(ResultCode.PARAMS_ERROR, "账务周期发起交款请求参数错误");
        }
        map.put("detail", iAccountCycleService.findById(id));
        map.put("action", CashAction.PAYER.getCode());
        map.put("actionText", CashAction.PAYER.getName());
        return "cycle/addPayer";
    }

    /**
     * 结账申请对账
     */
    @PostMapping("/applySettle.action")
    @ResponseBody
    public BaseOutput<AccountCycleDto> applySettle(@RequestBody @Validated(value = {ConstantValidator.Update.class}) AccountCycleDto accountCycleDto) {
        log.info("结账申请对账*****{}", JSONObject.toJSONString(accountCycleDto));
        return BaseOutput.successData(iAccountCycleService.settle(accountCycleDto));
    }

    /**
     * 平账
     */
    @PostMapping("/flated.action")
    @ResponseBody
    public BaseOutput<Boolean> flated(@RequestBody @Validated(value = {ConstantValidator.Default.class}) AccountCycleDto accountCycleDto) {
        log.info("平账*****{}", JSONObject.toJSONString(accountCycleDto));
        // 构建商户相关信息
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new CardAppBizException(ResultCode.PARAMS_ERROR, "登录过期,请重新登录");
        }
        iAccountCycleService.flated(accountCycleDto.getId(), userTicket.getId(), userTicket.getRealName());
        return BaseOutput.success("平账成功！");
    }

    /**
     * 账务列表
     */
    @PostMapping("/page.action")
    @ResponseBody
    public Map<String, Object> page(@RequestBody @Validated(ConstantValidator.Page.class) AccountCycleDto accountCycleDto) {
        log.info("对账管理列表查询*****{}", JSONObject.toJSONString(accountCycleDto));
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new CardAppBizException("登录过期，请重新登录");
        }
        accountCycleDto.setFirmId(userTicket.getFirmId());
        return successPage(iAccountCycleService.page(accountCycleDto));
    }

    /**
     * 账务详情
     */
    @PostMapping("/detail.action")
    @ResponseBody
    public BaseOutput<AccountCycleDto> detail(@RequestBody @Validated(value = {ConstantValidator.Default.class}) AccountCycleDto accountCycleDto) {
        log.info("账务详情>{}", JSONObject.toJSONString(accountCycleDto));
        AccountCycleDto detail = iAccountCycleService.detail(accountCycleDto.getId());
        return BaseOutput.successData(detail);
    }

    /**
     * 校验是否存在活跃的账务周期
     */
    @PostMapping("/checkExistActiveCycle.action")
    @ResponseBody
    public BaseOutput<Boolean> checkExistActiveCycle(@RequestBody AccountCycleDto accountCycleDto) {
        log.info("校验是否存在活跃的账务周期*****{}", JSONObject.toJSONString(accountCycleDto));
        return BaseOutput.successData(iAccountCycleService.checkExistActiveCycle(accountCycleDto.getUserId()));
    }


    /**
     * 结帐申请打印
     */
    @RequestMapping("/print.action")
    @ResponseBody
    public BaseOutput<AccountCyclePrintlDto> print(Long userId, Double settleAmount) {
        log.info("结帐申请打印*****{}=={}", userId, settleAmount);
        // 获取最新的账务周期
        AccountCycleDo accountCycle = iAccountCycleService.findLatestCycleByUserId(userId);
        log.info("结帐申请打印数据*****{}", JSONObject.toJSONString(accountCycle));
        AccountCyclePrintlDto printlDto = new AccountCyclePrintlDto();
        printlDto.setCycleNo(accountCycle.getCycleNo());
        printlDto.setEndTime(accountCycle.getEndTime());
        printlDto.setFirmName(getUserTicket().getFirmName());
        printlDto.setLastDeliverAmountText("￥" + NumberUtil.decimalFormatMoney(settleAmount));
        printlDto.setUserName(accountCycle.getUserName());
        printlDto.setPrintTime(LocalDateTime.now());
        printlDto.setPrintUserName(getUserTicket().getRealName());
        Firm firm = GenericRpcResolver.resolver(firmRpc.getById(getUserTicket().getFirmId()), ServiceName.UAP);
        printlDto.setFirmSimpleName(firm.getSimpleName());
        return BaseOutput.successData(printlDto);
    }

    /**
     * 获取结账打印数据
     * @author miaoguoxin
     * @date 2021/3/8
     */
    @RequestMapping("/settlePrint.action")
    @ResponseBody
    public BaseOutput<AccountSettlePrintPrintlDto> settlePrint(Long id) {
        log.info("对帐申请打印*****{}", id);
        AccountCycleDto detail = iAccountCycleService.detail(id);
        // 获取最新的账务周期,获取对帐时间，能进到这个方法的都是已对帐了的
        //AccountCycleDo accountCycle = iAccountCycleService.findLatestCycleByUserId(detail.getUserId());

        AccountSettlePrintPrintlDto printlDto = new AccountSettlePrintPrintlDto();
        printlDto.setCycleNo(detail.getCycleNo());
        printlDto.setEndTime(detail.getEndTime());
        printlDto.setFirmName(getUserTicket().getFirmName());
        Long deliverAmount = detail.getAccountCycleDetailDto().getDeliverAmount();
        printlDto.setLastDeliverAmountText("￥" + getMoneyFormat(deliverAmount));
        printlDto.setUserName(detail.getUserName());
        printlDto.setSettleTime(detail.getSettleTime());
        printlDto.setPrintTime(LocalDateTime.now());
        printlDto.setPrintUserName(getUserTicket().getRealName());
        printlDto.setAuditorId(detail.getAuditorId());
        printlDto.setAuditorName(detail.getAuditorName());

        // 对帐详情数据
        AccountCycleDetailDto cycleData = detail.getAccountCycleDetailDto();
        printlDto.setReceiveTimes(cycleData.getReceiveTimes());
        printlDto.setReceiveAmountText(getMoneyFormat(cycleData.getReceiveAmount()));
        printlDto.setDeliverTimes(cycleData.getDeliverTimes());
        printlDto.setDeliverAmountText(getMoneyFormat(cycleData.getDeliverAmount()));
        printlDto.setDepoCashTimes(cycleData.getDepoCashTimes());
        printlDto.setDepoCashAmountText(getMoneyFormat(cycleData.getDepoCashAmount()));
        printlDto.setDepoPosTimes(cycleData.getDepoPosTimes());
        printlDto.setDepoPosAmountText(getMoneyFormat(cycleData.getDepoPosAmount()));
        printlDto.setBankInTimes(cycleData.getBankInTimes());
        printlDto.setBankInAmountText(getMoneyFormat(cycleData.getBankInAmount()));
        printlDto.setBankOutTimes(cycleData.getBankOutTimes());
        printlDto.setBankOutAmountText(getMoneyFormat(cycleData.getBankOutAmount()));
        printlDto.setDrawCashTimes(cycleData.getDrawCashTimes());
        printlDto.setDrawCashAmountText(getMoneyFormat(cycleData.getDrawCashAmount()));
        printlDto.setLastDeliverAmountText(getMoneyFormat(cycleData.getLastDeliverAmount()));
        Firm firm = GenericRpcResolver.resolver(firmRpc.getById(getUserTicket().getFirmId()), ServiceName.UAP);
        printlDto.setFirmSimpleName(firm.getSimpleName());
        log.info("对帐申请打印数据*****{}", JSONObject.toJSONString(printlDto));
        return BaseOutput.successData(printlDto);
    }

    /**
     * 带￥符号前缀，千分位，保留两位小数
     * @param amount 分
     * @return
     */
    private static String getMoneyFormat(Long amount) {
        if (amount == null) {
            amount = 0L;
        }
        return "￥" + CurrencyUtils.toYuanWithStripTrailingZeros(amount);
    }
}

package com.dili.card.controller;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.BankCounterPrintResponseDto;
import com.dili.card.dto.BankCounterQuery;
import com.dili.card.dto.BankCounterRequestDto;
import com.dili.card.dto.BankCounterResponseDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.IBankCounterService;
import com.dili.card.validator.ConstantValidator;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/12/9 15:28
 * @Description: 银行存取款
 */
@Controller
@RequestMapping("/bankCounter")
public class BankCounterController implements IControllerHandler {

    @Autowired
    private IBankCounterService bankCounterService;

    /**
     * 跳转列表页面
     * @author miaoguoxin
     * @date 2020/6/22
     */
    @GetMapping("/list.html")
    public String listView() {
        return "bankcounter/list";
    }

    /**
     * 分页查询
     * @author miaoguoxin
     * @date 2020/6/22
     */
    @PostMapping("/page.action")
    @ResponseBody
    public Map<String, Object> page(@Validated(ConstantValidator.Page.class)
                                            BankCounterQuery param) {
        // this.buildOperatorInfo(param);
        this.validateTime(param);
        PageOutput<List<BankCounterResponseDto>> page = bankCounterService.getPage(param);
        return successPage(page);
    }

    /**
    *  获取打印数据
    * @author miaoguoxin
    * @date 2021/1/27
    */
    @GetMapping("/printData.action")
    @ResponseBody
    public BaseOutput<BankCounterPrintResponseDto> getPrintData(Long id){
        BankCounterPrintResponseDto printData = bankCounterService.getPrintData(id);
        return BaseOutput.successData(printData);
    }

    /**
     * 跳转新增存款页面
     * @author miaoguoxin
     * @date 2020/12/10
     */
    @GetMapping("/addDeposit.html")
    public String addDepositView(ModelMap modelMap) {
        modelMap.addAttribute("date", DateUtil.now());
        return "bankcounter/addDeposit";
    }

    /**
     * 添加存款信息
     * @author miaoguoxin
     * @date 2020/12/10
     */
    @PostMapping("/addDeposit.action")
    @ResponseBody
    public BaseOutput<?> addDeposit(@RequestBody @Validated(ConstantValidator.Insert.class)
                                            BankCounterRequestDto requestDto) {
        buildOperatorInfo(requestDto);
        bankCounterService.add(requestDto);
        return BaseOutput.success();
    }

    /**
     * 跳转新增取款页面
     * @author miaoguoxin
     * @date 2020/12/10
     */
    @GetMapping("/addWithdraw.html")
    public String addWithdrawView(ModelMap modelMap) {
        modelMap.addAttribute("date", DateUtil.now());
        return "bankcounter/addWithdraw";
    }

    /**
    * 新增银行取款
    * @author miaoguoxin
    * @date 2020/12/10
    */
    @PostMapping("/addWithdraw.action")
    @ResponseBody
    public BaseOutput<?> addWithdraw(@RequestBody @Validated(ConstantValidator.Insert.class)
                                            BankCounterRequestDto requestDto) {
        buildOperatorInfo(requestDto);
        bankCounterService.add(requestDto);
        return BaseOutput.success();
    }


    /**
    * 校验时间
    * @author miaoguoxin
    * @date 2021/1/7
    */
    private void validateTime(BankCounterQuery param) {
        if (param.getStartDate() == null || param.getEndDate() == null) {
            return;
        }
        Date startDate = DateUtils.localDateTimeToUdate(param.getStartDate());
        Date endDate = DateUtils.localDateTimeToUdate(param.getEndDate());
        long diffDay = DateUtil.between(startDate, endDate, DateUnit.DAY, true);
        if (diffDay > 365) {
            throw new CardAppBizException("查询时间跨度不能超过365天");
        }
    }
}

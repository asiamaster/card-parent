package com.dili.card.controller;

import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.BankCounterQuery;
import com.dili.card.dto.BankCounterRequestDto;
import com.dili.card.dto.BankCounterResponseDto;
import com.dili.card.service.IBankCounterService;
import com.dili.card.validator.ConstantValidator;
import com.dili.ss.domain.BaseOutput;
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
import java.util.Map;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/12/9 15:28
 * @Description:
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
        PageOutput<List<BankCounterResponseDto>> page = bankCounterService.getPage(param);
        return successPage(page);

    }

    /**
     * 跳转新增存款页面
     * @author miaoguoxin
     * @date 2020/12/10
     */
    @GetMapping("/addDeposit.html")
    public String addDepositView() {
        return "bankCounter/addDeposit";
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
    public String addWithdrawView() {
        return "bankCounter/addWithdraw";
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
}

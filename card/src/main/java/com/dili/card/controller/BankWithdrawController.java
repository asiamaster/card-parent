package com.dili.card.controller;

import com.dili.card.common.handler.IControllerHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/11/27 16:57
 * @Description:
 */
@Controller
@RequestMapping("/bank_withdraw")
public class BankWithdrawController implements IControllerHandler {

    /**
    * 列表页
    * @author miaoguoxin
    * @date 2020/11/27
    */
    @GetMapping("/list.html")
    public String listView(){
        return "bankwithdarw/list";
    }


}

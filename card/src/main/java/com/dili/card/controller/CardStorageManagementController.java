package com.dili.card.controller;

import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.ApplyRecordQueryDto;
import com.dili.card.dto.ApplyRecordResponseDto;
import com.dili.card.service.ICardStorageService;
import com.dili.card.validator.ConstantValidator;
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

/**
 * 卡仓库管理
 * @Auther: miaoguoxin
 * @Date: 2020/7/1 16:11
 */
@Controller
@RequestMapping("cardStorage")
public class CardStorageManagementController implements IControllerHandler {

    @Autowired
    private ICardStorageService cardStorageService;

    /**
    * 跳转出库页面
    * @author miaoguoxin
    * @date 2020/7/1
    */
    @GetMapping("outList.html")
    public String outListView(){
        return "cardstorage/outList";
    }
    /**
    * 多条件分页
    * @author miaoguoxin
    * @date 2020/7/1
    */
    @PostMapping("page.action")
    @ResponseBody
    public PageOutput<List<ApplyRecordResponseDto>> getPage(@RequestBody @Validated(ConstantValidator.Page.class)
                                                                        ApplyRecordQueryDto queryDto){
        return cardStorageService.getPage(queryDto);
    }
}

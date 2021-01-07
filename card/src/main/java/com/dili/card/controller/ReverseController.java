package com.dili.card.controller;

import com.alibaba.fastjson.JSON;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.common.serializer.EnumTextDisplayAfterFilter;
import com.dili.card.dto.ReverseRecordQueryDto;
import com.dili.card.dto.ReverseRecordResponseDto;
import com.dili.card.dto.ReverseRequestDto;
import com.dili.card.service.IReverseService;
import com.dili.card.util.AssertUtils;
import com.dili.card.validator.ConstantValidator;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.uap.sdk.domain.UserTicket;
import org.apache.commons.lang3.math.NumberUtils;
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

/**
 * @Auther: miaoguoxin
 * @Date: 2020/11/24 14:28
 * @Description:
 */
@Controller
@RequestMapping("/reverse")
public class ReverseController implements IControllerHandler {
    @Autowired
    private IReverseService reverseService;

    /**
     *  冲正记录页面
     * @author miaoguoxin
     * @date 2020/11/25
     */
    @GetMapping("/list.html")
    public String listView() {
        return "reverse/list";
    }

    /**
     * 冲正初始化页面
     * @author miaoguoxin
     * @date 2020/11/25
     */
    @GetMapping("/add.html")
    public String addView() {
        return "reverse/add";
    }

    /**
     * 详情页面
     * @author miaoguoxin
     * @date 2020/11/25
     */
    @GetMapping("/detail.html")
    public String detailView(String serialNo, ModelMap map) {
        AssertUtils.notEmpty(serialNo, "业务流水号缺失");
        UserTicket userTicket = getUserTicket();
        String json = JSON.toJSONString(reverseService.getDetail(serialNo, userTicket.getFirmId()),
                new EnumTextDisplayAfterFilter());
        map.put("detail", JSON.parseObject(json));
        return "reverse/detail";
    }

    /**
     * 冲正记录分页
     * @author miaoguoxin
     * @date 2020/11/24
     */
    @PostMapping("/page.action")
    @ResponseBody
    public Map<String, Object> getReversePage(@Validated(ConstantValidator.Page.class)
                                                                             ReverseRecordQueryDto queryDto) {
        AssertUtils.notNull(queryDto.getFirmId(), "市场id不能为空");
        return successPage(reverseService.getPage(queryDto));
    }

    /**
     * 提交冲正
     * @author miaoguoxin
     * @date 2020/11/25
     */
    @PostMapping("/add.action")
    @ResponseBody
    public BaseOutput<Long> add(@RequestBody @Validated(ConstantValidator.Insert.class)
                                            ReverseRequestDto requestDto) {
        buildOperatorInfo(requestDto);
        validateCommonParam(requestDto);
        requestDto.setFee(NumberUtils.toLong(requestDto.getFee() + ""));
        return BaseOutput.successData(reverseService.add(requestDto));
    }

}

package com.dili.card.controller;

import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.CardStorageDto;
import com.dili.card.dto.CardStorageOutQueryDto;
import com.dili.card.dto.CardStorageOutRequestDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.ICardStorageService;
import com.dili.card.type.CardStorageState;
import com.dili.card.util.AssertUtils;
import com.dili.card.validator.ConstantValidator;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 卡仓库管理
 *
 * @Auther: miaoguoxin
 * @Date: 2020/7/1 16:11
 */
@Controller
@RequestMapping("cardStorage")
public class CardStorageOutController implements IControllerHandler {

    @Autowired
    private ICardStorageService cardStorageService;

    /**
     * 跳转出库页面
     * @author miaoguoxin
     * @date 2020/7/1
     */
    @GetMapping("outList.html")
    public String outListView() {
        return "cardstorage/outList";
    }


    /**
     * 跳转到出库详情
     *
     * @author miaoguoxin
     * @date 2020/7/2
     */
    @GetMapping("outDetail.html")
    public String outDetailView(Long id, ModelMap modelMap) {
        if (id == null || id <= 0) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "id不能为空");
        }
        modelMap.put("detail", cardStorageService.getById(id));
        return "cardstorage/outDetail";
    }

    /**
     * 跳转到添加页面
     *
     * @author miaoguoxin
     * @date 2020/7/2
     */
    @GetMapping("outAdd.html")
    public String outAddView() {
        return "cardstorage/outAdd";
    }

    /**
     * 列表分页
     *
     * @author miaoguoxin
     * @date 2020/7/1
     */
    @PostMapping("outPage.action")
    @ResponseBody
    public Map<String, Object> getPage(@Validated(ConstantValidator.Page.class)
                                               CardStorageOutQueryDto queryDto) {
        // this.buildOperatorInfo(queryDto);
        return successPage(cardStorageService.getPage(queryDto));
    }

    /**
     * 添加出库记录
     * @author miaoguoxin
     * @date 2020/7/3
     */
    @PostMapping("addOut.action")
    @ResponseBody
    public BaseOutput<?> addOutRecord(
            @RequestBody @Validated(ConstantValidator.Insert.class) CardStorageOutRequestDto requestDto) {
        this.buildOperatorInfo(requestDto);
        cardStorageService.saveOutRecord(requestDto);
        return BaseOutput.success();
    }


    /**
     * 校验卡状态
     * @author miaoguoxin
     * @date 2020/7/29
     */
    @PostMapping("checkCard.action")
    @ResponseBody
    public BaseOutput<?> checkCard(@RequestBody CardRequestDto cardRequestDto) {
        AssertUtils.notEmpty(cardRequestDto.getCardNo(),"卡号不能为空");
        CardStorageDto cardStorage = cardStorageService.getCardStorageByCardNo(cardRequestDto.getCardNo());
        if (cardStorage.getState() != CardStorageState.UNACTIVATE.getCode()) {
            return BaseOutput.failure("该卡状态为[" + CardStorageState.getName(cardStorage.getState()) + "],不能出库!");
        }
        return BaseOutput.success();
    }
}

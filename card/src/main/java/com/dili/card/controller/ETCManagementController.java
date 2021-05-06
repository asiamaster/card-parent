package com.dili.card.controller;

import cn.hutool.core.util.StrUtil;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.ETCQueryDto;
import com.dili.card.dto.ETCRequestDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IBusinessLogService;
import com.dili.card.service.ICustomerService;
import com.dili.card.service.IETCService;
import com.dili.card.service.IMiscService;
import com.dili.card.type.CardType;
import com.dili.card.type.DictValue;
import com.dili.card.type.OperateType;
import com.dili.card.util.AssertUtils;
import com.dili.card.validator.ConstantValidator;
import com.dili.ss.domain.BaseOutput;
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

import java.util.BitSet;
import java.util.Map;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/4/26 10:24
 * @Description:
 */
@Controller
@RequestMapping("etc")
public class ETCManagementController implements IControllerHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ETCManagementController.class);

    @Autowired
    private IMiscService miscService;
    @Autowired
    private IAccountQueryService accountQueryService;
    @Autowired
    private IETCService ETCService;
    @Autowired
    private IBusinessLogService businessLogService;
    @Autowired
    private ICustomerService customerService;

    /**
     * 跳转首页
     * @author miaoguoxin
     * @date 2021/4/26
     */
    @GetMapping("/index.html")
    public String index(ModelMap pageView) {
        UserTicket userTicket = getUserTicket();
        String dictVal = miscService.getSingleDictVal(DictValue.PWD_BOX_ALLOW_INPUT.getCode(), userTicket.getFirmId(), "1");
        pageView.put("allowInput", dictVal);
        return "etc/index";
    }

    /**
    * etc列表查询页面
    * @author miaoguoxin
    * @date 2021/5/6
    */
    @GetMapping("/list.html")
    public String list(){
        return "etc/list";
    }

    /**
     * 获取卡信息
     * @author miaoguoxin
     * @date 2021/4/26
     */
    @GetMapping("/cardInfo.action")
    @ResponseBody
    public BaseOutput<UserAccountCardResponseDto> getCardInfo(String cardNo) {
        AssertUtils.notEmpty(cardNo, "卡号不能为空");
        UserAccountCardResponseDto dto = accountQueryService.getByCardNo(cardNo);
        if (!CardType.isMaster(dto.getCardType())) {
            throw new CardAppBizException("请使用主卡进行操作");
        }
        String subTypeNames = customerService.getSubTypeNames(dto.getCustomerId(), dto.getFirmId());
        dto.setSubTypeNames(subTypeNames);
        return BaseOutput.successData(dto);
    }

    /**
     * 新增绑定车牌
     * @author miaoguoxin
     * @date 2021/4/27
     */
    @PostMapping("bind.action")
    @ResponseBody
    public BaseOutput<String> bind(@RequestBody @Validated(ConstantValidator.Insert.class)
                                          ETCRequestDto requestDto) {
        validateCommonParam(requestDto);
        buildOperatorInfo(requestDto);
        String content = StrUtil.format("账户id：{}，关联卡号：{}，绑定车牌：{}",
                requestDto.getAccountId(), requestDto.getCardNo(), requestDto.getPlateNo());
        businessLogService.saveLog(OperateType.ETC_BIND, getUserTicket(), content);
        return BaseOutput.successData(ETCService.bind(requestDto));
    }

    /**
    * 解绑
    * @author miaoguoxin
    * @date 2021/4/27
    */
    @PostMapping("unbind.action")
    @ResponseBody
    public BaseOutput<?> unbind(@RequestBody @Validated(ConstantValidator.Update.class)
                                        ETCRequestDto requestDto){
        validateCommonParam(requestDto);
        buildOperatorInfo(requestDto);
        ETCService.unBind(requestDto);
        return BaseOutput.success();
    }

    /**
     * 分页查询
     * @author miaoguoxin
     * @date 2021/4/27
     */
    @PostMapping("page.action")
    @ResponseBody
    public Map<String, Object> getPage(ETCQueryDto queryDto) {
        // AssertUtils.notNull(queryDto.getFirmId(),"市场id不能为空");
        queryDto.setFirmId(8L);
        return successPage(ETCService.getPage(queryDto));
    }

}

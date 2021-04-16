package com.dili.card.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.common.serializer.EnumTextDisplayAfterFilter;
import com.dili.card.dto.BusinessRecordResponseDto;
import com.dili.card.dto.BusinessRecordSummaryDto;
import com.dili.card.dto.SerialQueryDto;
import com.dili.card.dto.SerialRecordResponseDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.ISerialService;
import com.dili.card.service.IStatisticsService;
import com.dili.card.type.OperateType;
import com.dili.card.type.ReverseFundItemMap;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/4/8 09:13
 * @Description:
 */
@Controller
@RequestMapping("/statistics")
public class StatisticsController implements IControllerHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    private IStatisticsService statisticsService;
    @Autowired
    private ISerialService serialService;
    @Autowired
    private IAccountQueryService accountQueryService;

    private static List<Integer> operateType = new ArrayList<>();

    static {
        operateType.add(OperateType.ACCOUNT_TRANSACT.getCode());
        operateType.add(OperateType.CHANGE.getCode());
        operateType.add(OperateType.ACCOUNT_CHARGE.getCode());
        operateType.add(OperateType.ACCOUNT_WITHDRAW.getCode());
        operateType.add(OperateType.REFUND_CARD.getCode());
        operateType.add(OperateType.LOSS_CARD.getCode());
        operateType.add(OperateType.LOSS_REMOVE.getCode());
        operateType.add(OperateType.PWD_CHANGE.getCode());
        operateType.add(OperateType.RESET_PWD.getCode());
        operateType.add(OperateType.LIFT_LOCKED.getCode());
        operateType.add(OperateType.PERMISSION_SET.getCode());
    }

    /**
     * 卡务统计首页跳转
     * @author miaoguoxin
     * @date 2021/4/12
     */
    @GetMapping("index.html")
    public String recordIndex(ModelMap model) {
        UserTicket userTicket = getUserTicket();
        //需要做数据权限控制
        List<Map> maps = SessionContext.getSessionContext().dataAuth("dataRange");
        if (CollectionUtil.isEmpty(maps)){
            model.put("allAuth",1);
        }else {
            Object value = maps.get(0).get("value");
            model.put("allAuth",NumberUtil.parseInt(value.toString()));
        }
        model.put("operatorId", userTicket.getId());
        model.put("userName", userTicket.getRealName());
        return "statistics/recordList";
    }

    /**
     * 跳转记录详情页
     * @author miaoguoxin
     * @date 2021/4/12
     */
    @GetMapping("recordDetail.html")
    public String recordDetail(Long id, ModelMap modelMap) {
        //操作记录
        BusinessRecordResponseDto recordResponseDto = serialService.getOneById(id);
        //手续费信息
        SerialQueryDto queryDto = new SerialQueryDto();
        queryDto.setSerialNo(recordResponseDto.getSerialNo());
        List<SerialRecordResponseDto> serialList = serialService.getSerialList(queryDto);
        List<SerialRecordResponseDto> feeList = serialList.stream()
                .filter(l -> ReverseFundItemMap.isFeeFundItem(l.getFundItem()))
                .collect(Collectors.toList());
        Long totalFee = feeList.stream()
                .mapToLong(serialRecordDo -> NumberUtils.toLong(serialRecordDo.getAmount() + ""))
                .sum();
        //期末需要扣除手续费
        recordResponseDto.setEndBalance(NumberUtil.sub(recordResponseDto.getEndBalance(), totalFee).longValue());

        String recordJson = JSON.toJSONString(recordResponseDto, new EnumTextDisplayAfterFilter());
        modelMap.put("record", JSON.parseObject(recordJson));
        String listJson = JSON.toJSONString(feeList, new EnumTextDisplayAfterFilter());
        modelMap.put("feeList", JSON.parseArray(listJson));

        //卡信息
        UserAccountCardResponseDto accountCard = accountQueryService.getByCardNoWithoutValidate(recordResponseDto.getCardNo());
        String accountCardJson = JSON.toJSONString(accountCard, new EnumTextDisplayAfterFilter());
        modelMap.put("accountCard", JSON.parseObject(accountCardJson));

        return "statistics/recordDetail";
    }

    /**
     * 操作记录类型分组汇总统计
     * @author miaoguoxin
     * @date 2021/4/8
     */
    @GetMapping("/recordSummary.action")
    @ResponseBody
    public BaseOutput<List<BusinessRecordSummaryDto>> getBusinessRecordSummaryList(SerialQueryDto queryDto) {
        UserTicket userTicket = getUserTicket();
        queryDto.setFirmId(userTicket.getFirmId());
        List<BusinessRecordSummaryDto> result = statisticsService.countBusinessRecordSummary(queryDto);
        return BaseOutput.successData(result);
    }

    /**
     * 业务日志分页
     * @author miaoguoxin
     * @date 2020/7/1
     */
    @PostMapping("/business/page.action")
    @ResponseBody
    public Map<String, Object> businessPage(SerialQueryDto queryDto) {
        LOGGER.info("统计业务日志分页*****{}", JSONObject.toJSONString(queryDto));
        UserTicket userTicket = getUserTicket();
        queryDto.setFirmId(userTicket.getFirmId());
        List<Integer> operateTypeList = queryDto.getOperateTypeList();
        if (CollectionUtil.isEmpty(operateTypeList)) {
            operateTypeList = new ArrayList<>(operateType);
        } else {
            operateTypeList.addAll(operateType);
        }
        queryDto.setOperateTypeList(operateTypeList);
        PageOutput<List<BusinessRecordResponseDto>> lists = serialService.queryPage(queryDto);
        return successPage(lists);
    }

}

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
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.entity.bo.FeeSerialRecordBo;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.ISerialService;
import com.dili.card.service.IStatisticsService;
import com.dili.card.type.OperateType;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
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

    private static List<Integer> otherOperateType = new ArrayList<>();

    static {
        otherOperateType.add(OperateType.FROZEN_FUND.getCode());
        otherOperateType.add(OperateType.UNFROZEN_FUND.getCode());
        otherOperateType.add(OperateType.FROZEN_ACCOUNT.getCode());
        otherOperateType.add(OperateType.UNFROZEN_ACCOUNT.getCode());
        otherOperateType.add(OperateType.FUND_REVERSE.getCode());

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

        operateType.addAll(otherOperateType);
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
        model.put("allAuth", getDataAuthFlag());
        model.put("firmName", userTicket.getFirmName());
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
        BusinessRecordResponseDto record = serialService.getOneById(id);
        //手续费信息
        FeeSerialRecordBo feeSerialRecordBo = serialService.getFeeSerialListBySerialNo(record.getSerialNo());
        //期末需要扣除手续费
        record.setEndBalance(feeSerialRecordBo.calculateEndBalanceWhenDeductFee(record.getEndBalance()));
        //卡信息
        UserAccountCardResponseDto accountCard = accountQueryService.getByCardNoWithoutValidate(record.getCardNo());

        String accountCardJson = JSON.toJSONString(accountCard, new EnumTextDisplayAfterFilter());
        String recordJson = JSON.toJSONString(record, new EnumTextDisplayAfterFilter());
        String listJson = JSON.toJSONString(feeSerialRecordBo.getFeeList(), new EnumTextDisplayAfterFilter());
        modelMap.put("record", JSON.parseObject(recordJson));
        modelMap.put("feeList", JSON.parseArray(listJson));
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
        //“个人”权限设置为当前操作员
        if (getDataAuthFlag() == 0) {
            queryDto.setOperatorId(userTicket.getId());
        }
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
        //只查询指定的操作类型
        List<Integer> operateTypeList = queryDto.getOperateTypeList();
        if (CollectionUtil.isEmpty(operateTypeList)) {
            operateTypeList = new ArrayList<>(operateType);
        }else if (operateTypeList.contains(0)){
            //“其他”类型
            operateTypeList.addAll(otherOperateType);
        }
        queryDto.setOperateTypeList(operateTypeList);
        //“个人”权限设置为当前操作员
        if (getDataAuthFlag() == 0) {
            queryDto.setOperatorId(userTicket.getId());
        }
        PageOutput<List<BusinessRecordResponseDto>> lists = serialService.queryPage(queryDto);
        return successPage(lists);
    }

    /**
     * 获取数据权限的标记 ，1：所有人 0：个人
     * @author miaoguoxin
     * @date 2021/4/19
     */
    private static int getDataAuthFlag() {
        List<Map> maps = SessionContext.getSessionContext().dataAuth("dataRange");
        if (CollectionUtil.isEmpty(maps)) {
            return 1;
        } else {
            Object value = maps.get(0).get("value");
            return NumberUtil.parseInt(value.toString());
        }
    }
}

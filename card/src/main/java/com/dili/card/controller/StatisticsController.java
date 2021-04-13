package com.dili.card.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
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
import com.dili.card.type.ReverseFundItemMap;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @Autowired
    private IStatisticsService statisticsService;
    @Autowired
    private ISerialService serialService;
    @Autowired
    private IAccountQueryService accountQueryService;

    /**
     * 卡务统计首页跳转
     * @author miaoguoxin
     * @date 2021/4/12
     */
    @GetMapping("index.html")
    public String recordIndex(ModelMap model) {
        UserTicket userTicket = getUserTicket();
        //TODO 空暂时代表“所有人”，uap有bug
        //需要做数据权限控制
        List<Map> maps = SessionContext.getSessionContext().dataAuth();
        model.put("allAuth", CollectionUtil.isEmpty(maps) ? 1 : 0);
        model.put("operatorId", userTicket.getId());
        model.put("userName", userTicket.getRealName());
        //List<Map> dataRange = SessionContext.getSessionContext().dataAuth("dataRange");
        return "statistics/recordList";
    }

    /**
     * 跳转记录详情页
     * @author miaoguoxin
     * @date 2021/4/12
     */
    @GetMapping("recordDetail.html")
    public String recordDetail(Long id, ModelMap modelMap) {
        BusinessRecordResponseDto recordResponseDto = serialService.getOneById(id);
        String recordJson = JSON.toJSONString(recordResponseDto, new EnumTextDisplayAfterFilter());
        modelMap.put("record", JSON.parseObject(recordJson));

        //卡信息
        UserAccountCardResponseDto accountCard = accountQueryService.getByCardNoWithoutValidate(recordResponseDto.getCardNo());
        String accountCardJson = JSON.toJSONString(accountCard, new EnumTextDisplayAfterFilter());
        modelMap.put("accountCard", JSON.parseObject(accountCardJson));

        //流水信息
        SerialQueryDto queryDto = new SerialQueryDto();
        queryDto.setSerialNo(recordResponseDto.getSerialNo());
        List<SerialRecordResponseDto> serialList = serialService.getSerialList(queryDto);
        List<SerialRecordResponseDto> feeList = serialList.stream()
                .filter(l -> ReverseFundItemMap.isFeeFundItem(l.getFundItem()))
                .collect(Collectors.toList());
        String listJson = JSON.toJSONString(feeList, new EnumTextDisplayAfterFilter());
        modelMap.put("feeList", JSON.parseArray(listJson));
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
        //TODO 如果数据权限是“个人”，那么只查询当前操作人，如果是“所有人”，那么不设置searchOpId
        List<BusinessRecordSummaryDto> result = statisticsService.countBusinessRecordSummary(queryDto);
        return BaseOutput.successData(result);
    }
}

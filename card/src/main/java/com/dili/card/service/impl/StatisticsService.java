package com.dili.card.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.dili.card.dao.IStatisticsDao;
import com.dili.card.dto.BusinessRecordSummaryDto;
import com.dili.card.dto.SerialQueryDto;
import com.dili.card.entity.BusinessRecordSummaryDo;
import com.dili.card.service.IStatisticsService;
import com.dili.card.type.OperateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/4/8 09:20
 * @Description:
 */
@Service
public class StatisticsService implements IStatisticsService {

    @Autowired
    private IStatisticsDao statisticsDao;

    @Override
    public List<BusinessRecordSummaryDto> countBusinessRecordSummary(SerialQueryDto queryDto) {
        List<BusinessRecordSummaryDo> list = statisticsDao.countBusinessRecordOperateType(queryDto.getOperatorId(), queryDto.getFirmId(),
                queryDto.getOperateTimeStart(), queryDto.getOperateTimeEnd(), queryDto.getSerialNo());
        Map<Integer, BusinessRecordSummaryDto> summaryMap = initSummaryMap();
        for (BusinessRecordSummaryDo summaryDo : list) {
            BusinessRecordSummaryDto summaryDto = summaryMap.get(summaryDo.getOperateType());
            if (summaryDto == null) {
                continue;
            }
            summaryDto.setCount(NumberUtil.add(summaryDto.getCount(), summaryDo.getCount()).longValue());
        }
        return summaryMap.values().stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private static Map<Integer, BusinessRecordSummaryDto> initSummaryMap() {
        BusinessRecordSummaryDto other = new BusinessRecordSummaryDto(0, "其他", 1);
        final Map<Integer, BusinessRecordSummaryDto> SUMMARY_MAP = new LinkedHashMap<>();
        SUMMARY_MAP.put(OperateType.ACCOUNT_TRANSACT.getCode(), new BusinessRecordSummaryDto(OperateType.ACCOUNT_TRANSACT));
        SUMMARY_MAP.put(OperateType.CHANGE.getCode(), new BusinessRecordSummaryDto(OperateType.CHANGE));
        SUMMARY_MAP.put(OperateType.ACCOUNT_CHARGE.getCode(), new BusinessRecordSummaryDto(OperateType.ACCOUNT_CHARGE));
        SUMMARY_MAP.put(OperateType.ACCOUNT_WITHDRAW.getCode(), new BusinessRecordSummaryDto(OperateType.ACCOUNT_WITHDRAW));
        SUMMARY_MAP.put(OperateType.REFUND_CARD.getCode(), new BusinessRecordSummaryDto(OperateType.REFUND_CARD));
        SUMMARY_MAP.put(OperateType.LOSS_CARD.getCode(), new BusinessRecordSummaryDto(OperateType.LOSS_CARD));
        SUMMARY_MAP.put(OperateType.LOSS_REMOVE.getCode(), new BusinessRecordSummaryDto(OperateType.LOSS_REMOVE));
        SUMMARY_MAP.put(OperateType.PWD_CHANGE.getCode(), new BusinessRecordSummaryDto(OperateType.PWD_CHANGE));
        SUMMARY_MAP.put(OperateType.RESET_PWD.getCode(), new BusinessRecordSummaryDto(OperateType.RESET_PWD));
        SUMMARY_MAP.put(OperateType.LIFT_LOCKED.getCode(), new BusinessRecordSummaryDto(OperateType.LIFT_LOCKED));
        SUMMARY_MAP.put(OperateType.PERMISSION_SET.getCode(), new BusinessRecordSummaryDto(OperateType.PERMISSION_SET));
        //特殊的操作 “其他”
        SUMMARY_MAP.put(OperateType.FROZEN_FUND.getCode(), other);
        SUMMARY_MAP.put(OperateType.UNFROZEN_FUND.getCode(), other);
        SUMMARY_MAP.put(OperateType.FROZEN_ACCOUNT.getCode(), other);
        SUMMARY_MAP.put(OperateType.UNFROZEN_ACCOUNT.getCode(), other);
        SUMMARY_MAP.put(OperateType.FUND_REVERSE.getCode(), other);
        return SUMMARY_MAP;
    }
}

package com.dili.card.service.impl;

import com.dili.card.dao.IStatisticsDao;
import com.dili.card.dto.BusinessRecordSummaryDto;
import com.dili.card.dto.SerialQueryDto;
import com.dili.card.entity.BusinessRecordSummaryDo;
import com.dili.card.service.IStatisticsService;
import com.dili.card.type.OperateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
                queryDto.getOperateTimeStart(), queryDto.getOperateTimeEnd());
        Map<Integer, BusinessRecordSummaryDto> summaryMap = initSummaryMap();
        for (BusinessRecordSummaryDo summaryDo : list) {
            BusinessRecordSummaryDto summaryDto = summaryMap.get(summaryDo.getOperateType());
            if (summaryDto == null){
                continue;
            }
            summaryDto.setCount(summaryDo.getCount());
        }
        return new LinkedList<>(summaryMap.values());
    }

    private static Map<Integer, BusinessRecordSummaryDto> initSummaryMap(){
        final Map<Integer, BusinessRecordSummaryDto> SUMMARY_MAP = new LinkedHashMap<>();
        SUMMARY_MAP.put(OperateType.ACCOUNT_TRANSACT.getCode(), new BusinessRecordSummaryDto(OperateType.ACCOUNT_TRANSACT));
        SUMMARY_MAP.put(OperateType.CHANGE.getCode(), new BusinessRecordSummaryDto(OperateType.CHANGE));
        SUMMARY_MAP.put(OperateType.ACCOUNT_CHARGE.getCode(), new BusinessRecordSummaryDto(OperateType.ACCOUNT_CHARGE, 1));
        SUMMARY_MAP.put(OperateType.ACCOUNT_WITHDRAW.getCode(), new BusinessRecordSummaryDto(OperateType.ACCOUNT_WITHDRAW, 1));
        SUMMARY_MAP.put(OperateType.REFUND_CARD.getCode(), new BusinessRecordSummaryDto(OperateType.REFUND_CARD));
        SUMMARY_MAP.put(OperateType.LOSS_CARD.getCode(), new BusinessRecordSummaryDto(OperateType.LOSS_CARD));
        SUMMARY_MAP.put(OperateType.LOSS_REMOVE.getCode(), new BusinessRecordSummaryDto(OperateType.LOSS_REMOVE));
        SUMMARY_MAP.put(OperateType.PWD_CHANGE.getCode(), new BusinessRecordSummaryDto(OperateType.PWD_CHANGE));
        SUMMARY_MAP.put(OperateType.RESET_PWD.getCode(), new BusinessRecordSummaryDto(OperateType.RESET_PWD));
        SUMMARY_MAP.put(OperateType.LIFT_LOCKED.getCode(), new BusinessRecordSummaryDto(OperateType.LIFT_LOCKED));
        SUMMARY_MAP.put(OperateType.PERMISSION_SET.getCode(), new BusinessRecordSummaryDto(OperateType.PERMISSION_SET));
        return SUMMARY_MAP;
    }
}

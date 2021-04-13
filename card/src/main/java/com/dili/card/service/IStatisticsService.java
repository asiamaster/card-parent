package com.dili.card.service;

import com.dili.card.dto.BusinessRecordSummaryDto;
import com.dili.card.dto.SerialQueryDto;

import java.util.List;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/4/8 09:15
 * @Description:
 */
public interface IStatisticsService {
    /**
    * 操作记录汇总统计
    * @author miaoguoxin
    * @date 2021/4/8
    */
    List<BusinessRecordSummaryDto> countBusinessRecordSummary(SerialQueryDto queryDto);
}

package com.dili.card.dao;

import com.dili.card.entity.BusinessRecordSummaryDo;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/4/8 09:41
 * @Description:
 */
public interface IStatisticsDao {

    /**
     * 根据操作类型分组统计操作记录
     * @author miaoguoxin
     * @date 2021/4/8
     */
    List<BusinessRecordSummaryDo> countBusinessRecordOperateType(@Param("operatorId") Long operatorId,
                                                                 @Param("firmId") Long firmId,
                                                                 @Param("startDate") String startDate,
                                                                 @Param("endDate") String endDate);
}

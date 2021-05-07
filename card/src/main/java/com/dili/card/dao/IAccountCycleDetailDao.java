package com.dili.card.dao;

import com.dili.card.dto.CycleStatistcDto;
import com.dili.card.entity.AccountCycleDetailDo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 柜员账务周期详情
 * @author bob<>
 */
public interface IAccountCycleDetailDao {

    /**
     * 新增
     * @param accountCycleDetail
     * @return
     */
    int save(AccountCycleDetailDo accountCycleDetail);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    AccountCycleDetailDo getById(Long id);

    /**
     * 修改
     * @param accountCycleDetail
     * @return
     */
    int update(AccountCycleDetailDo accountCycleDetail);

    /**
     * 根据账务周期号统计账务周期详情
     */
    List<CycleStatistcDto> statisticCycleBussinessRecord(@Param("cycleNos") List<Long> cycleNos, @Param("types") List<Integer> types);

    /**
     * 根据账务周期号统计统计冲正记录
     */
    List<CycleStatistcDto> statisticReverseByCycleNo(List<Long> cycleNos);
}

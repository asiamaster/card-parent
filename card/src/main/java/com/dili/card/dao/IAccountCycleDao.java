package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import com.dili.card.entity.AccountCycleDo;

/**
 * 柜员账务周期
 * @author bob<>
 */
@Mapper
public interface IAccountCycleDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<AccountCycleDo> selectList(AccountCycleDo accountCycle);

    /**
     * 新增
     * @param accountCycle
     * @return
     */
	int save(AccountCycleDo accountCycle);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	AccountCycleDo getById(Long id);

    /**
     * 修改
     * @param accountCycle
     * @return
     */
	int update(AccountCycleDo accountCycle);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);

	/**
	 * 根据账务周期编号查询账务周期
	 * @param cycleNo
	 */
	AccountCycleDo findByCycleNo(Long cycleNo);

	/**
	 * 更新状态 
	 * @param id 账务周期主键id
	 * @param state 状态
	 */
	int updateStateById(Long id, Integer state, Integer version);

	/**
	 * 根据童虎id查询账务周期
	 * @param userId
	 * @param state
	 */
	AccountCycleDo findByUserIdAndState(Long userId, Integer state);
}

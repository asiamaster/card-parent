package com.dili.card.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dili.card.dto.AccountCycleDto;
import com.dili.card.entity.AccountCycleDo;

/**
 * 柜员账务周期
 * @author bob<>
 */
public interface IAccountCycleDao {

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
	 * 根据账务周期编号查询账务周期
	 * @param cycleNo
	 */
	AccountCycleDo findByCycleNo(@Param("cycleNo") Long cycleNo);

	/**
	 * 更新状态 
	 * @param id 账务周期主键id
	 * @param state 状态
	 */
	int updateStateById(@Param("id")  Long id, @Param("state") Integer state, @Param("version") Integer version);

	/**
	 * 根据童虎id查询账务周期
	 * @param userId
	 * @param state
	 */
	AccountCycleDo findByUserIdAndState(@Param("userId")  Long userId, @Param("state") Integer state);
	
	/**
	 * 更新现金池金额
	 * @param cycleNo z账务周期号
	 * @param amount 金额
	 */
	boolean updateCashBox(@Param("cycleNo") Long cycleNo, @Param("amount")  Long amount, @Param("version") Integer version);

	/**
	 * 通过条件查询
	 */
	List<AccountCycleDo> findByCondition(AccountCycleDto accountCycleDto);
	
	/**
	 * 通过条件查询
	 */
	Long findCountByCondition(AccountCycleDto accountCycleDto);

	/**
	 * 获取当前人最新的账务周期
	 */
	AccountCycleDo findLatestActiveCycleByUserId(@Param("userId") Long userId);
}

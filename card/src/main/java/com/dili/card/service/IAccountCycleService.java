package com.dili.card.service;

import java.util.List;

import com.dili.card.dto.AccountCycleDto;
import com.dili.card.entity.AccountCycleDo;
import com.dili.ss.domain.PageOutput;

/**
 * 账务周期服务
 * @author zhangxing
 */
public interface IAccountCycleService {

	/**
	 * 结账
	 */
	void settle(AccountCycleDto accountCycleDto);
	
	/**
	 * 平账
	 */
	void flated(Long id);

	/**
	 * 账务周期列表
	 */
	List<AccountCycleDto> list(AccountCycleDto accountCycleDto);
	
	/**
	 * 账务周期分页查询
	 */
	PageOutput<List<AccountCycleDto>> page(AccountCycleDto accountCycleDto);

	/**
	 * 账务周期详情
	 */
	AccountCycleDto detail(Long id);

	/**
	 * 获取当前活跃的账务周期号 没有就创建
	 * @param userId 操作员id
	 * @param userName 操作员姓名
	 * @param userCode 操作员工号
	 * @return
	 */
	AccountCycleDo findActiveCycleByUserId(Long userId, String userName, String userCode);

	/**
	 * 根据主键id查询账务周期
	 * @param cycleNo
	 */
	AccountCycleDo findById(Long id);

	/**
	 * 获取当前活跃的账务周期号  非创建
	 */
	AccountCycleDo findActiveCycleByUserId(Long userId);
	
	/**
	 * 增加现金池金额
	 * @param cycleNo z账务周期号
	 * @param amount 金额
	 */
	void increaseCashBox(Long cycleNo, Long amount);
	
	/**
	 * 减少现金池金额
	 * @param cycleNo z账务周期号
	 * @param amount 金额
	 */
	void decreaseeCashBox(Long cycleNo, Long amount);

	/**
	 * 账务周期结账申请详情
	 */
	AccountCycleDto applyDetail(Long userId);
	
	/**
	 * 更新现金柜
	 */
	void updateCashBox(Long cycleNo, Long amount);
	
	/**
	 * 获取最新账务周期
	 */
	AccountCycleDo findLatestCycleByUserId(Long userId);

	/**
	 * 校验是否存在合适的分支事务
	 */
	Boolean checkExistActiveCycle(Long userId);
	
	/**
	 * 更新状态 
	 * @param id 账务周期主键id
	 * @param state 状态
	 */
	void updateStateById(Long id, Integer state, Integer version);
	
}

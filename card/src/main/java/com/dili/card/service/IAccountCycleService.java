package com.dili.card.service;

import java.util.List;

import com.dili.card.dto.AccountCycleDto;
import com.dili.card.entity.AccountCycleDo;

/**
 * 账务周期服务
 * @author zhangxing
 */
public interface IAccountCycleService {

	/**
	 * 结账
	 */
	void settle(Long id);
	
	/**
	 * 结账
	 */
	void flated(Long id);

	/**
	 * 账务周期列表
	 */
	List<AccountCycleDto> list(AccountCycleDto accountCycleDto);

	/**
	 * 账务周期详情
	 */
	AccountCycleDto detail(Long id);

	/**
	 * 创建账务周期号
	 */
	AccountCycleDo findActiveCycleByUserId(Long userId, String userName);
	
	/**
	 * 通过用户id查询账务周期
	 */
	AccountCycleDo findByUserId(Long userId);
	
	/**
	 * 获取该柜员活跃的账务周期 
	 * @param userId 柜员编号
	 * @param userName 柜员姓名
	 */
	AccountCycleDo findActiveCycleByUserId(Long userId);
	
	/**
	 * 获取该柜员已结账账务周期 
	 * @param userId 柜员编号
	 * @param userName 柜员姓名
	 */
	AccountCycleDo findSettledCycleByUserId(Long userId);

	/**
	 * 根据账务周期编号查询账务周期
	 * @param cycleNo
	 */
	AccountCycleDo findById(Long id);

}

package com.dili.card.service;

import java.util.List;

import com.dili.card.dto.AccountCycleDto;

/**
 * 账务周期服务
 * @author zhangxing
 */
public interface IAccountCycleService {

	/**
	 * 对账
	 */
	void checkAccount(Long id);

	/**
	 * 账务周期列表
	 */
	List<AccountCycleDto> list(AccountCycleDto accountCycleDto);

	/**
	 * 账务周期详情
	 */
	AccountCycleDto detail(Long id);
	
	

}

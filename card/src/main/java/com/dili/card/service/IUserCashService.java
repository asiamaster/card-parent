package com.dili.card.service;

import java.util.List;

import com.dili.card.dto.UserCashDto;
import com.dili.card.type.CashAction;

/**
 * 领款取款操作
 */
public interface IUserCashService {

	/**
	 * 保存现金操作记录
	 */
	void save(UserCashDto userCashDto, CashAction cashAction);

	/**
	 * 现金操作记录列表
	 */
	List<UserCashDto> list(UserCashDto userCashDto, CashAction cashAction);

	/**
	 * 删除现金操作记录
	 */
	void delete(Long id);
	
	/**
	 * 删除现金操作记录
	 */
	UserCashDto findById(Long id);

	/**
	 * 现金领款记录列表
	 */
	List<UserCashDto> listPayee(UserCashDto userCashDto);

	/**
	 * 保存现金领款记录
	 */
	void savePayee(UserCashDto userCashDto);

	/**
	 * 保存现金交款记录
	 */
	void savePayer(UserCashDto userCashDto);

	/**
	 * 现金交款记录列表
	 */
	List<UserCashDto> listPayer(UserCashDto userCashDto);

	/**
	 * 修改更新
	 */
	void modify(UserCashDto userCashDto);
	
	/**
	 * 根据账务周期更新记录
	 */
	void flatedByCycle(Long cycleNo);

}

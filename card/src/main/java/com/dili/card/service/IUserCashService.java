package com.dili.card.service;

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
	void list(UserCashDto userCashDto, CashAction cashAction);

	/**
	 * 删除现金操作记录
	 */
	void delete(Long id);

	/**
	 * 现金领款记录列表
	 */
	void listPayee(UserCashDto userCashDto);

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
	void listPayer(UserCashDto userCashDto);

}

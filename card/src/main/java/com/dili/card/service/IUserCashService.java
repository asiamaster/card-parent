package com.dili.card.service;

import java.util.List;

import com.dili.card.dto.UserCashDto;
import com.dili.card.entity.UserCashDo;
import com.dili.card.type.CashAction;
import com.dili.ss.domain.PageOutput;

/**
 * 领款取款操作
 */
public interface IUserCashService {

	/**
	 * 保存现金操作记录
	 */
	void save(UserCashDto userCashDto);

	/**
	 * 现金操作记录列表
	 */
	List<UserCashDto> list(UserCashDto userCashDto, CashAction cashAction);

	/**
	 * 删除现金操作记录
	 */
	void delete(Long id);
	
	/**
	 * 查找操作记录
	 */
	UserCashDo findById(Long id);
	
	/**
	 * 查找操作记录
	 */
	UserCashDto detail(Long id);

	/**
	 * 现金领款记录列表
	 */
	PageOutput<List<UserCashDto>> listPayee(UserCashDto userCashDto);

	/**
	 * 现金交款记录列表
	 */
	PageOutput<List<UserCashDto>> listPayer(UserCashDto userCashDto);

	/**
	 * 修改更新
	 */
	void modify(UserCashDto userCashDto);
	
	/**
	 * 根据账务周期更新记录
	 */
	void flatedByCycle(Long cycleNo);
	
	/**
	 * 获取交款取款分类处理的总金额
	 */
	Long findTotalAmountByUserId(UserCashDto userCashDto, CashAction cashAction);
	
	/**
	 * 获取总金额
	 */
	Long findTotalAmountByUserId(UserCashDto userCashDto);

	/**
	 * 获取最新的现金领取款
	 */
	UserCashDo getLastestUesrCash(Long userId, Long cycle, int cashAction);

}

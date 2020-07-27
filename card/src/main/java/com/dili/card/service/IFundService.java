package com.dili.card.service;

import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.FundUnfrozenRecordDto;
import com.dili.card.dto.UnfreezeFundDto;
import com.dili.ss.domain.PageOutput;

/**
 * 资金操作service接口
 *
 * @author xuliang
 */
public interface IFundService {
	/**
	 * 冻结资金
	 * @author miaoguoxin
	 * @date 2020/6/29
	 */
	void frozen(FundRequestDto fundRequestDto);

	/**
	 * 未解冻资金记录
	 *
	 * @param fundRequestDto
	 */
	PageOutput<FundUnfrozenRecordDto> unfrozenRecord(UnfreezeFundDto unfreezeFundDto);

	/**
	 * 解冻资金,支持批量操作
	 */
	void unfrozen(UnfreezeFundDto unfreezeFundDto);

}

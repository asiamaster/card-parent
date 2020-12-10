package com.dili.card.service;

import java.util.List;

import com.dili.card.dto.AccountCycleDetailDto;
import com.dili.card.dto.AccountCycleDto;
import com.dili.card.entity.AccountCycleDo;

public interface ICycleStatisticService {
	
	/**
	 * 账务周期列表构建
	 */
	List<AccountCycleDto> statisticList(List<AccountCycleDo> cycles, boolean detail);
	
	/**
	 * 账务周期详情构建
	 */
	AccountCycleDetailDto statisticCycleDetail(Long cycleNo);
}

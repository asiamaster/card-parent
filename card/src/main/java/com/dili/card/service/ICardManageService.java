package com.dili.card.service;

import com.dili.card.dto.CardRequestDto;

/**
 * @description： 卡片管理服务，包括退卡，换卡，补卡，挂失，解挂
 *
 * @author ：WangBo
 * @time ：2020年4月26日下午5:59:10
 */
public interface ICardManageService {

	/**
	 * 解挂卡片
	 */
	void unLostCard(CardRequestDto cardParam);
	
	/**
	 * 退卡
	 */
	void returnCard(CardRequestDto cardParam);

}

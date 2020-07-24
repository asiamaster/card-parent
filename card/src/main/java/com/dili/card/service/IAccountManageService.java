package com.dili.card.service;

import com.dili.card.dto.CardRequestDto;

public interface IAccountManageService {
	/**
	 * 冻结账户
	 */
	void frozen(CardRequestDto cardRequestDto);

	/**
	 * 解冻账户
	 */
	void unfrozen(CardRequestDto cardRequestDto);
}

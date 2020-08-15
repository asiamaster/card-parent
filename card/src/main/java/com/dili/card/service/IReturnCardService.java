package com.dili.card.service;

import com.dili.card.dto.CardRequestDto;

/**
 * 退卡处理相关
 */
public interface IReturnCardService {
	/**
	 * 实际退卡处理
	 */
	void handle(CardRequestDto cardParam);
}

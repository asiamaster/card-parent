package com.dili.card.service;

import com.dili.card.dto.AccountPermissionRequestDto;
import com.dili.card.dto.CardRequestDto;

import java.util.Optional;

/**
 * 卡账户管理操作
 */
public interface IAccountManageService {
	/**
	 * 冻结账户
	 */
	void frozen(CardRequestDto cardRequestDto);

	/**
	 * 解冻账户
	 */
	void unfrozen(CardRequestDto cardRequestDto);

	/**
	 * 设置卡账户权限
	 * @param requestDto
	 * @return
	 */
	Optional<String> saveCardPermission(AccountPermissionRequestDto requestDto);
}

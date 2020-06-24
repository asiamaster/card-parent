package com.dili.card.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dili.card.dto.CardRequestDto;
import com.dili.card.rpc.resolver.CardManageRpcResolver;
import com.dili.card.service.IPasswordService;

/**
 * @description: 密码相关的操作
 */
@Service
public class PasswordServiceImpl implements IPasswordService{

	@Resource
	private CardManageRpcResolver cardManageRpcResolver;

	@Override
	public void resetLoginPwd(CardRequestDto cardRequestDto) throws Exception {
		cardManageRpcResolver.resetLoginPwd(cardRequestDto);
	}
}

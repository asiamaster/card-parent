package com.dili.card.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.card.dto.CardRequestDto;
import com.dili.card.rpc.resolver.CardManageRpcResolver;
import com.dili.card.service.IPasswordService;
import com.dili.card.service.ISerialService;

/**
 * @description: 密码相关的操作
 */
@Service
public class PasswordServiceImpl implements IPasswordService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CardManageServiceImpl.class);

	@Resource
	private CardManageRpcResolver cardManageRpcResolver;
	@Autowired
	private ISerialService serialService;

	@Override
	public void resetLoginPwd(CardRequestDto cardRequestDto) throws Exception {
		//重置密码
		try {
			cardManageRpcResolver.resetLoginPwd(cardRequestDto);
		} catch (Exception e) {
			LOGGER.error("重置密码失败");
		}
		//记录操作记录
	}
}

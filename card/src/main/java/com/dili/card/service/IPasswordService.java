package com.dili.card.service;

import com.dili.card.dto.CardRequestDto;

/**
 * @description： 重置密码修改密码等
 *
 * @author ：WangBo
 * @time ：2020年4月26日下午5:52:54
 */
public interface IPasswordService {

	/**
	 * @description：重置登陆密码
	 */
	void resetLoginPwd(CardRequestDto cardRequestDto) throws Exception;
}

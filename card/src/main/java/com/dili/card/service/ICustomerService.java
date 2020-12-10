package com.dili.card.service;

import java.util.ArrayList;
import java.util.List;

import com.dili.card.dto.UserCashDto;
import com.dili.card.entity.UserCashDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.type.CashAction;
import com.dili.customer.sdk.domain.CharacterType;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.google.common.collect.Lists;

/**
 * @description： 客户系统相关操作
 * 
 * @author ：WangBo
 * @time ：2020年12月9日下午6:00:05
 */
public interface ICustomerService {

	/**
	 * 将从客户系统中获取的角色集合转为以逗号分割的字符串
	 * 
	 * @param types
	 * @return
	 */
	public String getCharacterTypes(List<CharacterType> typeList, Long customerId);

	/**
	 * 得到子身份类型集合
	 */
	public List<String> getSubTypes(List<CharacterType> typeList);

	/**
	 * 获取客户身份类型显示的值，从数据字典中获取该值，每个客户包含多个该类型
	 */
	public String getSubTypeName(List<CharacterType> typeList, Long firmId);
}

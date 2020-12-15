package com.dili.card.service;

import java.util.List;
import java.util.Map;

import com.dili.customer.sdk.domain.CharacterType;

/**
 * @description： 客户系统相关操作
 * 
 * @author ：WangBo
 * @time ：2020年12月9日下午6:00:05
 */
public interface ICustomerService {
	
	/**
	 * 获取客户子类型，以逗号分割
	 * @param cid
	 * @param firmId
	 * @return
	 */
	public String getSubTypeNames(Long cid, Long firmId);
	
	/**
	 * 批量获取客户子类型，以逗号分割
	 * @param cidList
	 * @param firmId
	 * @return key=cId,value=subTypeName
	 */
	public Map<Long, String> getSubTypeNames(List<Long> cidList, Long firmId);

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

package com.dili.card.service;

import com.dili.assets.sdk.dto.TypeMarketDto;

/**
 * @description：获取市场配置的收益帐户接口 
 *          
 * @author     ：WangBo
 * @time       ：2021年1月15日上午9:59:16
 */
public interface ITypeMarketService {

	/**
	 * 获取市场配置的收益帐户,没有则返回null
	 * @param firmId
	 * @return
	 */
	public TypeMarketDto getmarket(String code);
	
	/**
	 * 获取市场配置的收益帐户id没有则返回null
	 * @param firmId
	 * @return
	 */
	public Long getmarketId(String code);
}

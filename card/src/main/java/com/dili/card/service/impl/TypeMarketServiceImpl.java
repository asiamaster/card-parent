package com.dili.card.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dili.assets.sdk.dto.TypeMarketDto;
import com.dili.assets.sdk.rpc.TypeMarketRpc;
import com.dili.card.common.constant.Constant;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.ITypeMarketService;

/**
 * @description：获取当前市场配置的收益帐户信息
 * 
 * @author ：WangBo
 * @time ：2021年1月15日上午9:56:37
 */
@Service
public class TypeMarketServiceImpl implements ITypeMarketService {
	private static final Logger log = LoggerFactory.getLogger(TypeMarketServiceImpl.class);

	@Autowired
	private TypeMarketRpc typeMarketRpc;

	@Override
	public TypeMarketDto getmarket(String code) {
		TypeMarketDto typeMarket = null;
		try {
			List<TypeMarketDto> marketList = GenericRpcResolver.resolver(typeMarketRpc.queryAll(), ServiceName.ASSETS);
			log.info("获取当前市场配置的收益帐户信息 *****", JSONObject.toJSONString(marketList));
			for (TypeMarketDto dto : marketList) {
				if (Constant.CARD_INCOME_ACCOUNT.equalsIgnoreCase(dto.getType())) {
					typeMarket = dto;
					break;
				}
			}
		} catch (Exception e) {
			log.error("获取市场收益帐户失败：", e);
			throw new CardAppBizException("获取市场收益帐户失败");
		}
		return typeMarket;
	}

	@Override
	public Long getmarketId(String code) {
		TypeMarketDto getmarket = getmarket(code);
		if(getmarket == null) {
			return null;
		}
		return getmarket.getMarketId();
	}

}

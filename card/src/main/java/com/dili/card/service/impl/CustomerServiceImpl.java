package com.dili.card.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.ICustomerService;
import com.dili.customer.sdk.domain.CharacterType;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;
import com.google.common.collect.Lists;

@Service("customerService")
public class CustomerServiceImpl implements ICustomerService {

	private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Resource
	private CustomerRpc customerRpc;
	@Autowired
	private DataDictionaryRpc dataDictionaryRpc;

	public String getCharacterTypes(List<CharacterType> typeList, Long customerId) {
		if (typeList == null || typeList.size() == 0) {
			throw new CardAppBizException("客户ID{}角色信息为空", customerId);
		}
		List<String> typesList = new ArrayList<String>();
		typeList.forEach(type -> {
			typesList.add(type.getCharacterType());
		});
		return String.join(",", typesList);
	}

	public List<String> getSubTypes(List<CharacterType> typeList) {
		if (typeList == null || typeList.size() == 0) {
			return Lists.newArrayList();
		}
		List<String> typesList = new ArrayList<String>();
		typeList.forEach(type -> {
			typesList.add(type.getSubType());
		});
		return typesList;
	}

	public String getSubTypeName(List<CharacterType> typeList, Long firmId) {
		if (typeList == null || typeList.size() == 0) {
			return "";
		}
		List<String> subTypeNameList = Lists.newArrayList();
		typeList.forEach(characterType -> {
			DataDictionaryValue ddv = DTOUtils.newInstance(DataDictionaryValue.class);
			ddv.setDdCode(characterType.getCharacterType());
			ddv.setCode(characterType.getSubType());
			ddv.setFirmId(firmId);
			List<DataDictionaryValue> ddList = GenericRpcResolver
					.resolver(dataDictionaryRpc.listDataDictionaryValue(ddv), "DataDictionaryRpc");
			if (ddList == null || ddList.size() == 0) {
				log.warn("数据字典中没找到该客户身份类型CharacterType[{}]SubType[{}]", characterType.getCharacterType(),
						characterType.getSubType());
			} else {
				if (ddList.size() > 1) {
					log.warn("数据字典中配置了多个身份类型CharacterType[{}]SubType[{}]，只取第一个", characterType.getCharacterType(),
							characterType.getSubType());
				}
				subTypeNameList.add(ddList.get(0).getName());
			}
		});
		return String.join(",", subTypeNameList);
	}

}

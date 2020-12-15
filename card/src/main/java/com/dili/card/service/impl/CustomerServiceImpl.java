package com.dili.card.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.cache.CharacterTypeCache;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.CustomerRpcResolver;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.ICustomerService;
import com.dili.customer.sdk.domain.CharacterType;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
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
	@Autowired
	private CharacterTypeCache characterTypeCache;
	@Autowired
	private CustomerRpcResolver customerRpcResolver;

	public String getCharacterTypes(List<CharacterType> typeList, Long customerId) {
		if (CollectionUtils.isEmpty(typeList)) {
			throw new CardAppBizException("客户ID{}角色信息为空", customerId);
		}
		List<String> typesList = new ArrayList<String>();
		typeList.forEach(type -> {
			typesList.add(type.getCharacterType());
		});
		return String.join(",", typesList);
	}

	public List<String> getSubTypes(List<CharacterType> typeList) {
		if (CollectionUtils.isEmpty(typeList)) {
			return Lists.newArrayList();
		}
		List<String> typesList = new ArrayList<String>();
		typeList.forEach(type -> {
			typesList.add(type.getSubType());
		});
		return typesList;
	}

	public String getSubTypeName(List<CharacterType> typeList, Long firmId) {
		if (CollectionUtils.isEmpty(typeList)) {
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

	@Override
	public Map<Long, String> getSubTypeNames(List<Long> cidList, Long firmId) {
		Map<Long, String> subTypeMap = new HashMap<Long, String>();
		List<CustomerExtendDto> clist = customerRpcResolver.findCustomerByIds(cidList, firmId);
		List<DataDictionaryValue> subTypeDD = characterTypeCache.getSubTypeList(firmId);
		clist.forEach(c -> {
			List<String> subTypeList = new ArrayList<String>();
			for (CharacterType ctype : c.getCharacterTypeList()) {
				if (StringUtils.isBlank(ctype.getSubType())) {
					// 客户子类型允许为空
					continue;
				}
				log.info(">>>>>>>>>>>>>>>{}",JSONObject.toJSONString(ctype));
				for (DataDictionaryValue dd : subTypeDD) {
					if (ctype.getCharacterType().equals(dd.getDdCode()) && ctype.getSubType().equals(dd.getCode())) {
						subTypeList.add(dd.getName());
						break;
					}
				}
			}
			subTypeMap.put(c.getId(), String.join(",", subTypeList));
		});
		return subTypeMap;
	}

	@Override
	public String getSubTypeNames(Long cid, Long firmId) {
		Map<Long, String> dataMap = getSubTypeNames(Lists.newArrayList(cid), firmId);
		return dataMap.get(cid);
	}
}

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
import com.dili.card.service.ICustomerService;
import com.dili.customer.sdk.domain.CharacterType;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.google.common.collect.Lists;

@Service("customerService")
public class CustomerServiceImpl implements ICustomerService {

	private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Resource
	private CustomerRpc customerRpc;
	@Autowired
	private CharacterTypeCache characterTypeCache;
	@Autowired
	private CustomerRpcResolver customerRpcResolver;

	public String convertCharacterTypes(List<CharacterType> typeList, Long customerId) {
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

	@Override
	public String getSubTypeCodes(Long cid, Long firmId) {
		CustomerExtendDto customer = customerRpcResolver.findCustomerById(cid, firmId);
		List<String> subTypes = getSubTypes(customer.getCharacterTypeList());
		return String.join(",", subTypes);
	}
	
	public String getSubTypeName(List<CharacterType> typeList, Long firmId) {
		if (CollectionUtils.isEmpty(typeList)) {
			return "";
		}
		// 获取缓存中的数据字典角色及身份信息
		List<DataDictionaryValue> ddList = characterTypeCache.getSubTypeList(firmId);
		log.info("市场{}客户角色身份信息{}", firmId, JSONObject.toJSONString(ddList));
		
		List<String> subTypeNameList = Lists.newArrayList();
		typeList.forEach(characterType -> {
			for (DataDictionaryValue dd : ddList) {
				if (characterType.getCharacterType().equals(dd.getDdCode())) {
					subTypeNameList.add(dd.getName());
				}
			}
		});
		return String.join(",", subTypeNameList);
	}

	@Override
	public Map<Long, String> getSubTypeNames(List<Long> cidList, Long firmId) {
		Map<Long, String> subTypeMap = new HashMap<Long, String>();
		List<CustomerExtendDto> clist = customerRpcResolver.findCustomerByIds(cidList, firmId);
		List<DataDictionaryValue> subTypeDD = characterTypeCache.getSubTypeList(firmId);
		log.info("市场{}客户角色身份信息{}",firmId,JSONObject.toJSONString(subTypeDD));
		clist.forEach(c -> {
			List<String> subTypeList = new ArrayList<String>();
			for (CharacterType ctype : c.getCharacterTypeList()) {
				if (StringUtils.isBlank(ctype.getCharacterType()) || StringUtils.isBlank(ctype.getSubType())) {
					// 客户子类型允许为空,  部分数据没有角色
					subTypeMap.put(c.getId(), "");
					continue;
				}
				if(CollectionUtils.isEmpty(subTypeDD)) {
					continue;
				}
				for (DataDictionaryValue dd : subTypeDD) {
//					System.out.println(c.getId()+"="+ctype.getCharacterType()+"="+ctype.getSubType()+"="+dd.getDdCode()+"="+dd.getCode());
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

package com.dili.card.common.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.dili.card.common.constant.ServiceName;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LRUCache;

/**
 * @description： 客户角角身份类型缓存
 *          
 * @author     ：WangBo
 * @time       ：2020年12月14日上午10:04:56
 */
@Component
public class CharacterTypeCache {
	@Autowired
	private DataDictionaryRpc dataDictionaryRpc;
	
	static LRUCache<Long, List<DataDictionaryValue>> dataCache = CacheUtil.newLRUCache(10, 600000);
	/**
	 * 根据全市场通用角色（买家、经营户、其他类型）,获取角色下各市场的子类型
	 * @param firmId 市场ID
	 * @return
	 */
	public List<DataDictionaryValue> getSubTypeList(Long firmId){
		List<DataDictionaryValue> ddList = dataCache.get(firmId);
		if(CollectionUtils.isEmpty(ddList)) {
			// 重新从数据字典获取，并且设置超时时间30分钟
			List<DataDictionaryValue> buyList = getByCharacterTypeCode(firmId, CustomerEnum.CharacterType.买家.getCode());
			List<DataDictionaryValue> businessList = getByCharacterTypeCode(firmId, CustomerEnum.CharacterType.经营户.getCode());
			List<DataDictionaryValue> otherList = getByCharacterTypeCode(firmId, CustomerEnum.CharacterType.其他类型.getCode());
			buyList.addAll(businessList);
			buyList.addAll(otherList);
			dataCache.put(firmId, buyList, 1800000);
			return buyList;
		}else {
			return ddList;
		}
	}
	
	/**
	 * 从数据字典中获取角色下各市场的子类型
	 * @param firmId
	 * @param characterTypeCode 角色编码
	 * @return
	 */
	private List<DataDictionaryValue> getByCharacterTypeCode(Long firmId,String characterTypeCode){
		DataDictionaryValue ddv = DTOUtils.newInstance(DataDictionaryValue.class);
		ddv.setDdCode(characterTypeCode);
		ddv.setFirmId(firmId);
		List<DataDictionaryValue> list = GenericRpcResolver.resolver(dataDictionaryRpc.listDataDictionaryValue(ddv),ServiceName.DD);
		return list;
	}
	
}

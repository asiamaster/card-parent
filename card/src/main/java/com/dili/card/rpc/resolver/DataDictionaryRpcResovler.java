package com.dili.card.rpc.resolver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.dili.card.exception.CardAppBizException;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;

/**
 * 数据字典
 * 
 * @author zhangxing
 */
@Component
public class DataDictionaryRpcResovler {

	@Autowired
	private DataDictionaryRpc dataDictionaryRpc;

	/**
	 * 根据业务类型获取数据字典
	 * 
	 * @param type
	 * @return
	 */
	public String findByDataDictionaryValue(String ddCode) {
		BaseOutput<List<DataDictionaryValue>> baseOutput = dataDictionaryRpc.listDataDictionaryValueByDdCode(ddCode);
		if (!baseOutput.isSuccess()) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "远程调用数据字典服务失败");
		}
		if (CollectionUtils.isEmpty(baseOutput.getData())) {
			return null;
		}
		return baseOutput.getData().get(0).getCode();
	}

	/**
	 * 根据参数获取字典列表值
	 * @param ddv
	 * @return
	 */
	public List<DataDictionaryValue> findByDataDictionaryValue(DataDictionaryValue ddv) {
		BaseOutput<List<DataDictionaryValue>> baseOutput = dataDictionaryRpc.listDataDictionaryValue(ddv);
		if (!baseOutput.isSuccess()) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "远程调用数据字典服务失败");
		}
		if (CollectionUtils.isEmpty(baseOutput.getData())) {
			return null;
		}
		return baseOutput.getData();
	}
}

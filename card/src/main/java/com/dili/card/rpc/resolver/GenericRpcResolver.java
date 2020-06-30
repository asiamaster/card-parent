package com.dili.card.rpc.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.exception.ErrorCode;
import com.dili.ss.domain.BaseOutput;

/**
 * @description： 通用RPC结果解析
 * 
 * @author ：WangBo
 * @time ：2020年6月30日下午2:28:22
 */
public class GenericRpcResolver {

	 static Logger log = LoggerFactory.getLogger(GenericRpcResolver.class);

	/**
	 * 判断baseOutput.isSuccess()为false则抛出异常
	 * 
	 * @param <T>
	 * @param baseOutput
	 * @return
	 */
	public static <T> T resolver(BaseOutput<T> baseOutput) {
		if (!baseOutput.isSuccess()) {
			log.error("远程服务返回了一个错误![{}]",JSONObject.toJSONString(baseOutput));
			throw new CardAppBizException(ErrorCode.SERVICE_CODE, "远程服务返回了一个错误!");
		}
		return baseOutput.getData();
	}

	/**
	 * 判断baseOutput.isSuccess()为false则抛出异常
	 * 
	 * @param <T>
	 * @param baseOutput
	 * @param errorMsg
	 * @return
	 */
	public static <T> T resolver(BaseOutput<T> baseOutput, String errorMsg) {
		if (!baseOutput.isSuccess()) {
			log.error("远程服务返回了一个错误![{}]",JSONObject.toJSONString(baseOutput));
			throw new CardAppBizException(ErrorCode.SERVICE_CODE, errorMsg);
		}
		return baseOutput.getData();
	}
}

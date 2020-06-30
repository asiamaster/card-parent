package com.dili.card.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description： 业务异常代理类，增加日志输出及参数处理
 * 
 * @author ：WangBo
 * @time ：2020年6月29日下午2:56:18
 */
public class BizExceptionProxy {

	private static final Logger log = LoggerFactory.getLogger(CardAppBizException.class);

	/** */

	public static CardAppBizException exception(int code, String msg) {
		return new CardAppBizException(code + "", msg.replace("{}", ""));
	}

	/**
	 * 抛出默认错误码（100000）异常，并打印日志 <br>
	 * <i>由于前端可能直接使用该异常信息，有些信息不需要用户看到，所以抛出的异常信息会去掉占位符</i>
	 * 
	 * @param msg 错误信息，可使用log4j格式占位符
	 * @param obj 占位符日志参数
	 */
	public static CardAppBizException exception(String msg, Object... obj) {
		log.error(msg, obj);
		return new CardAppBizException(ErrorCode.GENERAL_CODE + "", msg.replace("{}", ""));
	}

	public static CardAppBizException exception(String msg) {
		return new CardAppBizException(msg.replace("{}", ""));
	}

}

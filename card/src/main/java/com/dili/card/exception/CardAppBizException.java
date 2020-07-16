package com.dili.card.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description： 业务异常
 *
 * @author ：WangBo
 * @time ：2019年6月22日下午4:22:55
 */
public class CardAppBizException extends RuntimeException {
    private static final Logger log = LoggerFactory.getLogger(CardAppBizException.class);
    /**错误code码*/
    private String code;

    /** */
    private static final long serialVersionUID = 4248326393464652492L;

    /**
     * Constructor for AopConfigException.
     *
     * @param msg the detail message
     */
    public CardAppBizException(String code, String msg) {
        super(msg.replace("{}", ""));
        this.code = code;
    }

    public CardAppBizException(String msg) {
        super(msg.replace("{}", ""));
        this.code = ErrorCode.GENERAL_CODE;
    }

    /**
     * 抛出默认错误码（100000）异常，并打印日志
     * <br><i>由于前端可能直接使用该异常信息，有些信息不需要用户看到，所以抛出的异常信息会去掉占位符</i>
     * @param msg 错误信息，可使用log4j格式占位符
     * @param obj 占位符日志参数
     */
    public CardAppBizException(String msg, Object... obj) {
        super(msg.replace("{}", ""));
        this.code = ErrorCode.GENERAL_CODE;
        log.error(msg, obj);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    public String getCode() {
        return code;
    }
//
//	public AppException(String msg, Throwable cause)
//	{
//		super(msg, cause);
//	}
//
//	public AppException(Throwable cause) {
//        super(cause);
//    }
}

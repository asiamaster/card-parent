package com.dili.card.service;

import com.dili.card.type.OperateType;
import com.dili.uap.sdk.domain.UserTicket;

/**
 * @description： 通过BusinessLogRpc保存操作日志
 * 
 * @author ：WangBo
 * @time ：2020年10月11日上午11:22:49
 */
public interface IBusinessLogService {
	/**
	 * 通过BusinessLogRpc保存操作日志
	 * <br>操作人等基本参数通过SessionContext.getSessionContext().getUserTicket();
	 * @param operateType
	 * @param content
	 */
	void saveLog(OperateType operateType, String content);

	/**
	 * 通过BusinessLogRpc保存操作日志
	 */
	void saveLog(UserTicket user, OperateType operateType, String content);
}

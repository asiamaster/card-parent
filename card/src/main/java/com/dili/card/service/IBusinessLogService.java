package com.dili.card.service;

import com.dili.card.type.LogOperationType;
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
	 * 通过BusinessLogRpc保存操作日志，
	 * <br><i>操作日志类型默认为已提交，对应数据字典 - 日志服务系统 - 操作日志类型
	 * @param operateType 对应数据字典 - 日志服务系统 - 业务类型
	 * @param userTicket 当前用户
	 * @param content 日志内容,如   金额:30,数量:20
	 */
	void saveLog(OperateType operateType, UserTicket userTicket, String... content);
	
	/**
	 * 通过BusinessLogRpc保存操作日志
	 * @param operateType  对应数据字典 - 日志服务系统 - 业务类型
	 * @param logOperationType  对应数据字典 - 日志服务系统 - 操作日志类型
	 * @param userTicket 当前用户
	 * @param content 日志内容,如   金额:30,数量:20
	 */
	void saveLog(OperateType operateType,LogOperationType logOperationType, UserTicket userTicket, String... content);
}

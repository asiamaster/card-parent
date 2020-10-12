package com.dili.card.type;

/**
 * @description： 日志服务系统操作类型对应 <br>
 * 与数据字典 - 日志服务系统 - 操作日志类型
 * 
 * @author ：WangBo
 * @time ：2020年10月11日下午3:21:09
 */
public enum LogOperationType {
	/** 注销 */
	ADD("add", "新增"),
	/** 有效 */
	DEL("del", "删除"),
	/** 禁用 */
	EDIT("edit", "修改");

	private String code;
	private String name;

	private LogOperationType(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}
}

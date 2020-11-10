package com.dili.card.common.constant;

import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

/**
 * @description： 
 *          toJsonString时需要排除的字段
 * @author ：WangBo
 * @time ：2020年11月10日上午11:22:00
 */
public class JsonExcludeFilter {
	/** json时需要排除的字段 */
	private static String[] FIELDS= {"tradePwd","loginPwd","parentLoginPwd","secondLoginPwd"};
	
	/** 排除密码类型字段 */
	public static final SimplePropertyPreFilter FILTER = new SimplePropertyPreFilter(FIELDS);
}

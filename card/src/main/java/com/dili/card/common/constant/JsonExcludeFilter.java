package com.dili.card.common.constant;

import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

/**
 * @description： 
 *          toJsonString时需要排除的字段
 * @author ：WangBo
 * @time ：2020年11月10日上午11:22:00
 */
public class JsonExcludeFilter {
	
	/** 排除密码类型字段 */
	public static final SimplePropertyPreFilter PWD_FILTER = new SimplePropertyPreFilter();
	static {
		/** 排除密码类型字段 */
		PWD_FILTER.getExcludes().add("tradePwd");
		PWD_FILTER.getExcludes().add("loginPwd");
		PWD_FILTER.getExcludes().add("parentLoginPwd");
		PWD_FILTER.getExcludes().add("secondLoginPwd");
		PWD_FILTER.getExcludes().add("oldLoginPwd");
	}
}

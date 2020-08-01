package com.dili.card.service;

import java.math.BigDecimal;

import com.dili.card.type.RuleFeeBusinessType;
import com.dili.card.type.SystemSubjectType;

/**
 * @description： 费用规则服务
 * 
 * @author ：WangBo
 * @time ：2020年7月14日下午4:59:44
 */
public interface IRuleFeeService {

	/**
	 * 从费用规则系统查询收费项 ，如果有多个收费项时只匹配第一个systemSubjectType<br>
	 * 当没查询到收费项时返回0，如果查询到有收费项但没有与systemSubjectType匹配上将抛出内容为errorMsg的异常
	 * 
	 * @param amount              操作金额（针对充值、提现或单个条件计算金额）,在配置规则条件时需要写死为"amount"
	 * @param ruleFeeBusinessType 业务类型，该值在数据字典-基础数据中心-业务类型中配置
	 * @param systemSubjectType   系统科目，各业务系统定义好值，在基础数据中心-业务费用项管理添加费用项时会选择该一个类型，在查询到费用项时用于该费用项与本地各业务类型的对应
	 * @return
	 */
	public BigDecimal getRuleFee(Long amount, RuleFeeBusinessType ruleFeeBusinessType,
			SystemSubjectType systemSubjectType);
	
	/**
	 * 从费用规则系统查询收费项 ，如果有多个收费项时只匹配第一个systemSubjectType<br>
	 * 当没查询到收费项时返回0，如果查询到有收费项但没有与systemSubjectType匹配上将抛出内容为errorMsg的异常
	 * 
	 * @param ruleFeeBusinessType 业务类型，该值在数据字典-基础数据中心-业务类型中配置
	 * @param systemSubjectType   系统科目，各业务系统定义好值，在基础数据中心-业务费用项管理添加费用项时会选择该一个类型，在查询到费用项时用于该费用项与本地各业务类型的对应
	 * @return
	 */
	public BigDecimal getRuleFee(RuleFeeBusinessType ruleFeeBusinessType,
			SystemSubjectType systemSubjectType);

}

package com.dili.card.service;

/**
 * @description： 费用规则服务
 * 
 * @author ：WangBo
 * @time ：2020年7月14日下午4:59:44
 */
public interface IRuleFeeService {

	/**
	 * 获取开卡费用（目前寿光开卡只有工本费）
	 * 
	 * @return
	 */
	public Long getOpenCardFee(Long firmId);

	
}

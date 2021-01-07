package com.dili.card.service;

import com.dili.card.dto.CardRequestDto;

/**
 * @description： 卡片管理服务，包括退卡，换卡，补卡，挂失，解挂
 *
 * @author ：WangBo
 * @time ：2020年4月26日下午5:59:10
 */
public interface ICardManageService {

	/**
	 * 解挂卡片
	 */
	String unLostCard(CardRequestDto cardParam);

	/**
	 * 退卡
	 */
	String returnCard(CardRequestDto cardParam);

	/**
	 * @description：重置登陆密码
	 */
	String resetLoginPwd(CardRequestDto cardParam);
	
	
	/**
	 * @description：修改密码
	 */
	String modifyLoginPwd(CardRequestDto cardParam);

	/**
	 * 解锁卡片
	 * @param cardParam
	 */
	String unLockCard(CardRequestDto cardParam);
	/**
	* 挂失
	* @author miaoguoxin
	* @date 2020/7/14
	*/
    String reportLossCard(CardRequestDto cardParam);

    /**
    * 换卡
    * @author miaoguoxin
    * @date 2020/7/29
    */
    String changeCard(CardRequestDto cardParam);

	/**
	 * 查询换卡工本费(返回金额：分)
	 * @return
	 */
	Long getChangeCardCostFee();
}

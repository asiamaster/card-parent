package com.dili.card.service;

import com.dili.card.dto.OpenCardDto;
import com.dili.card.dto.OpenCardResponseDto;

/**
 * @description： 用户开卡service接口
 * 
 * @author ：WangBo
 * @time ：2020年6月30日上午11:32:59
 */
public interface IOpenCardService {

	/**
	 * 开主卡
	 * 
	 * @param openCardInfo
	 * @return
	 * @throws InterruptedException
	 * @throws Exception
	 */
	OpenCardResponseDto openCard(OpenCardDto openCardInfo);

	/**
	 * 查询开卡工本费
	 * 
	 * @return
	 */
	Long getOpenCostFee();

	/**
	 * 校验新卡状态,卡类型，卡面与客户类型
	 * 
	 * @param openCardInfo
	 */
	void checkNewCardNo(OpenCardDto openCardInfo);

}

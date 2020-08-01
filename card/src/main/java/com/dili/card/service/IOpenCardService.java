package com.dili.card.service;

import com.dili.card.dto.OpenCardDto;
import com.dili.card.dto.OpenCardResponseDto;

/**
 * @description： 
 *          用户开卡service接口
 * @author ：WangBo
 * @time ：2020年6月30日上午11:32:59
 */
public interface IOpenCardService {


	/**
	 * 开主卡
	 * @param openCardInfo
	 * @return
	 * @throws InterruptedException 
	 * @throws Exception 
	 */
	public OpenCardResponseDto openMasterCard(OpenCardDto openCardInfo);

	/**
	 * 开副卡
	 * @param openCardInfo
	 * @return
	 */
	public OpenCardResponseDto openSlaveCard(OpenCardDto openCardInfo);
	
	/**
	 * 查询开卡工本费
	 * @return
	 */
	public Long getOpenCostFee();

}

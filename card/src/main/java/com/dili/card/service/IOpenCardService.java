package com.dili.card.service;

import com.dili.card.dto.CustomerResponseDto;
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
	 * 根据证件号在客户系统中获取客户信息。 <i><br>
	 * 未找到、已禁用、已注销的客户将抛出业务异常
	 * 
	 * @param certificateNumber 证件号
	 * @param firmId            市场ID
	 * @return
	 */
	CustomerResponseDto getCustomerInfoByCertificateNumber(String certificateNumber, Long firmId);

	/**
	 * 根据客户ID判断当前客户拥有几张主卡。根据各市场不同配置。<br>
	 * 如果当前客户拥有的卡数量已达到最大配置将抛出业务异常
	 * 
	 * @param customerResponseDto
	 * @return 当前持有主卡数量 
	 */
	Integer checkCardNum(CustomerResponseDto customerResponseDto);

}

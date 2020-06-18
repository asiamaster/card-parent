package com.dili.card.service;

import com.dili.card.dto.FundContractRequestDto;

/**
 * 合同管理
 * @author apache
 *
 */
public interface IContractService {

	/**
	 * 保存
	 */
	void save(FundContractRequestDto fundContractRequest);

	/**
	 * 列表
	 */
	void list(FundContractRequestDto fundContractRequest);

	/**
	 * 解除合同
	 */
	void remove(FundContractRequestDto fundContractRequest);

	/**
	 * 合同详情
	 */
	void detail(FundContractRequestDto fundContractRequest);

}

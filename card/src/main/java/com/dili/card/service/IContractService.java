package com.dili.card.service;

import java.util.List;

import com.dili.card.dto.FundContractRequestDto;
import com.dili.card.dto.FundContractResponseDto;

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
	List<FundContractResponseDto> list(FundContractRequestDto fundContractRequest);

	/**
	 * 解除合同
	 */
	void remove(FundContractRequestDto fundContractRequest);

	/**
	 * 合同详情
	 */
	FundContractResponseDto detail(FundContractRequestDto fundContractRequest);

}

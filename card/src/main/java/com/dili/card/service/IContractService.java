package com.dili.card.service;

import java.util.List;

import com.dili.card.dto.FundContractQueryDto;
import com.dili.card.dto.FundContractRequestDto;
import com.dili.card.dto.FundContractResponseDto;
import com.dili.ss.domain.PageOutput;

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
	List<FundContractResponseDto> list(FundContractQueryDto contractQueryDto);
	
	/**
	 * 分页
	 */
	PageOutput<List<FundContractResponseDto>> page(FundContractQueryDto contractQueryDto);

	/**
	 * 解除合同
	 */
	void remove(FundContractRequestDto fundContractRequest);

	/**
	 * 合同详情
	 */
	FundContractResponseDto detail(Long id);

	/**
	 * 获取解除双方的信息
	 */
	FundContractResponseDto removeToPage(Long id);

	/**
	 * 关闭过期合同
	 * @return
	 */
	void closeOverdueContract();

	/**
	 * 激活合同
	 */
	void activeOverdueContract();

}

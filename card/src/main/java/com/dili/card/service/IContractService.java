package com.dili.card.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dili.card.dto.FundContractPrintDto;
import com.dili.card.dto.FundContractQueryDto;
import com.dili.card.dto.FundContractRequestDto;
import com.dili.card.dto.FundContractResponseDto;
import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.domain.dto.CustomerQueryInput;
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
	 * 关闭过期合同
	 * @return
	 */
	void closeOverdueContract();

	/**
	 * 激活合同
	 */
	void activeOverdueContract();
	
	/**
	 * 获取客户信息
	 */
	Customer findCustomers(CustomerQueryInput query);

	/**
	 * 通过账号查询合同
	 */
	FundContractResponseDto findActiveContractByAccountId(FundContractQueryDto contractQueryDto);

	/**
	 * 合同打印
	 */
	FundContractPrintDto print(Long id);

	/**
	 * 文件上传
	 */
	String upload(MultipartFile multipartFile);

	/**
	 * 合同预览
	 */
	FundContractResponseDto preview(Long id);

}

package com.dili.card.service.impl;

import com.dili.card.dao.IFundConsignorDao;
import com.dili.card.dao.IFundContractDao;
import com.dili.card.dto.FundConsignorRequestDto;
import com.dili.card.dto.FundContractRequestDto;
import com.dili.card.dto.FundContractResponseDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.entity.FundConsignorDo;
import com.dili.card.entity.FundContractDo;
import com.dili.card.rpc.resolver.AccountQueryRpcResolver;
import com.dili.card.rpc.resolver.CustomerRpcResolver;
import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.service.IContractService;
import com.dili.card.type.BizNoType;
import com.dili.card.type.ContractState;
import com.dili.customer.sdk.domain.Customer;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.DateUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContractServiceImpl implements IContractService {
	
	@Autowired
	private IFundConsignorDao fundConsignorDao;
	@Autowired
	private IFundContractDao contractDao;
	@Autowired
	private UidRpcResovler uidRpcResovler;
	@Autowired
	private CustomerRpcResolver customerRpcResolver;
	@Autowired
	private AccountQueryRpcResolver accountQueryRpcResolver;

	@Override
	public void save(FundContractRequestDto fundContractRequest) {
		//构建合同主体
		FundContractDo fundContract = this.buildContractEntity(fundContractRequest);
		//构建被委托人主体
		List<FundConsignorDo> consignors = this.buildConsignorEntities(fundContractRequest);
		//数据保存操作
		contractDao.save(fundContract);
		fundConsignorDao.saveBatch(consignors);
	}

	@Override
	public List<FundContractResponseDto> list(FundContractRequestDto fundContractRequest) {
		this.buildQueryContractConditon(fundContractRequest);
		List<FundContractDo> fundContracts = contractDao.findEntityByCondition(fundContractRequest);
		List<FundContractResponseDto> fundResponseContracts = this.huildResponseContracts(fundContracts);
		return fundResponseContracts;
	}

	@Override
	public void remove(FundContractRequestDto fundContractRequest) {
		FundContractDo fundContract = contractDao.getById(fundContractRequest.getId());
		if (fundContract == null) {
			throw new BusinessException(ResultCode.DATA_ERROR, "该合同号不存在");
		}
		if (ContractState.REMOVED.getCode() == fundContract.getState()) {
			return;
		}
		FundContractDo updateFundContract = new FundContractDo();
		updateFundContract.setId(fundContractRequest.getId());
		updateFundContract.setState(ContractState.REMOVED.getCode());
		contractDao.update(updateFundContract);
	}

	@Override
	public FundContractResponseDto detail(FundContractRequestDto fundContractRequest) {
		FundContractDo fundContract = contractDao.getById(fundContractRequest.getId());
		if (fundContract == null) {
			throw new BusinessException(ResultCode.DATA_ERROR, "该合同号不存在");
		}
		FundContractResponseDto contractResponse = this.buildContractDetail(fundContract);
		return contractResponse;
	}

	/**
	 * 构建页面合同详情数据
	 */
	private FundContractResponseDto buildContractDetail(FundContractDo fundContract) {
		return this.buildContractResponse(fundContract, true);
	}

	/**
	 * 构建页面相应数据
	 */
	private FundContractResponseDto buildContractResponse(FundContractDo fundContractDo, boolean detail) {
		FundContractResponseDto contractResponseDto = new FundContractResponseDto();
		contractResponseDto.setContractNo(fundContractDo.getContractNo());
		contractResponseDto.setCreator(fundContractDo.getCreator());
		contractResponseDto.setCreateTime(fundContractDo.getCreateTime());
		contractResponseDto.setStartTime(fundContractDo.getStartTime());
		contractResponseDto.setEndTime(fundContractDo.getEndTime());
		
		List<FundConsignorDo> consignors = fundConsignorDao.FindConsignorsByContractNo(fundContractDo.getContractNo());
		
		if (!detail) {
			
			for (FundConsignorDo fundConsignorDo : consignors) {
				
			}
			//构建卡数据
			UserAccountCardResponseDto userAccountCardResponseDto = accountQueryRpcResolver.findSingleUserCard(fundContractDo.getConsignorAccountId());
			contractResponseDto.setConsignorCard(userAccountCardResponseDto.getCardNo());
			//获取客户信息
			Customer customer = customerRpcResolver.findCustomerById(userAccountCardResponseDto.getCustomerId());
			contractResponseDto.setConsignorName(customer.getName());
		}
		return null;
	}

	/**
	 * 构建被委托人信息列表
	 */
	private List<FundConsignorDo> buildConsignorEntities(FundContractRequestDto fundContractRequest) {
		List<FundConsignorDo> consignorDos = new ArrayList<FundConsignorDo>();
		for (FundConsignorRequestDto consignorRequestDto : fundContractRequest.getConsignors()) {
			consignorDos.add(this.buildConsignorEntity(consignorRequestDto, fundContractRequest.getContractNo()));
		}
		return consignorDos;
	}

	/**
	 * 构建被委托人信息
	 */
	private FundConsignorDo buildConsignorEntity(FundConsignorRequestDto consignorRequestDto, String contractNo) {
		FundConsignorDo fundConsignorDo = new FundConsignorDo();
		//构建合同被委托人核心数据
		fundConsignorDo.setConsigneeIdCode(consignorRequestDto.getConsigneeIdCode());
		fundConsignorDo.setConsigneeIdMobile(consignorRequestDto.getConsigneeIdMobile());
		fundConsignorDo.setConsigneeName(consignorRequestDto.getConsigneeName());
		fundConsignorDo.setSignatureImagePath(consignorRequestDto.getSignatureImagePath());
		//构建商户信息
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		fundConsignorDo.setFirmId(userTicket.getFirmId());
		fundConsignorDo.setFirmName(userTicket.getFirmName());
		fundConsignorDo.setContractNo(contractNo);
		return fundConsignorDo;
	}

	/**
	 * 构建合同主体详情
	 */
	private FundContractDo buildContractEntity(FundContractRequestDto fundContractRequest) {
		FundContractDo fundContractDo = new FundContractDo();
		//构建合同委托人核心数据
		fundContractDo.setConsignorAccountId(fundContractRequest.getConsignorAccountId());
		fundContractDo.setStartTime(fundContractRequest.getStartTime());
		fundContractDo.setEndTime(fundContractRequest.getEndTime());
		fundContractDo.setSignatureImagePath(fundContractRequest.getSignatureImagePath());
		fundContractDo.setNotes(fundContractRequest.getNotes());
		//构建商户信息
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		fundContractDo.setCreatorId(userTicket.getId());
		fundContractDo.setCreator(userTicket.getUserName());
		fundContractDo.setFirmId(userTicket.getFirmId());
		fundContractDo.setFirmName(userTicket.getFirmName());
		fundContractDo.setState(ContractState.ENTUST.getCode());
		//获取业务编号
		String contractNo = uidRpcResovler.bizNumber(BizNoType.CONTRACT_NO.getCode());
		fundContractDo.setContractNo(contractNo);
		fundContractRequest.setContractNo(contractNo);
		//获取客户信息
		Customer customer = customerRpcResolver.findCustomerById(fundContractRequest.getCustomerId());
		fundContractDo.setConsignorCustomerCode(customer.getCode());
		return fundContractDo;
	}

	/**
	 * 合同页面信息列表
	 */
	private List<FundContractResponseDto> huildResponseContracts(List<FundContractDo> fundContracts) {
		List<FundContractResponseDto> contractResponseDtos = new ArrayList<FundContractResponseDto>();
		for (FundContractDo fundContractDo : fundContracts) {
			contractResponseDtos.add(this.buildPageContracts(fundContractDo));
		}
		return contractResponseDtos;
	}

	/**
	 * 合同页面信息
	 */
	private FundContractResponseDto buildPageContracts(FundContractDo fundContractDo) {
		FundContractResponseDto contractResponseDto = new FundContractResponseDto();
		contractResponseDto.setContractNo(fundContractDo.getContractNo());
		contractResponseDto.setCreator(fundContractDo.getCreator());
		contractResponseDto.setCreateTime(fundContractDo.getCreateTime());
		contractResponseDto.setStartTime(fundContractDo.getStartTime());
		contractResponseDto.setEndTime(fundContractDo.getEndTime());
		return contractResponseDto;
	}

	/**
	 * 构建查询条件
	 */
	private void buildQueryContractConditon(FundContractRequestDto fundContractRequest) {
		fundContractRequest.setExpirationTime(DateUtils.format(DateUtils.addDays(new Date(), fundContractRequest.getDays())));
	}
	
}

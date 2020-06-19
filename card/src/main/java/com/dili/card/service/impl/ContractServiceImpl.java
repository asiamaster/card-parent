package com.dili.card.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.card.dao.IFundConsignorDao;
import com.dili.card.dao.IFundContractDao;
import com.dili.card.dto.FundConsignorRequestDto;
import com.dili.card.dto.FundContractRequestDto;
import com.dili.card.dto.FundContractResponseDto;
import com.dili.card.entity.FundConsignorDo;
import com.dili.card.entity.FundContractDo;
import com.dili.card.service.IContractService;
import com.dili.card.type.ContractState;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.DateUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

@Service
public class ContractServiceImpl implements IContractService {
	
	@Autowired
	private IFundConsignorDao fundConsignorDao;
	@Autowired
	private IFundContractDao contractDao;

	@Override
	public void save(FundContractRequestDto fundContractRequest) {
		//构建合同主体
		FundContractDo fundContract = this.buildContractEntity(fundContractRequest);
		//构建被委托人主体
		List<FundConsignorDo> consignors = this.buildConsignorEntities(fundContractRequest.getConsignors());
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
		FundConsignorDo fundConsignorDo = new FundConsignorDo();
		fundConsignorDo.setContractNo(fundContract.getContractNo());
		List<FundConsignorDo> consignors = fundConsignorDao.selectList(fundConsignorDo);
		FundContractResponseDto contractResponse = this.buildContractDetail(fundContract, consignors);
		return contractResponse;
	}

	private FundContractResponseDto buildContractDetail(FundContractDo fundContract,
			List<FundConsignorDo> consignors) {
		return null;
	}

	/**
	 * 构建被委托人信息列表
	 */
	private List<FundConsignorDo> buildConsignorEntities(List<FundConsignorRequestDto> consignors) {
		List<FundConsignorDo> consignorDos = new ArrayList<FundConsignorDo>();
		for (FundConsignorRequestDto consignorRequestDto : consignors) {
			consignorDos.add(this.buildConsignorEntity(consignorRequestDto));
		}
		return consignorDos;
	}

	/**
	 * 构建被委托人信息
	 */
	private FundConsignorDo buildConsignorEntity(FundConsignorRequestDto consignorRequestDto) {
		FundConsignorDo fundConsignorDo = new FundConsignorDo();
		fundConsignorDo.setConsigneeIdCode(consignorRequestDto.getConsigneeIdCode());
		fundConsignorDo.setConsigneeIdMobile(consignorRequestDto.getConsigneeIdMobile());
		fundConsignorDo.setConsigneeName(consignorRequestDto.getConsigneeName());
		fundConsignorDo.setSignatureImagePath(consignorRequestDto.getSignatureImagePath());
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		fundConsignorDo.setFirmId(userTicket.getFirmId());
		fundConsignorDo.setFirmName(userTicket.getFirmName());
		return fundConsignorDo;
	}

	/**
	 * 构建合同主体详情
	 */
	private FundContractDo buildContractEntity(FundContractRequestDto fundContractRequest) {
		FundContractDo fundContractDo = new FundContractDo();
		fundContractDo.setConsignorAccountId(fundContractRequest.getConsignorAccountId());
		fundContractDo.setStartTime(fundContractRequest.getStartTime());
		fundContractDo.setEndTime(fundContractRequest.getEndTime());
		fundContractDo.setSignatureImagePath(fundContractRequest.getSignatureImagePath());
		fundContractDo.setNotes(fundContractRequest.getNotes());
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		fundContractDo.setCreatorId(userTicket.getId());
		fundContractDo.setCreator(userTicket.getUserName());
		fundContractDo.setFirmId(userTicket.getFirmId());
		fundContractDo.setFirmName(userTicket.getFirmName());
		fundContractDo.setState(ContractState.ENTUST.getCode());
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
		contractResponseDto.setStartTime(fundContractDo.getStartTime());
		contractResponseDto.setEndTime(fundContractDo.getEndTime());
		return null;
	}

	/**
	 * 构建查询条件
	 */
	private void buildQueryContractConditon(FundContractRequestDto fundContractRequest) {
		fundContractRequest.setExpirationTime(DateUtils.format(DateUtils.addDays(new Date(), fundContractRequest.getDays())));
	}
	
}

package com.dili.card.service.impl;

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

@Service
public class ContractServiceImpl implements IContractService {
	
	@Autowired
	private IFundConsignorDao iFundConsignorDao;
	@Autowired
	private IFundContractDao icContractDao;

	@Override
	public void save(FundContractRequestDto fundContractRequest) {
		FundContractDo fundContract = this.buildContract(fundContractRequest);
		List<FundConsignorDo> consignors = this.buildConsignor(fundContractRequest.getConsignors());
		icContractDao.save(fundContract);
		iFundConsignorDao.saveBatch(consignors);
	}

	@Override
	public List<FundContractResponseDto> list(FundContractRequestDto fundContractRequest) {
		FundContractDo condition = this.buildQueryContractConditon(fundContractRequest);
		List<FundContractDo> fundContracts = icContractDao.selectList(condition);
		List<FundContractResponseDto> fundResponseContracts = this.huildResponseContracts(fundContracts);
		return fundResponseContracts;
	}

	@Override
	public void remove(FundContractRequestDto fundContractRequest) {
		FundContractDo fundContract = icContractDao.getById(fundContractRequest.getId());
		if (fundContract == null) {
			throw new BusinessException(ResultCode.DATA_ERROR, "该合同号不存在");
		}
		if (ContractState.REMOVED.getCode() == fundContract.getState()) {
			return;
		}
		FundContractDo updateFundContract = new FundContractDo();
		updateFundContract.setId(fundContractRequest.getId());
		updateFundContract.setState(ContractState.REMOVED.getCode());
		icContractDao.update(updateFundContract);
	}

	@Override
	public FundContractResponseDto detail(FundContractRequestDto fundContractRequest) {
		FundContractDo fundContract = icContractDao.getById(fundContractRequest.getId());
		if (fundContract == null) {
			throw new BusinessException(ResultCode.DATA_ERROR, "该合同号不存在");
		}
		FundConsignorDo fundConsignorDo = new FundConsignorDo();
		fundConsignorDo.setContractNo(fundContract.getContractNo());
		List<FundConsignorDo> consignors = iFundConsignorDao.selectList(fundConsignorDo);
		FundContractResponseDto contractResponse = this.buildContractDetail(fundContract, consignors);
		return contractResponse;
	}

	private FundContractResponseDto buildContractDetail(FundContractDo fundContract,
			List<FundConsignorDo> consignors) {
		return null;
	}

	private List<FundConsignorDo> buildConsignor(List<FundConsignorRequestDto> consignors) {
		return null;
	}

	private FundContractDo buildContract(FundContractRequestDto fundContractRequest) {
		return null;
	}

	private List<FundContractResponseDto> huildResponseContracts(List<FundContractDo> fundContracts) {
		return null;
	}

	private FundContractDo buildQueryContractConditon(FundContractRequestDto fundContractRequest) {
		return null;
	}
	
}

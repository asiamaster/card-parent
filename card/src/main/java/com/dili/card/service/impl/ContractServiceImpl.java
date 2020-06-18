package com.dili.card.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.card.dao.IFundConsignorDao;
import com.dili.card.dao.IFundContractDao;
import com.dili.card.dto.FundContractRequestDto;
import com.dili.card.service.IContractService;

@Service
public class ContractServiceImpl implements IContractService {
	
	@Autowired
	private IFundConsignorDao iFundConsignorDao;
	@Autowired
	private IFundContractDao icContractDao;

	@Override
	public void save(FundContractRequestDto fundContractRequest) {
		
	}

	@Override
	public void list(FundContractRequestDto fundContractRequest) {
		
	}

	@Override
	public void remove(FundContractRequestDto fundContractRequest) {
		
	}

	@Override
	public void detail(FundContractRequestDto fundContractRequest) {

	}
	
	
}

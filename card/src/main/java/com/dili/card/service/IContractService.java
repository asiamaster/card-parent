package com.dili.card.service;

import com.dili.card.dto.FundContractRequestDto;

public interface IContractService {

	void save(FundContractRequestDto fundContractRequest);

	void list(FundContractRequestDto fundContractRequest);

	void remove(FundContractRequestDto fundContractRequest);

	void detail(FundContractRequestDto fundContractRequest);

}

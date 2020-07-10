package com.dili.card.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.card.service.IContractService;

/**
* 执行定时任务
*/
@Service
public class ContractScheduleHandler {

	@Autowired
	private IContractService contractService;

	/**
	*任务执行方法
	*/
	public void execute() {
		contractService.closeOverdueContract();
		contractService.activeOverdueContract();
	}

}

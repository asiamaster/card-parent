package com.dili.card.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.card.dao.IAccountCycleDao;
import com.dili.card.dao.IAccountCycleDetailDao;
import com.dili.card.dto.AccountCycleDto;
import com.dili.card.service.IAccountCycleService;

@Service("accountCycleService")
public class AccountCycleServiceImpl implements IAccountCycleService {
	
	@Autowired
	private IAccountCycleDao accountCycleDao;
	
	@Autowired
	private IAccountCycleDetailDao accountCycleDetailDao;

	@Override
	public void checkAccount(Long id) {

	}

	@Override
	public List<AccountCycleDto> list(AccountCycleDto accountCycleDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccountCycleDto detail(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}

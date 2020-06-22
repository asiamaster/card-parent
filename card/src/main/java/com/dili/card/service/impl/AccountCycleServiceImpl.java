package com.dili.card.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.card.dao.IAccountCycleDao;
import com.dili.card.dao.IAccountCycleDetailDao;
import com.dili.card.dto.AccountCycleDto;
import com.dili.card.entity.AccountCycleDo;
import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.type.BizNoType;

@Service("accountCycleService")
public class AccountCycleServiceImpl implements IAccountCycleService {
	
	@Autowired
	private IAccountCycleDao accountCycleDao;
	@Autowired
	private IAccountCycleDetailDao accountCycleDetailDao;
	@Autowired
	private UidRpcResovler uidRpcResovler;
	

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

	@Override
	public void createCycle(Long userId, String userName) {
		AccountCycleDo accountCycle = accountCycleDao.findActiveCycleByUserId(userId);
		if (accountCycle == null) {
			accountCycle = new AccountCycleDo();
			accountCycle.setUserId(userId);
			accountCycle.setUserName(userName);
			accountCycle.setCycleNo(Long.valueOf(uidRpcResovler.bizNumber(BizNoType.CYCLET_NO.getCode())));
		}
	}

	@Override
	public AccountCycleDo findByUserId(Long userId) {
		return null;
	}

}

package com.dili.card.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.card.dao.IAccountCycleDao;
import com.dili.card.dao.IAccountCycleDetailDao;
import com.dili.card.dto.AccountCycleDto;
import com.dili.card.entity.AccountCycleDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.type.BizNoType;
import com.dili.card.type.CycleState;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

@Service("accountCycleService")
public class AccountCycleServiceImpl implements IAccountCycleService {
	
	@Autowired
	private IAccountCycleDao accountCycleDao;
	@Autowired
	private IAccountCycleDetailDao accountCycleDetailDao;
	@Autowired
	private UidRpcResovler uidRpcResovler;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void settle(Long id) {
		AccountCycleDo accountCycle = this.findById(id);
		if (accountCycle.getState() == CycleState.SETTLED.getCode()) {
			throw new CardAppBizException("当前账务周期已结账,不能重复操作");
		}
		if (accountCycle.getState() == CycleState.FLATED.getCode()) {
			throw new CardAppBizException("当前账务周期已平账,不能进行结账操作");
		}
		int update = accountCycleDao.updateStateById(id, CycleState.SETTLED.getCode(), accountCycle.getVersion());
		if (update == 0) {
			throw new CardAppBizException("操作频繁,对账失败");
		}
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void flated(Long id) {
		AccountCycleDo accountCycle = this.findById(id);
		if (accountCycle.getState() == CycleState.ACTIVE.getCode()) {
			throw new CardAppBizException("当前账务周期未结账,不能进行此操作");
		}
		if (accountCycle.getState() == CycleState.FLATED.getCode()) {
			throw new CardAppBizException("当前账务周期已平账,不能重复操作");
		}
		int update = accountCycleDao.updateStateById(id, CycleState.FLATED.getCode(), accountCycle.getVersion());
		if (update == 0) {
			throw new CardAppBizException("操作频繁,平账失败");
		}
	}

	@Override
	public List<AccountCycleDto> list(AccountCycleDto accountCycleDto) {
		return null;
	}

	@Override
	public AccountCycleDto detail(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AccountCycleDo createCycleRecord(Long userId, String userName) {
		AccountCycleDo accountCycle = accountCycleDao.findActiveCycleByUserId(userId);
		if (accountCycle == null) {
			accountCycle = new AccountCycleDo();
			accountCycle.setUserId(userId);
			accountCycle.setUserName(userName);
			accountCycle.setCycleNo(Long.valueOf(uidRpcResovler.bizNumber(BizNoType.CYCLET_NO.getCode())));
			accountCycle.setCashBox(0L);
			accountCycle.setState(CycleState.ACTIVE.getCode());
			//构建商户信息
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			accountCycle.setFirmId(userTicket.getFirmId());
			accountCycle.setFirmName(userTicket.getFirmName());
			accountCycle.setVersion(1);
			accountCycleDao.save(accountCycle);
		}
		if (CycleState.SETTLED.getCode() == accountCycle.getState()) {
			throw new CardAppBizException("当前账务周期正在对账中,不能操作");
		}
		return accountCycle;
	}

	@Override
	public AccountCycleDo findByUserId(Long userId) {
		return accountCycleDao.findActiveCycleByUserId(userId);
	}

	@Override
	public AccountCycleDo findActiveCycleByUserId(Long userId, String userName) {
		return this.createCycleRecord(userId, userName);
	}

	@Override
	public AccountCycleDo findById(Long id) {
		AccountCycleDo accountCycle = accountCycleDao.getById(id);
		if (accountCycle == null) {
			throw new CardAppBizException("账务周期不存在");
		}
		return accountCycle;
	}

}

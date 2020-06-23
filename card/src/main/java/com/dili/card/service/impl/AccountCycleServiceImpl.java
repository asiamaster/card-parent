package com.dili.card.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.card.dao.IAccountCycleDao;
import com.dili.card.dao.IAccountCycleDetailDao;
import com.dili.card.dto.AccountCycleDto;
import com.dili.card.dto.UserCashDto;
import com.dili.card.entity.AccountCycleDetailDo;
import com.dili.card.entity.AccountCycleDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.IUserCashService;
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
	@Autowired
	private IUserCashService userCashService;

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
		this.updateStateById(id, CycleState.SETTLED.getCode(), accountCycle.getVersion());
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void flated(Long id) {
		AccountCycleDo accountCycle = this.findById(id);
		//平账状态校验
		this.validateCycleFlatedState(accountCycle);
		//更新状态
		this.updateStateById(id, CycleState.FLATED.getCode(), accountCycle.getVersion());
		//构建账务周期信息
		AccountCycleDetailDo accountCycleDetail = new AccountCycleDetailDo();
		accountCycleDetail.setCycleNo(accountCycle.getCycleNo());
		//构建领取款相关信息
		UserCashDto userCashDto = this.buildUserCashCondition(accountCycle);
		//账务周期领款信息
		this.buildCyclePayee(accountCycleDetail, userCashDto);
		//账务周期交款信息
		this.buildCyclePayer(accountCycleDetail, userCashDto);
		//构建商户相关信息
		this.buildFirmInfo(accountCycleDetail);
		
		accountCycleDetailDao.save(accountCycleDetail);
	}

	@Override
	public List<AccountCycleDto> list(AccountCycleDto accountCycleDto) {
		return null;
	}

	@Override
	public AccountCycleDto detail(Long id) {
		return null;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AccountCycleDo createCycleRecord(Long userId, String userName) {
		AccountCycleDo accountCycle = this.findSettledCycleByUserId(userId);
		if (accountCycle != null) {
			throw new CardAppBizException("当前账务周期正在对账中,不能操作");
		}
		accountCycle = this.findActiveCycleByUserId(userId);
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
		return accountCycle;
	}

	@Override
	public AccountCycleDo findByUserId(Long userId) {
		return null;
	}

	@Override
	public AccountCycleDo findActiveCycleByUserId(Long userId) {
		return accountCycleDao.findByUserIdAndState(userId, CycleState.ACTIVE.getCode());
	}
	
	@Override
	public AccountCycleDo findSettledCycleByUserId(Long userId) {
		return accountCycleDao.findByUserIdAndState(userId, CycleState.SETTLED.getCode());
	}

	@Override
	public AccountCycleDo findById(Long id) {
		AccountCycleDo accountCycle = accountCycleDao.getById(id);
		if (accountCycle == null) {
			throw new CardAppBizException("账务周期不存在");
		}
		return accountCycle;
	}
	
	
	/**
	 * 构建商户相关信息
	 */
	private void buildFirmInfo(AccountCycleDetailDo accountCycleDetail) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		accountCycleDetail.setFirmId(userTicket.getFirmId());
		accountCycleDetail.setFirmName(userTicket.getFirmName());
	}

	/**
	 * 更新账务周期状态
	 */
	private void updateStateById(Long id, Integer state, Integer version) {
		int update = accountCycleDao.updateStateById(id, state, version);
		if (update == 0) {
			throw new CardAppBizException("操作频繁,平账失败");
		}
	}
	
	/**
	 * 平账前状态校验
	 */
	private void validateCycleFlatedState(AccountCycleDo accountCycle) {
		if (accountCycle.getState() == CycleState.ACTIVE.getCode()) {
			throw new CardAppBizException("当前账务周期未结账,不能进行此操作");
		}
		if (accountCycle.getState() == CycleState.FLATED.getCode()) {
			throw new CardAppBizException("当前账务周期已平账,不能重复操作");
		}
	}

	/**
	 * 账务周期交款信息
	 */
	private void buildCyclePayer(AccountCycleDetailDo accountCycleDetail, UserCashDto userCashDto) {
		List<UserCashDto> userCashs = userCashService.listPayer(userCashDto);
		accountCycleDetail.setDeliverTimes(userCashs.size());
		Long amount = 0L;
		for (UserCashDto userCash : userCashs) {
			amount += userCash.getAmount();
		}
		accountCycleDetail.setDeliverAmount(amount);
	}

	/**
	 * 账务周期领款信息
	 */
	private void buildCyclePayee(AccountCycleDetailDo accountCycleDetail, UserCashDto userCashDto) {
		List<UserCashDto> userCashs = userCashService.listPayee(userCashDto);
		accountCycleDetail.setReceiveTimes(userCashs.size());
		Long amount = 0L;
		for (UserCashDto userCash : userCashs) {
			amount += userCash.getAmount();
		}
		accountCycleDetail.setReceiveAmount(amount);
	}

	/**
	 * 构建领取款查询条件
	 */
	private UserCashDto buildUserCashCondition(AccountCycleDo accountCycle) {
		UserCashDto userCashDto = new UserCashDto();
		userCashDto.setUserId(accountCycle.getUserId());
		userCashDto.setCreateStartTime(accountCycle.getStartTime());
		userCashDto.setCreateEndTime(LocalDateTime.now());
		return userCashDto;
	}

}

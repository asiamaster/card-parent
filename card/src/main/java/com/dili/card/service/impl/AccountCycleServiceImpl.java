package com.dili.card.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.card.dao.IAccountCycleDao;
import com.dili.card.dao.IAccountCycleDetailDao;
import com.dili.card.dto.AccountCycleDetailDto;
import com.dili.card.dto.AccountCycleDto;
import com.dili.card.dto.CycleStatistcDto;
import com.dili.card.entity.AccountCycleDetailDo;
import com.dili.card.entity.AccountCycleDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.IUserCashService;
import com.dili.card.type.BizNoType;
import com.dili.card.type.CycleState;
import com.dili.card.type.CycleStatisticType;
import com.dili.card.util.PageUtils;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.PageOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.hutool.core.util.NumberUtil;

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
	public void settle(Long userId) {
		//获取最新的账务周期
		AccountCycleDo accountCycle = accountCycleDao.findLatestActiveCycleByUserId(userId);
		// 对账状态校验
		this.validateCycleSettledState(accountCycle);
		// 更新账务周期状态
		this.updateStateById(accountCycle.getId(), CycleState.SETTLED.getCode(), accountCycle.getVersion());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void flated(Long id) {
		AccountCycleDo cycle = this.findById(id);
		// 平账状态校验
		this.validateCycleFlatedState(cycle);
		// 账务周期详情统计信息
		AccountCycleDetailDo accountCycleDetail = this.buildAccountCycleDetailDo(this.buildCycleDetail(cycle));
		// 构建商户相关信息
		this.buildFirmInfo(accountCycleDetail);
		// 更新账务周期状态
		this.updateStateById(id, CycleState.FLATED.getCode(), cycle.getVersion());
		// 保存账务周期详情信息
		accountCycleDetailDao.save(accountCycleDetail);
		// 更新领取款记录
		userCashService.flatedByCycle(cycle.getCycleNo());
	}

	@Override
	public List<AccountCycleDto> list(AccountCycleDto accountCycleDto) {
		// 构建查询条件
		this.buildQueryCondition(accountCycleDto);
		// 封装返回数据
		return this.buildAccountCycleList(accountCycleDao.findByCondition(accountCycleDto));
	}

	@Override
	public PageOutput<List<AccountCycleDto>> page(AccountCycleDto accountCycleDto) {
		Page<?> page = PageHelper.startPage(accountCycleDto.getPage(), accountCycleDto.getRows());
		List<AccountCycleDto> accountCycles = this.list(accountCycleDto);
		return PageUtils.convert2PageOutput(page, accountCycles);
	}

	@Override
	public AccountCycleDto detail(Long id) {
		return this.buildAccountCycleWrapper(accountCycleDao.getById(id));
	}
	
	@Override
	public AccountCycleDto applyDetail(Long userId) {
		return this.buildAccountCycleWrapper(accountCycleDao.findLatestActiveCycleByUserId(userId));
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void increaseCashBox(Long cycleNo, Long amount) {
		accountCycleDao.updateCashBox(cycleNo, amount);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void decreaseeCashBox(Long cycleNo, Long amount) {
		accountCycleDao.updateCashBox(cycleNo, -amount);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AccountCycleDo findActiveCycleByUserId(Long userId, String userName, String userCode) {
		if (userId == null || StringUtils.isBlank(userName) || StringUtils.isBlank(userCode)) {
			throw new CardAppBizException("查询账务周期参数错误");
		}
		AccountCycleDo accountCycle = this.findSettledCycleByUserId(userId);
		if (accountCycle != null) {
			throw new CardAppBizException("当前账务周期正在对账中,不能操作");
		}
		accountCycle = this.findActiveCycleByUserId(userId);
		if (accountCycle == null) {
			accountCycle = new AccountCycleDo();
			accountCycle.setUserId(userId);
			accountCycle.setUserCode(userCode);
			accountCycle.setUserName(userName);
			accountCycle.setCycleNo(Long.valueOf(uidRpcResovler.bizNumber(BizNoType.CYCLET_NO.getCode())));
			accountCycle.setCashBox(0L);
			accountCycle.setCashAmount(0L);
			accountCycle.setState(CycleState.ACTIVE.getCode());
			// 构建商户信息
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			accountCycle.setAuditorId(userTicket.getId());
			accountCycle.setAuditorName(userTicket.getRealName());
			accountCycle.setFirmId(userTicket.getFirmId());
			accountCycle.setFirmName(userTicket.getFirmName());
			accountCycle.setVersion(1);
			accountCycleDao.save(accountCycle);
		}
		return accountCycle;
	}

	/**
	 * 获取活跃的账务周期
	 */
	@Override
	public AccountCycleDo findActiveCycleByUserId(Long userId) {
		return accountCycleDao.findByUserIdAndState(userId, CycleState.ACTIVE.getCode());
	}

	/**
	 * 获取对账的账务周期
	 */
	private AccountCycleDo findSettledCycleByUserId(Long userId) {
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
	 * 构造页面响应实体列表
	 */
	private List<AccountCycleDto> buildAccountCycleList(List<AccountCycleDo> accountCycles) {
		List<AccountCycleDto> accountCycleDtos = new ArrayList<AccountCycleDto>();
		for (AccountCycleDo accountCycle : accountCycles) {
			accountCycleDtos.add(this.buildAccountCycleWrapper(accountCycle));
		}
		return accountCycleDtos;
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
	 * 对账前状态校验
	 */
	private void validateCycleSettledState(AccountCycleDo accountCycle) {
		if (accountCycle.getState() == CycleState.SETTLED.getCode()) {
			throw new CardAppBizException("当前账务周期已结账,不能重复操作");
		}
		if (accountCycle.getState() == CycleState.FLATED.getCode()) {
			throw new CardAppBizException("当前账务周期已平账,不能进行结账操作");
		}
	}

	/**
	 * 平账前状态校验
	 */
	private void validateCycleFlatedState(AccountCycleDo accountCycle) {
		if (accountCycle.getState() == CycleState.ACTIVE.getCode()) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "当前账务周期未结账,不能进行此操作");
		}
		if (accountCycle.getState() == CycleState.FLATED.getCode()) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "当前账务周期已平账,不能重复操作");
		}
	}

	/**
	 * 账务周期详情统计信息
	 */
	private AccountCycleDetailDto buildCycleDetail(AccountCycleDo cycle) {
		AccountCycleDetailDto accountCycleDetail = new AccountCycleDetailDto();
		accountCycleDetail.setCycleNo(cycle.getCycleNo());
		List<CycleStatistcDto> cycleStatistcs = accountCycleDetailDao.statisticCycleRecord(cycle.getCycleNo(),
				cycle.getUserId());
		for (CycleStatistcDto cycleStatistc : cycleStatistcs) {
			CycleStatisticType cycleStatisticType = CycleStatisticType.getCycleStatisticType(cycleStatistc.getType(),
					cycleStatistc.getTradeChannel());
			Field times;
			Field amount;
			try {
				times = accountCycleDetail.getClass().getDeclaredField(cycleStatisticType.getTimes());
				times.setAccessible(true);
				times.set(accountCycleDetail, cycleStatistc.getTimes());
				amount = accountCycleDetail.getClass().getDeclaredField(cycleStatisticType.getAmount());
				amount.setAccessible(true);
				amount.set(accountCycleDetail, cycleStatistc.getAmount() == null ? 0 : cycleStatistc.getAmount());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 计算现金收支余额 工本费+ 现金充值 -现金取款
		accountCycleDetail.setRevenueAmount(accountCycleDetail.getCostAmount() + accountCycleDetail.getDepoCashAmount()
				- accountCycleDetail.getDrawCashAmount());
		return accountCycleDetail;
	}

	/**
	 * 查询条件
	 */
	private void buildQueryCondition(AccountCycleDto accountCycleDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		accountCycleDto.setFirmId(1L);
		accountCycleDto.setUserId(
				NumberUtil.isInteger(accountCycleDto.getUserName()) ? Long.valueOf(accountCycleDto.getUserName())
						: null);
		accountCycleDto.setAuditorId(
				NumberUtil.isInteger(accountCycleDto.getAuditorName()) ? Long.valueOf(accountCycleDto.getAuditorName())
						: null);
	}

	/**
	 * 构建账务周期包装实体实体
	 */
	private AccountCycleDto buildAccountCycleWrapper(AccountCycleDo cycle) {
		// 构建账务周期实体
		AccountCycleDto accountCycleDto = this.buildAccountCycleDto(cycle);
		// 构建账务周期详情
		AccountCycleDetailDto accountCycleDetail = this.buildCycleDetail(cycle);
		// 计算网银存取款
		accountCycleDetail
				.setInOutBankAmount(accountCycleDetail.getBankInAmount() - accountCycleDetail.getBankOutAmount());
		// 计算未交现金金额  领款金额 + 现金收支收益金额 - 现金交款金额
		Long unDeliverAmount = accountCycleDetail.getReceiveAmount() + accountCycleDetail.getRevenueAmount()
				- accountCycleDetail.getDeliverAmount();
		if (CycleState.ACTIVE.getCode() == cycle.getState()) {//活跃期
			accountCycleDetail.setUnDeliverAmount(unDeliverAmount);
		}else {//非活跃期 未交现金金额就是最终交款金额
			accountCycleDetail.setLastDeliverAmount(unDeliverAmount);
		}
		accountCycleDto.setAccountCycleDetailDto(accountCycleDetail);
		return accountCycleDto;
	}

	/**
	 * 构建账务周期相应实体
	 */
	private AccountCycleDto buildAccountCycleDto(AccountCycleDo cycle) {
		AccountCycleDto accountCycleDto = new AccountCycleDto();
		accountCycleDto.setId(cycle.getId());
		accountCycleDto.setUserCode(cycle.getUserCode());
		accountCycleDto.setUserId(cycle.getUserId());
		accountCycleDto.setUserName(cycle.getUserName());
		accountCycleDto.setCycleNo(cycle.getCycleNo());
		accountCycleDto.setCashBox(cycle.getCashBox());
		accountCycleDto.setStartTime(cycle.getStartTime());
		accountCycleDto.setEndTime(cycle.getEndTime());
		accountCycleDto.setState(cycle.getState());
		return accountCycleDto;
	}

	/**
	 * 构建账务周期详情相应实体从dto复制数据到do
	 */
	private AccountCycleDetailDo buildAccountCycleDetailDo(AccountCycleDetailDto accountCycleDetail) {
		AccountCycleDetailDo accountCycleDetailDo = new AccountCycleDetailDo();
		// 数据复制
		BeanUtils.copyProperties(accountCycleDetail, accountCycleDetailDo);
		return accountCycleDetailDo;
	}

}

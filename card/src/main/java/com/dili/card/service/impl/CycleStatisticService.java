package com.dili.card.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dili.card.dao.IAccountCycleDetailDao;
import com.dili.card.dto.AccountCycleDetailDto;
import com.dili.card.dto.AccountCycleDto;
import com.dili.card.dto.CycleStatistcDto;
import com.dili.card.entity.AccountCycleDo;
import com.dili.card.entity.UserCashDo;
import com.dili.card.service.ICycleStatisticService;
import com.dili.card.service.IUserCashService;
import com.dili.card.type.CashAction;
import com.dili.card.type.CycleState;
import com.dili.card.type.CycleStatisticType;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

@Service
public class CycleStatisticService implements ICycleStatisticService {

	private static final Logger log = LoggerFactory.getLogger(AccountCycleServiceImpl.class);

	@Autowired
	private IAccountCycleDetailDao accountCycleDetailDao;
	@Autowired
	private IUserCashService userCashService;

	@Override
	public List<AccountCycleDto> statisticList(List<AccountCycleDo> cycles, boolean detail) {
		if(CollectionUtils.isEmpty(cycles)) {
			return new ArrayList<AccountCycleDto>();
		}
		
		// 构建账务周期实体
		List<AccountCycleDto> accountCycleDtos = buildAccountCycleDtoList(cycles);
		
		List<Long> cycleNos = accountCycleDtos.stream().map(o -> o.getCycleNo()).collect(Collectors.toList());

		Map<Long, List<CycleStatistcDto>> cycleStatistcs = findCycleStatistcDtoMapByCycleNos(
				accountCycleDetailDao.statisticCycleBussinessRecord(cycleNos));
		Map<Long, List<CycleStatistcDto>> cycleReversedStatistcs = findCycleStatistcDtoMapByCycleNos(
				accountCycleDetailDao.statisticReverseByCycleNo(cycleNos));

		for (AccountCycleDto accountCycleDto : accountCycleDtos) {

			// 账务周期详情构建
			AccountCycleDetailDto accountCycleBussinessDetail = buildCycleDetail(accountCycleDto.getCycleNo(),
					cycleStatistcs.get(accountCycleDto.getCycleNo()),
					cycleReversedStatistcs.get(accountCycleDto.getCycleNo()));

			// 最后账务周期数据的计算用于展示
			calculateLastCycleDetail(accountCycleBussinessDetail, accountCycleDto, detail);

			accountCycleDto.setAccountCycleDetailDto(accountCycleBussinessDetail);
		}
		return accountCycleDtos;
	}

	@Override
	public AccountCycleDetailDto statisticCycleDetail(Long cycleNo) {
		return this.buildCycleDetail(cycleNo,
				accountCycleDetailDao.statisticCycleBussinessRecord(Arrays.asList(cycleNo)),
				accountCycleDetailDao.statisticReverseByCycleNo(Arrays.asList(cycleNo)));
	}

	/**
	 * 账务周期详情构建
	 * 
	 * @return
	 */
	private AccountCycleDetailDto buildCycleDetail(Long cycleNo, List<CycleStatistcDto> cycleStatistcs,
			List<CycleStatistcDto> cycleReversedStatistcs) {
		// 构建业务产生的金额 账务周期详情 不包括冲正数据
		AccountCycleDetailDto accountCycleBussinessDetail = this.transferStatisticToCycleDetail(cycleNo,
				cycleStatistcs);

		// 统计详情只包括冲正数据
		AccountCycleDetailDto accountCycleReverseDetail = this.transferStatisticToCycleDetail(cycleNo,
				cycleReversedStatistcs);

		// 合并账务周期详情统计信息
		mergeAccountCycleDetail(accountCycleBussinessDetail, accountCycleReverseDetail);

		return calculateBussinessCashBalanceAmount(accountCycleBussinessDetail);
	}

	/**
	 * 分组
	 */
	public Map<Long, List<CycleStatistcDto>> findCycleStatistcDtoMapByCycleNos(List<CycleStatistcDto> cycleStatistcs) {
		return cycleStatistcs.stream().collect(Collectors.groupingBy(CycleStatistcDto::getCycleNo));
	}

	/**
	 * 最后账务周期数据的计算用于展示
	 */
	private void calculateLastCycleDetail(AccountCycleDetailDto accountCycleBussinessDetail,
			AccountCycleDto accountCycleDto, boolean detail) {
		// 计算网银存取款
		accountCycleBussinessDetail.setInOutBankAmount(
				accountCycleBussinessDetail.getBankInAmount() - accountCycleBussinessDetail.getBankOutAmount());

		// 计算未交现金金额 领款金额 + 现金收支收益金额 - 现金交款金额
		Long unDeliverAmount = accountCycleBussinessDetail.getReceiveAmount()
				+ accountCycleBussinessDetail.getRevenueAmount() - accountCycleBussinessDetail.getDeliverAmount();

		if (accountCycleDto.getCycleNo() != null || accountCycleDto.getCycleNo() != -1L) {
			if (CycleState.ACTIVE.getCode() == accountCycleDto.getState()) {// 活跃期
				accountCycleBussinessDetail.setUnDeliverAmount(unDeliverAmount);
			} else {// 非活跃期 最近一次交现金金额就是最终交款金额
				UserCashDo userCashDo = userCashService.getLastestUesrCash(accountCycleDto.getUserId(),
						accountCycleDto.getCycleNo(), CashAction.PAYER.getCode());
				accountCycleBussinessDetail.setLastDeliverAmount(userCashDo.getAmount());
				// 结账后此字段为0
				accountCycleBussinessDetail.setUnDeliverAmount(0L);
				// 详情 平账 和 结账申请 后不统计结账申请交款记录 对账管理列表 交款金额 不统计最后一次交款金额
				if ((detail && CycleState.ACTIVE.getCode() != accountCycleDto.getState()) || !detail) {
					accountCycleBussinessDetail
							.setDeliverAmount(accountCycleBussinessDetail.getDeliverAmount() - userCashDo.getAmount());
					accountCycleBussinessDetail.setDeliverTimes(accountCycleBussinessDetail.getDeliverTimes() - 1);
				}
			}
		}

		// 现金收款包括充值和工本费
		accountCycleBussinessDetail.setDepoCashAmount(accountCycleBussinessDetail.getDepoCashAmount()
				+ accountCycleBussinessDetail.getOpenCostAmount() + accountCycleBussinessDetail.getChangeCostAmount());
		accountCycleBussinessDetail.setDepoCashTimes(
				accountCycleBussinessDetail.getDepoCashTimes() + accountCycleBussinessDetail.getOpenCostFeetimes()
						+ accountCycleBussinessDetail.getChangeCostFeetimes());

	}

	/**
	 * 计算业务现金收支余额 开卡/换卡工本费+ 现金充值 -现金取款
	 */
	private AccountCycleDetailDto calculateBussinessCashBalanceAmount(AccountCycleDetailDto accountCycleDetail) {
		accountCycleDetail
				.setRevenueAmount(accountCycleDetail.getOpenCostAmount() + accountCycleDetail.getChangeCostAmount()
						+ accountCycleDetail.getDepoCashAmount() - accountCycleDetail.getDrawCashAmount());
		return accountCycleDetail;
	}

	/**
	 * 业务操作统计金额转化为具体账务详情
	 */
	private AccountCycleDetailDto transferStatisticToCycleDetail(Long cycleNo, List<CycleStatistcDto> cycleStatistcs) {
		AccountCycleDetailDto accountCycleDetail = new AccountCycleDetailDto();
		accountCycleDetail.setCycleNo(cycleNo);
		if (!CollectionUtils.isEmpty(cycleStatistcs)) {
			for (CycleStatistcDto cycleStatistc : cycleStatistcs) {
				CycleStatisticType cycleStatisticType = CycleStatisticType
						.getCycleStatisticType(cycleStatistc.getType(), cycleStatistc.getTradeChannel());
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
					log.error("账务周期详情统计出错", e);
				}
			}
		}
		return accountCycleDetail;
	}

	/**
	 * 正向操作数据和冲正数据进行合并
	 */
	protected void mergeAccountCycleDetail(AccountCycleDetailDto masterCycleDetail,
			AccountCycleDetailDto accountCycleReverseDetail) {
		masterCycleDetail.setBeforeReverseBankInAmount(masterCycleDetail.getBankInAmount());
		masterCycleDetail.setBeforeReverseBankOutAmount(masterCycleDetail.getBankOutAmount());
		masterCycleDetail.setBeforeReverseDepoCashAmount(masterCycleDetail.getDepoCashAmount());
		masterCycleDetail.setBeforeReverseDepoPosAmount(masterCycleDetail.getDepoPosAmount());
		masterCycleDetail.setBeforeReverseDrawCashAmount(masterCycleDetail.getDrawCashAmount());
		masterCycleDetail
				.setBankInAmount(masterCycleDetail.getBankInAmount() + accountCycleReverseDetail.getBankInAmount());
		masterCycleDetail
				.setBankOutAmount(masterCycleDetail.getBankOutAmount() + accountCycleReverseDetail.getBankOutAmount());
		masterCycleDetail.setDepoCashAmount(
				masterCycleDetail.getDepoCashAmount() + accountCycleReverseDetail.getDepoCashAmount());
		masterCycleDetail
				.setDepoPosAmount(masterCycleDetail.getDepoPosAmount() + accountCycleReverseDetail.getDepoPosAmount());
		masterCycleDetail.setDrawCashAmount(
				masterCycleDetail.getDrawCashAmount() + accountCycleReverseDetail.getDrawCashAmount());
	}

	/**
	 * 构建账务周期相应实体
	 */
	private AccountCycleDto buildAccountCycleDto(AccountCycleDo cycle) {
		AccountCycleDto accountCycleDto = new AccountCycleDto();
		if (cycle == null) {
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			accountCycleDto.setUserCode(userTicket.getSerialNumber());
			accountCycleDto.setUserId(userTicket.getId());
			accountCycleDto.setUserName(userTicket.getRealName());
			accountCycleDto.setCycleNo(-1L);
			return accountCycleDto;
		}
		accountCycleDto.setId(cycle.getId());
		accountCycleDto.setUserCode(cycle.getUserCode());
		accountCycleDto.setUserId(cycle.getUserId());
		accountCycleDto.setUserName(cycle.getUserName());
		accountCycleDto.setCycleNo(cycle.getCycleNo() == null ? -1L : cycle.getCycleNo());
		accountCycleDto.setCashBox(cycle.getCashBox());
		accountCycleDto.setStartTime(cycle.getStartTime());
		accountCycleDto.setEndTime(cycle.getEndTime());
		accountCycleDto.setState(cycle.getState());
		return accountCycleDto;
	}

	/**
	 * 构建账务周期相应实体
	 */
	private List<AccountCycleDto> buildAccountCycleDtoList(List<AccountCycleDo> cycles) {
		List<AccountCycleDto> accountCycleDtos = new ArrayList<AccountCycleDto>();
		for (AccountCycleDo cycle : cycles) {
			accountCycleDtos.add(buildAccountCycleDto(cycle));
		}
		return accountCycleDtos;
	}

}

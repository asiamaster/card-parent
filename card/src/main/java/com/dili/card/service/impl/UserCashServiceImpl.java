package com.dili.card.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.dili.card.common.constant.Constant;
import com.dili.card.dao.IUserCashDao;
import com.dili.card.dto.AccountCycleDto;
import com.dili.card.dto.UserCashDto;
import com.dili.card.entity.AccountCycleDo;
import com.dili.card.entity.UserCashDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.IUserCashService;
import com.dili.card.type.BizNoType;
import com.dili.card.type.CashAction;
import com.dili.card.type.CashState;
import com.dili.card.util.CurrencyUtils;
import com.dili.card.util.PageUtils;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.PageOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service("userCashService")
public class UserCashServiceImpl implements IUserCashService {

	@Autowired
	private IUserCashDao userCashDao;
	@Autowired
	private IAccountCycleService accountCycleService;
	@Autowired
	private UidRpcResovler uidRpcResovler;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(UserCashDto userCashDto) {
		UserCashDo userCashDo = this.buildUserCashEntity(userCashDto);
		userCashDao.save(userCashDo);
	}

	@Override
	public List<UserCashDto> list(UserCashDto userCashDto, CashAction cashAction) {
		this.buildUserCashCondition(userCashDto, cashAction);
		List<UserCashDo> userCashs = userCashDao.findEntityByCondition(userCashDto);
		return this.buildPageUserCash(userCashs);
	}

	@Override
	public void delete(Long id) {
		UserCashDo userCashDo = this.findById(id);
		if (CashState.UNSETTLED.getCode() != userCashDo.getState()) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "已对账不能删除");
		}
		if (!userCashDao.delete(id)) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "删除失败");
		}
		;
	}

	@Override
	public void modify(UserCashDto userCashDto) {
		UserCashDo userCashDo = this.findById(userCashDto.getId());
		if (CashState.UNSETTLED.getCode() != userCashDo.getState()) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "已对账不能修改");
		}
		//金额校验
		this.validateAmount(userCashDto.getAmount());
		//获取账务周期
		AccountCycleDo accountCycle = accountCycleService.findActiveCycleByUserId(userCashDto.getUserId(),
				userCashDto.getUserName(), userCashDto.getUserCode());
		AccountCycleDto accountCycleDto = accountCycleService.detail(accountCycle.getId());
		//校验现金余额  领款修改不能导致现金余额小于0  并且校验是本人
		if (userCashDto.getAction().equals(CashAction.PAYEE.getCode()) && userCashDo.getUserId().equals(userCashDto.getUserId())) {
			if (accountCycleDto.getAccountCycleDetailDto().getUnDeliverAmount() < userCashDo.getAmount() - userCashDto.getAmount()) {
				throw new CardAppBizException(ResultCode.DATA_ERROR, "修改领款金额后导致现金余额统计小于0");
			}
		}
		//校验现金余额
		if (userCashDto.getAction().equals(CashAction.PAYER.getCode())) {
			//修改原来人的交款验证
			if (userCashDo.getUserId().equals(userCashDto.getUserId())) {
				if (accountCycleDto.getAccountCycleDetailDto().getUnDeliverAmount() < userCashDto.getAmount() - userCashDo.getAmount()) {
					throw new CardAppBizException(ResultCode.DATA_ERROR, "修改交款金额后导致现金余额统计小于0");
				}
			}else {//修改换了人后的操作就是新增交款  原来的数据直接换掉
				if (accountCycleDto.getAccountCycleDetailDto().getUnDeliverAmount() < userCashDto.getAmount()) {
					throw new CardAppBizException(ResultCode.DATA_ERROR, "修改交款金额后导致现金余额统计小于0");
				}
			}
		}
		userCashDo = new UserCashDo();
		userCashDo.setId(userCashDto.getId());
		userCashDo.setUserId(userCashDto.getUserId());
		userCashDo.setUserCode(userCashDto.getUserCode());
		userCashDo.setUserName(userCashDto.getUserName());
		userCashDo.setAmount(userCashDto.getAmount());
		userCashDo.setNotes(userCashDto.getNotes());
		userCashDo.setCycleNo(accountCycle.getCycleNo());
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		userCashDo.setCreatorId(userTicket.getId());
		userCashDo.setCreatorCode(userTicket.getUserName());
		userCashDo.setCreator(userTicket.getRealName());
		userCashDao.update(userCashDo);
	}

	@Override
	public UserCashDo findById(Long id) {
		UserCashDo userCashDo = userCashDao.getById(id);
		if (userCashDo == null) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "该记录不存在");
		}
		return userCashDo;
	}

	@Override
	public UserCashDto detail(Long id) {
		return this.buildSingleCashDtoy(this.findById(id));
	}

	@Override
	public Long findTotalAmountByUserId(UserCashDto userCashDto, CashAction cashAction) {
		this.buildUserCashCondition(userCashDto, cashAction);
		return userCashDao.findTotalAmountByUserId(userCashDto);
	}

	@Override
	public Long findTotalAmountByUserId(UserCashDto userCashDto) {
		this.buildUserCashCondition(userCashDto, null);
		return userCashDao.findTotalAmountByUserId(userCashDto);
	}

	@Override
	public void flatedByCycle(Long cycleNo) {
		int update = userCashDao.updateStateByCycle(cycleNo, CashState.SETTLED.getCode());
		if (update == 0) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "更新操作失败");
		}
	}

	@Override
	public PageOutput<List<UserCashDto>> listPayee(UserCashDto userCashDto) {
		Page<?> page = PageHelper.startPage(userCashDto.getPage(), userCashDto.getRows());
		List<UserCashDto> userCashs = this.list(userCashDto, CashAction.PAYEE);
		return PageUtils.convert2PageOutput(page, userCashs);
	}

	@Override
	public PageOutput<List<UserCashDto>> listPayer(UserCashDto userCashDto) {
		Page<?> page = PageHelper.startPage(userCashDto.getPage(), userCashDto.getRows());
		List<UserCashDto> userCashs = this.list(userCashDto, CashAction.PAYER);
		return PageUtils.convert2PageOutput(page, userCashs);
	}

	/**
	 * 封装领取款记录
	 */
	private UserCashDo buildUserCashEntity(UserCashDto userCashDto) {
		UserCashDo userCash = new UserCashDo();
		AccountCycleDo accountCycle = accountCycleService.findActiveCycleByUserId(userCashDto.getUserId(),
				userCashDto.getUserName(), userCashDto.getUserCode());
		if (userCashDto.getAction().equals(CashAction.PAYER.getCode())) {//校验现金余额
			AccountCycleDto accountCycleDto = accountCycleService.detail(accountCycle.getId());
			if (accountCycleDto.getAccountCycleDetailDto().getUnDeliverAmount() < userCashDto.getAmount()) {
				throw new CardAppBizException(ResultCode.DATA_ERROR, "交款金额大于现金余额");
			}
		}
		this.validateAmount(userCashDto.getAmount());
		userCash.setCashNo(Long.valueOf(uidRpcResovler.bizNumber(BizNoType.CASH_NO.getCode())));
		userCash.setAction(userCashDto.getAction());
		userCash.setAmount(userCashDto.getAmount());
		userCash.setUserId(userCashDto.getUserId());
		userCash.setUserCode(userCashDto.getUserCode());
		userCash.setUserName(userCashDto.getUserName());
		userCash.setState(CashState.UNSETTLED.getCode());
		userCash.setNotes(userCashDto.getNotes());
		// 构建商户信息和创建者
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		userCash.setCreatorId(userTicket.getId());
		userCash.setCreatorCode(userTicket.getUserName());
		userCash.setCreator(userTicket.getRealName());
		userCash.setFirmId(userTicket.getFirmId());
		userCash.setFirmName(userTicket.getFirmName());
		userCash.setCycleNo(accountCycle.getCycleNo());
		return userCash;
	}

	/**
	 * 构建领取款查询条件
	 */
	private void buildUserCashCondition(UserCashDto userCashDto, CashAction cashAction) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		userCashDto.setFirmId(userTicket.getFirmId());
		if (cashAction != null) {
			userCashDto.setAction(cashAction.getCode());
		}
		if (userCashDto.getCreateStartTime() != null && userCashDto.getCreateEndTime() != null
				&& userCashDto.getCreateStartTime().isAfter(userCashDto.getCreateEndTime())) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "开始时间大于结束时间");
		}
		// 默认365天
		if (userCashDto.getCreateEndTime() != null && userCashDto.getCreateStartTime() == null) {
			userCashDto.setCreateStartTime(userCashDto.getCreateEndTime().minusDays(365L));
		}
	}

	/**
	 * 构建页面列表实体
	 */
	private List<UserCashDto> buildPageUserCash(List<UserCashDo> userCashs) {
		List<UserCashDto> cashDtos = new ArrayList<UserCashDto>();
		if (CollectionUtils.isEmpty(userCashs)) {
			return cashDtos;
		}
		for (UserCashDo userCashDo : userCashs) {
			cashDtos.add(this.buildSingleCashDtoy(userCashDo));
		}
		return cashDtos;
	}

	/**
	 * 构建单个领取款记录实体
	 */
	private UserCashDto buildSingleCashDtoy(UserCashDo userCashDo) {
		UserCashDto cashDto = new UserCashDto();
		cashDto.setCashNo(userCashDo.getCashNo());
		cashDto.setAmount(userCashDo.getAmount());
		cashDto.setCreatorId(userCashDo.getCreatorId());
		cashDto.setCreatorCode(userCashDo.getCreatorCode());
		cashDto.setCreator(userCashDo.getCreator());
		cashDto.setUserId(userCashDo.getUserId());
		cashDto.setUserCode(userCashDo.getUserCode());
		cashDto.setUserName(userCashDo.getUserName());
		cashDto.setCreateTime(userCashDo.getCreateTime());
		cashDto.setNotes(userCashDo.getNotes());
		cashDto.setState(userCashDo.getState());
		cashDto.setId(userCashDo.getId());
		cashDto.setAction(userCashDo.getAction());
		return cashDto;
	}
	

	/**
	 * 校验金额
	 */
	private void validateAmount(Long amount) {
		if (amount < Constant.MIN_AMOUNT) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "金额不能低于"+ CurrencyUtils.toCurrency(Constant.MIN_AMOUNT) + "元");
		}
		if (amount > Constant.MAX_AMOUNT) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "金额不能超过"+ CurrencyUtils.toCurrency(Constant.MAX_AMOUNT) + "元");
		}
		
	}
}

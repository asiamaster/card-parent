package com.dili.card.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.dili.card.dao.IUserCashDao;
import com.dili.card.dto.UserCashDto;
import com.dili.card.entity.AccountCycleDo;
import com.dili.card.entity.UserCashDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.IUserCashService;
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

import cn.hutool.core.util.NumberUtil;

@Service("userCashService")
public class UserCashServiceImpl implements IUserCashService {

	@Autowired
	private IUserCashDao userCashDao;
	@Autowired
	private IAccountCycleService accountCycleService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(UserCashDto userCashDto, CashAction cashAction) {
		UserCashDo userCashDo = this.buildUserCashEntity(userCashDto, cashAction);
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
		if (CashState.SETTLED.getCode() == userCashDo.getState()) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "已对账不能删除");
		}
		userCashDao.delete(id);
	}

	@Override
	public void modify(UserCashDto userCashDto) {
		UserCashDo userCashDo = this.findById(userCashDto.getId());
		if (CashState.SETTLED.getCode() == userCashDo.getState()) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "已对账不能修改");
		}
		userCashDo = new UserCashDo();
		userCashDo.setId(userCashDto.getId());
		userCashDo.setAmount(CurrencyUtils.yuan2Cent(new BigDecimal(userCashDto.getAmountYuan())));
		userCashDo.setNotes(userCashDto.getNotes());
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
	public void savePayee(UserCashDto userCashDto) {
		this.save(userCashDto, CashAction.PAYEE);
	}

	@Override
	public void savePayer(UserCashDto userCashDto) {
		this.save(userCashDto, CashAction.PAYER);
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
	private UserCashDo buildUserCashEntity(UserCashDto userCashDto, CashAction cashAction) {
		UserCashDo userCash = new UserCashDo();
		userCash.setAction(cashAction.getCode());
		userCash.setAmount(CurrencyUtils.yuan2Cent(new BigDecimal(userCashDto.getAmountYuan())));
		userCash.setUserId(userCashDto.getUserId());
		userCash.setUserName(userCashDto.getUserName());
		userCash.setState(CashState.UNSETTLED.getCode());
		userCash.setNotes(userCashDto.getNotes());
		// 构建商户信息和创建者
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		userCash.setCreatorId(userTicket.getId());
		userCash.setCreator(userTicket.getRealName());
		userCash.setFirmId(userTicket.getFirmId());
		userCash.setFirmName(userTicket.getFirmName());
		AccountCycleDo accountCycle = accountCycleService.findActiveCycleByUserId(userCashDto.getUserId(),
				userCashDto.getUserName());
		userCash.setCycleNo(accountCycle.getCycleNo());
		return userCash;
	}

	/**
	 * 构建领取款查询条件
	 */
	private void buildUserCashCondition(UserCashDto userCashDto, CashAction cashAction) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		userCashDto.setFirmId(userTicket.getFirmId());
		userCashDto.setState(cashAction.getCode());
		userCashDto.setUserId(
				NumberUtil.isInteger(userCashDto.getUserName()) ? Long.valueOf(userCashDto.getUserName()) : null);
		userCashDto.setCreatorId(
				NumberUtil.isInteger(userCashDto.getCreator()) ? Long.valueOf(userCashDto.getCreator()) : null);
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
		cashDto.setAmountYuan(CurrencyUtils.toYuanWithStripTrailingZeros(userCashDo.getAmount()));
		cashDto.setCreatorId(userCashDo.getCreatorId());
		cashDto.setCreator(userCashDo.getCreator());
		cashDto.setUserId(userCashDo.getUserId());
		cashDto.setUserCode(userCashDo.getUserCode());
		cashDto.setUserName(userCashDo.getUserName());
		cashDto.setCreateTime(userCashDo.getCreateTime());
		cashDto.setNotes(userCashDo.getNotes());
		cashDto.setState(userCashDo.getState());
		return cashDto;
	}

}

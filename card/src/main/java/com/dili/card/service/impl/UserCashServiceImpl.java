package com.dili.card.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import com.dili.ss.constant.ResultCode;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

import cn.hutool.core.util.NumberUtil;

@Service("userCashService")
public class UserCashServiceImpl implements IUserCashService{
	
	@Autowired
	private IUserCashDao userCashDao;
	@Autowired
	private IAccountCycleService accountCycleService;

	@Override
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
		userCashDao.delete(id);
	}
	

	@Override
	public void modify(UserCashDto userCashDto) {
		userCashDao.updateAmount(userCashDto.getId(), userCashDto.getAmount(), userCashDto.getNotes());
	}
	
	@Override
	public UserCashDto findById(Long id) {
		UserCashDo userCashDo = userCashDao.getById(id);
		if (userCashDo == null) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "该记录不存在");
		}
		return this.buildSingleCashDtoy(userCashDo);
	}
	

	@Override
	public void flatedByCycle(Long cycleNo) {
		int update = userCashDao.updateStateByCycle(cycleNo, CashState.SETTLED.getCode());
		if (update == 0) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "更新操作失败");
		}
	}

	@Override
	public List<UserCashDto> listPayee(UserCashDto userCashDto) {
		return this.list(userCashDto, CashAction.PAYEE);
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
	public List<UserCashDto> listPayer(UserCashDto userCashDto) {
		return this.list(userCashDto, CashAction.PAYER);
	}

	/**
	 * 封装领取款记录
	 */
	private UserCashDo buildUserCashEntity(UserCashDto userCashDto, CashAction cashAction) {
		UserCashDo userCash = new UserCashDo();
		userCash.setAction(cashAction.getCode());
		userCash.setAmount(userCashDto.getAmount());
		userCash.setUserId(userCashDto.getUserId());
		userCash.setUserName(userCashDto.getUserName());
		userCash.setState(CashState.UNSETTLED.getCode());
		userCash.setNotes(userCashDto.getNotes());
		//构建商户信息和创建者
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		userCash.setCreatorId(userTicket.getId());
		userCash.setCreator(userTicket.getUserName());
		userCash.setFirmId(userTicket.getFirmId());
		userCash.setFirmName(userTicket.getFirmName());
		AccountCycleDo accountCycle = accountCycleService.createCycleRecord(userCashDto.getUserId(), userCashDto.getUserName());
		userCash.setCycleNo(accountCycle.getCycleNo());
		return userCash;
	}

	/**
	 * 构建领取款查询条件
	 */
	private void buildUserCashCondition(UserCashDto userCashDto, CashAction cashAction) {
		userCashDto.setState(cashAction.getCode());
		userCashDto.setUserId(NumberUtil.isInteger(userCashDto.getUserName()) ? Long.valueOf(userCashDto.getUserName()) : null);
		userCashDto.setCreatorId(NumberUtil.isInteger(userCashDto.getCreator()) ? Long.valueOf(userCashDto.getCreator()) : null);
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
		cashDto.setAmount(userCashDo.getAmount());
		cashDto.setCreatorId(userCashDo.getCreatorId());
		cashDto.setCreator(userCashDo.getCreator());
		cashDto.setUserId(userCashDo.getUserId());
		cashDto.setUserName(userCashDo.getUserName());
		cashDto.setCreateTime(userCashDo.getCreateTime());
		cashDto.setNotes(userCashDo.getNotes());
		cashDto.setState(userCashDo.getState());
		return cashDto;
	}

}

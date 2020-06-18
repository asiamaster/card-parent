package com.dili.card.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.card.dao.IUserCashDao;
import com.dili.card.dto.UserCashDto;
import com.dili.card.entity.UserCashDo;
import com.dili.card.service.IUserCashService;
import com.dili.card.type.CashAction;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.exception.BusinessException;

@Service
public class UserCashServiceImpl implements IUserCashService{
	
	@Autowired
	private IUserCashDao userCashDao;

	@Override
	public void save(UserCashDto userCashDto, CashAction cashAction) {
		UserCashDo userCashDo = this.buildUserCashEntity(userCashDto, cashAction);
		userCashDao.save(userCashDo);
	}

	@Override
	public List<UserCashDto> list(UserCashDto userCashDto, CashAction cashAction) {
		UserCashDo condition = this.buildUserCashCondition(userCashDto, cashAction);
		List<UserCashDo> userCashs = userCashDao.selectList(condition);
		return this.buildPageUserCash(userCashs);
	}

	@Override
	public void delete(Long id) {
		userCashDao.delete(id);
	}
	
	@Override
	public UserCashDto findById(Long id) {
		UserCashDo userCashDo = userCashDao.getById(id);
		if (userCashDo == null) {
			throw new BusinessException(ResultCode.DATA_ERROR, "该记录不存在");
		}
		return this.buildDetailUserCash(userCashDo);
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

	private UserCashDo buildUserCashEntity(UserCashDto userCashDto, CashAction cashAction) {
		return null;
	}

	private UserCashDo buildUserCashCondition(UserCashDto userCashDto, CashAction cashAction) {
		return null;
	}

	private List<UserCashDto> buildPageUserCash(List<UserCashDo> userCashs) {
		return null;
	}

	private UserCashDto buildDetailUserCash(UserCashDo userCashDo) {
		return null;
	}

}

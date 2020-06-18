package com.dili.card.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.card.dao.IUserCashDao;
import com.dili.card.dto.UserCashDto;
import com.dili.card.service.IUserCashService;
import com.dili.card.type.CashAction;

@Service
public class UserCashServiceImpl implements IUserCashService{
	
	@Autowired
	private IUserCashDao iUserCashDao;

	@Override
	public void save(UserCashDto userCashDto, CashAction cashAction) {
		
	}

	@Override
	public void list(UserCashDto userCashDto, CashAction cashAction) {
		
	}

	@Override
	public void delete(Long id) {
		iUserCashDao.delete(id);
	}

	@Override
	public void listPayee(UserCashDto userCashDto) {
		this.list(userCashDto, CashAction.PAYEE);
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
	public void listPayer(UserCashDto userCashDto) {
		this.list(userCashDto, CashAction.PAYER);
	}

}

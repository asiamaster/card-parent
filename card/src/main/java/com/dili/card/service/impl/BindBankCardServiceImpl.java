package com.dili.card.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.card.dao.IBindBankCardDao;
import com.dili.card.dto.BindBankCardDto;
import com.dili.card.service.IBindBankCardService;
import com.dili.ss.domain.PageOutput;

/**
 * @description： 卡片入库相关功能实现
 *
 * @author ：WangBo
 * @time ：2020年7月17日上午10:21:03
 */
@Service
public class BindBankCardServiceImpl implements IBindBankCardService {
	@Autowired
	private IBindBankCardDao bankCardDao;
	
	@Override
	public PageOutput<List<BindBankCardDto>> list(BindBankCardDto queryParam) {
		List<BindBankCardDto> selectList = bankCardDao.selectList(queryParam);
		PageOutput<List<BindBankCardDto>> pageOut=new PageOutput<List<BindBankCardDto>>();
		pageOut.setData(selectList);
		return pageOut;
	}

	@Override
	public boolean addBind(BindBankCardDto newData) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unBind(BindBankCardDto data) {
		// TODO Auto-generated method stub
		return false;
	}

}

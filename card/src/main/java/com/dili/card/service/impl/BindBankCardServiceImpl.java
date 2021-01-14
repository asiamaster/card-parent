package com.dili.card.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.dili.ss.domain.BaseOutput;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.card.dao.IBindBankCardDao;
import com.dili.card.dto.BindBankCardDto;
import com.dili.card.entity.BindBankCardDo;
import com.dili.card.service.IBindBankCardService;
import com.dili.card.type.BindBankStatus;
import com.dili.card.util.PageUtils;
import com.dili.ss.domain.PageOutput;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

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
	public PageOutput<List<BindBankCardDto>> page(BindBankCardDto queryParam) {
		if(StringUtils.isBlank(queryParam.getSort())) {
			queryParam.setSort("create_time");
			queryParam.setOrder("DESC");
		}
		Page<Object> startPage = PageHelper.startPage(queryParam.getPage(), queryParam.getRows());
		List<BindBankCardDto> selectList = bankCardDao.selectList(queryParam);
		return PageUtils.convert2PageOutput(startPage, selectList);
	}

	@Override
	public List<BindBankCardDto> getList(BindBankCardDto queryParam) {
		PageHelper.startPage(1, 100,false);
		return bankCardDao.selectList(queryParam);
	}

	@Override
	public boolean addBind(BindBankCardDto newDataDto) {
		// TODO 保存本地操作记录
		
		BindBankCardDo newData = new BindBankCardDo();
		BeanUtils.copyProperties(newDataDto, newData);
		newData.setStatus(BindBankStatus.NORMAL.getCode());
		newData.setCreateTime(LocalDateTime.now());
		newData.setOperatorId(newDataDto.getOpId());
		newData.setOperatorName(newDataDto.getOpName());
		bankCardDao.save(newData);
		return true;
	}

	@Override
	public boolean unBind(BindBankCardDto data) {
		BindBankCardDo unBind = new BindBankCardDo();
		unBind.setId(data.getId());
		unBind.setStatus(BindBankStatus.INVALID.getCode());
		unBind.setModifyTime(LocalDateTime.now());
		bankCardDao.update(unBind);
		return true;
	}

}

package com.dili.card.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.card.common.constant.ServiceName;
import com.dili.card.dao.IBindBankCardDao;
import com.dili.card.dto.BindBankCardDto;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.entity.BindBankCardDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.CardManageRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.IAccountQueryService;
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
	
	@Autowired
	private  CardManageRpc cardManageRpc;
	@Autowired
	private IAccountQueryService accountQueryService;

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
		if(StringUtils.isBlank(queryParam.getSort())) {
			queryParam.setSort("create_time");
			queryParam.setOrder("DESC");
		}
		return bankCardDao.selectList(queryParam);
	}

	@Override
	public boolean addBind(BindBankCardDto newDataDto) {
		// TODO 保存本地操作记录
		// 用于校验密码次数过多导致卡片锁定后，输入一次正确的还是能执行操作
		accountQueryService.getByAccountId(newDataDto.getAccountId());
		
		// 校验是否重复
		existsBankNo(newDataDto.getBankNo(), newDataDto.getAccountId(), newDataDto.getFirmId());

		// 保存数据
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
		// 用于校验密码次数过多导致卡片锁定后，输入一次正确的还是能执行操作
		accountQueryService.getByAccountId(data.getAccountId());
		
		// 校验密码
		CardRequestDto cardParam = new CardRequestDto();
		cardParam.setAccountId(data.getAccountId());
		cardParam.setLoginPwd(data.getLoginPwd());
		GenericRpcResolver.resolver(cardManageRpc.checkPassword(cardParam), ServiceName.ACCOUNT);
		
		BindBankCardDo unBind = new BindBankCardDo();
		unBind.setId(data.getId());
		unBind.setStatus(BindBankStatus.INVALID.getCode());
		unBind.setModifyTime(LocalDateTime.now());
		bankCardDao.update(unBind);
		return true;
	}

	@Override
	public boolean existsBankNo(String bankNo, Long accountId, Long firmId) {
		// 校验是否重复
		BindBankCardDto queryParam =new BindBankCardDto();
		queryParam.setAccountId(accountId);
		queryParam.setBankNo(bankNo);
		queryParam.setFirmId(firmId);
		List<BindBankCardDto> list = bankCardDao.selectList(queryParam);
		if (list != null && list.size() >= 1) {
			throw new CardAppBizException("该用户已绑定了相同的卡号");
		}
		return true;
	}

	@Override
	public BindBankCardDo getById(Long id) {
		return bankCardDao.getById(id);
	}

}

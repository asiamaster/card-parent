package com.dili.card.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.card.common.constant.ServiceName;
import com.dili.card.dao.IStorageInDao;
import com.dili.card.dto.BatchCardAddStorageDto;
import com.dili.card.dto.CardStorageDto;
import com.dili.card.dto.CardStorageOutQueryDto;
import com.dili.card.dto.StorageInDto;
import com.dili.card.entity.StorageInDo;
import com.dili.card.rpc.CardStorageRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.ICardStorageInService;
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
public class CardStorageInServiceImpl implements ICardStorageInService {
	@Autowired
	private CardStorageRpc cardStorageRpc;
	@Autowired
	private IStorageInDao storageInDao;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void batchCardStorageIn(StorageInDo storageIn) {
		// 保存入库记录
		storageIn.setCreateTime(LocalDateTime.now());
		storageIn.setModifyTime(LocalDateTime.now());
		storageInDao.save(storageIn);

		// 按号段入库
		BatchCardAddStorageDto batchInfo = new BatchCardAddStorageDto();
		batchInfo.setCardType(storageIn.getCardType());
		batchInfo.setCardFace(storageIn.getCardFace());
		batchInfo.setStorageInId(storageIn.getId());
		batchInfo.setCreator(storageIn.getCreator());
		batchInfo.setCreatorId(storageIn.getCreatorId());
		batchInfo.setStartCardNo(storageIn.getStartCardNo());
		batchInfo.setStartCardNoStr(storageIn.getStartCardNo()); // 原始卡号长度，避免转换长度后丢失前面的0
		batchInfo.setEndCardNo(storageIn.getEndCardNo());
		batchInfo.setFirmId(storageIn.getFirmId());
		batchInfo.setFirmName(storageIn.getFirmName());
		batchInfo.setNotes(storageIn.getNotes());

		GenericRpcResolver.resolver(cardStorageRpc.batchAddCard(batchInfo), ServiceName.ACCOUNT);
	}

	@Override
	public PageOutput<List<StorageInDto>> list(CardStorageOutQueryDto queryParam) {
		Page<Object> startPage = PageHelper.startPage(queryParam.getPage(), queryParam.getRows());
		List<StorageInDto> list = storageInDao.selectList(queryParam);
		return PageUtils.convert2PageOutput(startPage, list);
	}

	@Override
	//@GlobalTransactional
	@Transactional(rollbackFor = Exception.class)
	public void delStorageIn(Long[] ids, Long firmId) {
		storageInDao.batchRemove(ids);

		// 远程删除库存数据
		CardStorageDto delDto = new CardStorageDto();
		delDto.setStorageInId(ids[0]);
		GenericRpcResolver.resolver(cardStorageRpc.delByStorageInId(delDto), ServiceName.ACCOUNT);
	}

}

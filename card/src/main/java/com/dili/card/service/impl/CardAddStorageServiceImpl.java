package com.dili.card.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.card.dao.IApplyRecordDao;
import com.dili.card.dto.CardStorageDto;
import com.dili.card.rpc.CardStorageRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.ICardAddStorageService;
import com.dili.ss.domain.PageOutput;

/**
 * @description： 
 *          卡片入库相关功能实现
 * @author ：WangBo
 * @time ：2020年7月17日上午10:21:03
 */
@Service
public class CardAddStorageServiceImpl implements ICardAddStorageService {
    @Autowired
    private CardStorageRpc cardStorageRpc;

	@Override
	public void addCard(CardStorageDto addParam) {
		GenericRpcResolver.resolver(cardStorageRpc.addCard(addParam), "account-service");
	}

	@Override
	public PageOutput<List<CardStorageDto>> cardStorageList(CardStorageDto queryParam) {
		return cardStorageRpc.pageList(queryParam);
	}

	@Override
	public void voidCard(String cardNo, String remark) {
		CardStorageDto queryParam = new CardStorageDto();
		queryParam.setCardNo(cardNo);
		GenericRpcResolver.resolver(cardStorageRpc.voidCard(queryParam), "account-service");
	}

   

}

package com.dili.card.service.impl;

import com.dili.card.dto.CardRequestDto;
import com.dili.card.entity.CardAggregationWrapper;
import com.dili.card.rpc.CardManageRpc;
import com.dili.card.service.ICardManageService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * @description： 卡片退卡换卡等操作service实现
 *
 * @author ：WangBo
 * @time ：2020年4月28日下午5:09:47
 */
@Service("cardManageService")
public class CardManageServiceImpl implements ICardManageService {

    @Resource
    private CardManageRpc cardManageRpc;

    /**
     * @param cardParam
     */
    @Transactional
    @Override
    public void unLostCard(CardRequestDto cardParam) {
        BaseOutput<CardAggregationWrapper> baseOutput = cardManageRpc.unLostCard(cardParam);
        if (!baseOutput.isSuccess()) {
            throw new BusinessException(baseOutput.getCode(), baseOutput.getMessage());
        }
        CardAggregationWrapper wrapper = baseOutput.getData();

    }
}

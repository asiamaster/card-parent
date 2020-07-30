package com.dili.card.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardQuery;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.AccountManageRpc;
import com.dili.card.rpc.CardManageRpc;
import com.dili.card.rpc.resolver.AccountQueryRpcResolver;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.service.IAccountManageService;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.CardStatus;
import com.dili.card.type.DisableState;
import com.dili.card.type.OperateType;
import com.dili.ss.domain.BaseOutput;

@Service("accountManageService")
public class AccountManageServiceImpl implements IAccountManageService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CardManageServiceImpl.class);

	@Autowired
	private AccountManageRpc accountManageRpc;
    @Resource
    private CardManageRpc cardManageRpc;
    @Resource
    private ISerialService serialService;
    @Resource
    private PayRpcResolver payRpcResolver;
    @Resource
    private AccountQueryRpcResolver accountQueryRpcResolver;
    @Resource
    protected IAccountQueryService accountQueryService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void frozen(CardRequestDto cardRequestDto) {
		//保存本地操作记录 
		UserAccountCardResponseDto accountCard = accountQueryRpcResolver.findSingle(UserAccountCardQuery.createInstance(cardRequestDto.getAccountId()));
		if (Integer.valueOf(CardStatus.LOSS.getCode()).equals(accountCard.getCardState())) {
            throw new CardAppBizException("", String.format("该卡为%s状态,不能进行解挂", CardStatus.getName(accountCard.getCardState())));
        }
		if (!Integer.valueOf(DisableState.ENABLED.getCode()).equals(accountCard.getCardState())) {
            throw new CardAppBizException("", String.format("该卡为%s状态,不能进行操作", DisableState.getName(accountCard.getDisabledState())));
        }
		BusinessRecordDo businessRecord = serialService.createBusinessRecord(cardRequestDto, accountCard, temp -> {
            temp.setType(OperateType.FROZEN_ACCOUNT.getCode());
        });
		serialService.saveBusinessRecord(businessRecord);			
		//远程冻结卡账户操作
    	BaseOutput<?> baseOutput = accountManageRpc.frozen(cardRequestDto);
        if (!baseOutput.isSuccess()) {
            throw new CardAppBizException(baseOutput.getCode(), baseOutput.getMessage());
        } 
        //远程冻结资金账户
        try {
        	if (accountCard.isMaster()) {
        		payRpcResolver.freezeFundAccount(CreateTradeRequestDto.createCommon(accountCard.getFundAccountId(), accountCard.getAccountId()));
        	}
		} catch (Exception e) {
			LOGGER.error("远程冻结资金账户失败:" + accountCard.getAccountId(), e);
		}
        //记录远程操作记录
        try {
            SerialDto serialDto = serialService.createAccountSerial(businessRecord, null);
            serialService.handleSuccess(serialDto);
        } catch (Exception e) {
            LOGGER.error("freezeFundAccount", e);
        }	
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void unfrozen(CardRequestDto cardRequestDto) {
		//保存本地操作记录 
		UserAccountCardResponseDto accountCard = accountQueryRpcResolver.findSingle(UserAccountCardQuery.createInstance(cardRequestDto.getAccountId()));
		if (Integer.valueOf(CardStatus.LOSS.getCode()).equals(accountCard.getCardState())) {
            throw new CardAppBizException("", String.format("该卡为%s状态,不能进行解挂", CardStatus.getName(accountCard.getCardState())));
        }
		if (!Integer.valueOf(DisableState.DISABLED.getCode()).equals(accountCard.getDisabledState())) {
            throw new CardAppBizException("", String.format("该卡为%s状态,不能进行操作", DisableState.getName(accountCard.getDisabledState())));
        }
		BusinessRecordDo businessRecord = serialService.createBusinessRecord(cardRequestDto, accountCard, temp -> {
            temp.setType(OperateType.UNFROZEN_ACCOUNT.getCode());
        });
		serialService.saveBusinessRecord(businessRecord);		
		//远程解冻账户操作 
    	BaseOutput<?> baseOutput = accountManageRpc.unfrozen(cardRequestDto);
        if (!baseOutput.isSuccess()) {
            throw new CardAppBizException(baseOutput.getCode(), baseOutput.getMessage());
        } 
        //远程解冻资金账户
        try {
        	if (accountCard.isMaster()) {
        		payRpcResolver.unfreezeFundAccount(CreateTradeRequestDto.createCommon(accountCard.getFundAccountId(), accountCard.getAccountId()));
			}
		} catch (Exception e) {
			LOGGER.error("远程解冻资金账户失败:" + accountCard.getAccountId(), e);
		}
        //记录远程操作记录
        try {
            SerialDto serialDto = serialService.createAccountSerial(businessRecord, null);
            serialService.handleSuccess(serialDto);
        } catch (Exception e) {
            LOGGER.error("unfreezeFundAccount", e);
        }	
	}
	
	/**
     * 保存本地操作记录
     */
	private BusinessRecordDo saveSerialRecord(CardRequestDto cardParam, OperateType operateType) {
		return null;
	}

	 /**
     * 保存远程流水记录
     */
	private void saveRemoteSerialRecord(CardRequestDto cardParam, BusinessRecordDo businessRecord) {
	}

}

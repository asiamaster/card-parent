package com.dili.card.service.impl;

import javax.annotation.Resource;

import com.dili.card.dto.UserAccountSingleQueryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardQuery;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.AccountManageRpcResolver;
import com.dili.card.rpc.resolver.AccountQueryRpcResolver;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.service.IAccountManageService;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.CardStatus;
import com.dili.card.type.DisableState;
import com.dili.card.type.OperateType;
import com.dili.ss.constant.ResultCode;

@Service("accountManageService")
public class AccountManageServiceImpl implements IAccountManageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CardManageServiceImpl.class);

	@Autowired
	private AccountManageRpcResolver accountManageRpcResolver;
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
		//校验账户信息
		UserAccountCardResponseDto accountCard = this.validateCardAccount(cardRequestDto.getAccountId(), false, DisableState.ENABLED);
		//保存本地记录
		BusinessRecordDo businessRecord = this.saveLocalSerialRecord(cardRequestDto, accountCard, OperateType.FROZEN_ACCOUNT);
		//远程冻结卡账户操作
    	accountManageRpcResolver.frozen(cardRequestDto);
        //远程冻结资金账户
        try {
        	if (accountCard.getMaster()) {
        		payRpcResolver.freezeFundAccount(CreateTradeRequestDto.createCommon(accountCard.getFundAccountId(), accountCard.getAccountId()));
        	}
		} catch (Exception e) {
			LOGGER.error("远程冻结资金账户失败:" + accountCard.getAccountId(), e);
		}
        //记录远程操作记录
        this.saveRemoteSerialRecord(businessRecord);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void unfrozen(CardRequestDto cardRequestDto) {
		//校验账户信息
		UserAccountCardResponseDto accountCard = this.validateCardAccount(cardRequestDto.getAccountId(), false, DisableState.DISABLED);
		//保存本地记录
		BusinessRecordDo businessRecord = this.saveLocalSerialRecord(cardRequestDto, accountCard, OperateType.UNFROZEN_ACCOUNT);
		//远程解冻账户操作
		accountManageRpcResolver.unfrozen(cardRequestDto);
        //远程解冻资金账户
        try {
        	if (accountCard.getMaster()) {
        		payRpcResolver.unfreezeFundAccount(CreateTradeRequestDto.createCommon(accountCard.getFundAccountId(), accountCard.getAccountId()));
			}
		} catch (Exception e) {
			LOGGER.error("远程解冻资金账户失败:" + accountCard.getAccountId(), e);
		}
        //记录远程操作记录
        this.saveRemoteSerialRecord(businessRecord);
	}

	/**
	 * 校验卡状态
	 * @param account 账户信息
	 * @param checkCardLoss 是否判定挂失状态
	 * @return
	 */
	private UserAccountCardResponseDto validateCardAccount(Long account, boolean checkCardLoss, DisableState disableState) {
		UserAccountSingleQueryDto query = new UserAccountSingleQueryDto();
		query.setAccountId(account);
		UserAccountCardResponseDto accountCard = accountQueryRpcResolver.findSingle(query);
		if (checkCardLoss && Integer.valueOf(CardStatus.LOSS.getCode()).equals(accountCard.getCardState())) {
            throw new CardAppBizException("", String.format("该卡为%s状态,不能进行操作", CardStatus.getName(accountCard.getCardState())));
        }
		if (!Integer.valueOf(disableState.getCode()).equals(accountCard.getCardState())) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, String.format("该卡账户为%s状态,不能进行操作", DisableState.getName(accountCard.getDisabledState())));
        }
		return accountCard;
	}

	/**
     * 保存本地操作记录
     */
	private BusinessRecordDo saveLocalSerialRecord(CardRequestDto cardParam, UserAccountCardResponseDto accountCard, OperateType operateType) {
		BusinessRecordDo businessRecord = serialService.createBusinessRecord(cardParam, accountCard, temp -> {
            temp.setType(operateType.getCode());
        });
		serialService.saveBusinessRecord(businessRecord);
		return businessRecord;
	}

	 /**
     * 保存远程流水记录
     */
	private void saveRemoteSerialRecord(BusinessRecordDo businessRecord) {
		try {
            SerialDto serialDto = serialService.createAccountSerial(businessRecord, null);
            serialService.handleSuccess(serialDto);
        } catch (Exception e) {
            LOGGER.error("unfreezeFundAccount", e);
        }
	}

}

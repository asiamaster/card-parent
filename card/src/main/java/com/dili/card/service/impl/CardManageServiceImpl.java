package com.dili.card.service.impl;

import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.CardManageRpc;
import com.dili.card.rpc.resolver.AccountQueryRpcResolver;
import com.dili.card.rpc.resolver.CardManageRpcResolver;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.ICardManageService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.CardStatus;
import com.dili.card.type.OperateType;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(CardManageServiceImpl.class);

    @Autowired
    private CardManageRpcResolver cardManageRpcResolver;
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
    @Autowired
    private IAccountCycleService accountCycleService;

    /**
     * @param cardParam
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unLostCard(CardRequestDto cardParam) {
        UserAccountCardResponseDto accountCard = accountQueryService.getByAccountId(cardParam);
        if (!Integer.valueOf(CardStatus.LOSS.getCode()).equals(accountCard.getCardState())) {
            throw new CardAppBizException("", String.format("该卡为%s状态,不能进行解挂", CardStatus.getName(accountCard.getCardState())));
        }
        BusinessRecordDo businessRecordDo = serialService.createBusinessRecord(cardParam, accountCard, temp -> {
            temp.setType(OperateType.LOSS_REMOVE.getCode());
        });
        serialService.saveBusinessRecord(businessRecordDo);
        BaseOutput<?> baseOutput = cardManageRpc.unLostCard(cardParam);
        if (!baseOutput.isSuccess()) {
            throw new CardAppBizException(baseOutput.getCode(), baseOutput.getMessage());
        }
        try {//成功则修改状态及期初期末金额，存储操作流水
            SerialDto serialDto = serialService.createAccountSerial(businessRecordDo, (temp, feeType) -> {

            });
            serialService.handleSuccess(serialDto, false);
        } catch (Exception e) {
            LOGGER.error("unLostCard", e);
        }
    }

	@Override
	public void returnCard(CardRequestDto cardParam) {
    	//校验卡状态
    	UserAccountCardResponseDto accountCard =  accountQueryService.getByAccountId(cardParam.getAccountId());
    	if (!Integer.valueOf(CardStatus.NORMAL.getCode()).equals(accountCard.getCardState())) {
            throw new CardAppBizException("", String.format("该卡为%s状态,不能进行退卡", CardStatus.getName(accountCard.getCardState())));
        }
        //余额校验
		BalanceResponseDto  balanceResponseDto = payRpcResolver.findBalanceByFundAccountId(accountCard.getFundAccountId());
        if (balanceResponseDto.getBalance() > 100L) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "卡余额大于1元,不能退卡");
        }
		//保存本地操作记录
		BusinessRecordDo businessRecordDo = serialService.createBusinessRecord(cardParam, accountCard, temp -> {
            temp.setType(OperateType.LOSS_REMOVE.getCode());
        });
		serialService.saveBusinessRecord(businessRecordDo);
		//远程调用退卡操作
		cardManageRpcResolver.returnCard(cardParam);
		//记录远程操作记录
		saveRemoteSerialRecord(businessRecordDo);
	}

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void resetLoginPwd(CardRequestDto cardParam) {
        //保存本地操作记录
        BusinessRecordDo businessRecord = saveSerialRecord(cardParam, OperateType.RESET_PWD);
        //远程重置密码操作
        cardManageRpcResolver.resetLoginPwd(cardParam);
        //记录远程操作记录
        this.saveRemoteSerialRecord(businessRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unLockCard(CardRequestDto cardParam) {
        UserAccountCardResponseDto accountCard = accountQueryService.getByAccountId(cardParam);
        if (!Integer.valueOf(CardStatus.LOCKED.getCode()).equals(accountCard.getCardState())) {
            throw new CardAppBizException("", String.format("该卡为%s状态,不能进行解锁", CardStatus.getName(accountCard.getCardState())));
        }
        BusinessRecordDo businessRecordDo = serialService.createBusinessRecord(cardParam, accountCard, temp -> {
            temp.setType(OperateType.LIFT_LOCKED.getCode());
        });
        serialService.saveBusinessRecord(businessRecordDo);
        BaseOutput<?> baseOutput = cardManageRpc.unLostCard(cardParam);
        if (!baseOutput.isSuccess()) {
            throw new CardAppBizException(baseOutput.getCode(), baseOutput.getMessage());
        }

        this.saveRemoteSerialRecord(businessRecordDo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reportLossCard(CardRequestDto cardParam) {
        UserAccountCardResponseDto userAccount = accountQueryService.getByCardNo(cardParam.getCardNo());
        BusinessRecordDo businessRecord = serialService.createBusinessRecord(cardParam, userAccount,
                record -> record.setType(OperateType.LOSS_CARD.getCode()));
        serialService.saveBusinessRecord(businessRecord);

        cardManageRpcResolver.reportLossCard(cardParam);

        this.saveRemoteSerialRecord(businessRecord);
    }


    /**
     * 校验账户余额
     */
    private void validatedBanlance(CardRequestDto cardParam) {
        UserAccountCardResponseDto accountCard = accountQueryService.getByAccountId(cardParam);
        //余额校验
        BalanceResponseDto balanceResponseDto = payRpcResolver.findBalanceByFundAccountId(accountCard.getFundAccountId());
        if (balanceResponseDto.getBalance() != 0L) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "卡余额不为0,不能退卡");
        }
    }

    /**
     * 保存本地操作记录
     */
    private BusinessRecordDo saveSerialRecord(CardRequestDto cardParam, OperateType operateType) {
        BusinessRecordDo businessRecord = new BusinessRecordDo();
        serialService.buildCommonInfo(cardParam, businessRecord);
        businessRecord.setType(operateType.getCode());
        serialService.saveBusinessRecord(businessRecord);
        return businessRecord;
    }

    /**
     * saveRemoteSerialRecord
     */
    private void saveRemoteSerialRecord(BusinessRecordDo businessRecord) {
        try {//成功则修改状态及期初期末金额，存储操作流水
            SerialDto serialDto = serialService.createAccountSerial(businessRecord, null);
            serialService.handleSuccess(serialDto, false);
        } catch (Exception e) {
            LOGGER.error("unLostCard", e);
        }
    }


}

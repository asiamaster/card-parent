package com.dili.card.service.impl;

import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.CardManageRpc;
import com.dili.card.rpc.resolver.AccountQueryRpcResolver;
import com.dili.card.rpc.resolver.CardManageRpcResolver;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.service.ICardManageService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.OperateType;
import com.dili.card.type.TradeChannel;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


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
    /**
     * @param cardParam
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unLostCard(CardRequestDto cardParam) {
        BusinessRecordDo businessRecord = new BusinessRecordDo();
        serialService.buildCommonInfo(cardParam, businessRecord);
        businessRecord.setType(OperateType.LOSS_REMOVE.getCode());
        //businessRecord.setAmount(0L);
        serialService.saveBusinessRecord(businessRecord);
        BaseOutput<?> baseOutput = cardManageRpc.unLostCard(cardParam);
        if (!baseOutput.isSuccess()) {
            //针对解挂失操作，暂时处理为解挂失失败则回滚业务办理记录
            throw new CardAppBizException(baseOutput.getCode(), baseOutput.getMessage());
        }
        try {//成功则修改状态及期初期末金额，存储操作流水
            SerialDto serialDto = buildNoFundSerial(cardParam, businessRecord);
            serialService.handleSuccess(serialDto, false);
        } catch (Exception e) {
            LOGGER.error("unLostCard", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
	@Override
	public void returnCard(CardRequestDto cardParam) {
    	//校验账户余额
		validatedBanlance(cardParam);
		//保存本地操作记录
		BusinessRecordDo businessRecord = saveSerialRecord(cardParam, OperateType.REFUND_CARD);
		//远程调用退卡操作
		cardManageRpcResolver.returnCard(cardParam);
		//记录远程操作记录
		saveRemoteSerialRecord(cardParam, businessRecord);
	}
    
    @Transactional(rollbackFor = Exception.class)
	@Override
	public void resetLoginPwd(CardRequestDto cardParam){
		//保存本地操作记录
    	BusinessRecordDo businessRecord = saveSerialRecord(cardParam, OperateType.RESET_PWD);
		//远程重置密码操作
		cardManageRpcResolver.resetLoginPwd(cardParam);
		//记录远程操作记录
		saveRemoteSerialRecord(cardParam, businessRecord);
	}

	@Transactional(rollbackFor = Exception.class)
    @Override
    public void unLockCard(CardRequestDto cardParam) {
        BusinessRecordDo businessRecord = new BusinessRecordDo();
        serialService.buildCommonInfo(cardParam, businessRecord);
        businessRecord.setType(OperateType.LIFT_LOCKED.getCode());
        serialService.saveBusinessRecord(businessRecord);
        BaseOutput<?> baseOutput = cardManageRpc.unLockCard(cardParam);
        if (!baseOutput.isSuccess()) {
            throw new CardAppBizException(baseOutput.getCode(), baseOutput.getMessage());
        }
        try {//成功则修改状态及期初期末金额，存储操作流水
            SerialDto serialDto = buildNoFundSerial(cardParam, businessRecord);
            serialService.handleSuccess(serialDto, false);
        } catch (Exception e) {
            LOGGER.error("unLockCard", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeCard(CardRequestDto cardParam) {
        BusinessRecordDo businessRecord = new BusinessRecordDo();
        serialService.buildCommonInfo(cardParam, businessRecord);
        businessRecord.setType(OperateType.CHANGE.getCode());
        serialService.saveBusinessRecord(businessRecord);

        cardManageRpcResolver.changeCard(cardParam);

        try {
            //成功则修改状态及期初期末金额，存储操作流水
            SerialDto serialDto = buildNoFundSerial(cardParam, businessRecord);
            serialService.handleSuccess(serialDto);
        } catch (Exception e) {
            LOGGER.error("changeCard", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reportLossCard(CardRequestDto cardParam) {
        BusinessRecordDo businessRecord = new BusinessRecordDo();
        serialService.buildCommonInfo(cardParam, businessRecord);
        businessRecord.setType(OperateType.LOSS_CARD.getCode());
        businessRecord.setAmount(cardParam.getServiceFee());
        businessRecord.setTradeChannel(TradeChannel.CASH.getCode());
        serialService.saveBusinessRecord(businessRecord);

        cardManageRpcResolver.reportLossCard(cardParam);

        try {
            //成功则修改状态及期初期末金额，存储操作流水
            SerialDto serialDto = buildNoFundSerial(cardParam, businessRecord);
            serialService.handleSuccess(serialDto);
        } catch (Exception e) {
            LOGGER.error("changeCard", e);
        }
    }

    /**
     * 构建操作流水 后期根据各业务代码调整优化(针对无资金操作的流水构建)
     * @param cardParam
     * @return
     */
    private SerialDto buildNoFundSerial(CardRequestDto cardParam, BusinessRecordDo businessRecord) {
        SerialDto serialDto = new SerialDto();
        serialDto.setSerialNo(businessRecord.getSerialNo());
        //serialDto.setStartBalance(0L);
        //serialDto.setEndBalance(0L);
        List<SerialRecordDo> serialRecordList = new ArrayList<>(1);
        SerialRecordDo serialRecord = new SerialRecordDo();
        serialService.copyCommonFields(serialRecord, businessRecord);
        //注释字段为当前操作不需要的字段  其他业务可能需要
        //serialRecord.setAction();
        //serialRecord.setStartBalance(0L);
        //serialRecord.setAmount(0L);
        //serialRecord.setEndBalance(0L);
        //serialRecord.setTradeType();
        //serialRecord.setTradeChannel();
        //serialRecord.setTradeNo(businessRecord.getTradeNo());
        //serialRecord.setFundItem();
        //serialRecord.setFundItemName();
        /** 操作时间-与支付系统保持一致 */
        serialRecord.setOperateTime(businessRecord.getOperateTime());
        //serialRecord.setNotes();
        serialRecordList.add(serialRecord);
        serialDto.setSerialRecordList(serialRecordList);
        return serialDto;
    }
    
    /**
     * 校验账户余额
     */
	private void validatedBanlance(CardRequestDto cardParam) {
		UserAccountCardResponseDto accountCard = accountQueryRpcResolver.findByAccountId(cardParam.getAccountId());
        //余额校验
		BalanceResponseDto  balanceResponseDto = payRpcResolver.findBalanceByFundAccountId(accountCard.getFundAccountId());
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
	private void saveRemoteSerialRecord(CardRequestDto cardParam, BusinessRecordDo businessRecord) {
		try {//成功则修改状态及期初期末金额，存储操作流水
            SerialDto serialDto = buildNoFundSerial(cardParam, businessRecord);
            serialService.handleSuccess(serialDto, false);
        } catch (Exception e) {
            LOGGER.error("unLostCard", e);
        }
	}


}

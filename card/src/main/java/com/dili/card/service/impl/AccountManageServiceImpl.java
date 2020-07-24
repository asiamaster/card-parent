package com.dili.card.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.rpc.AccountManageRpc;
import com.dili.card.rpc.CardManageRpc;
import com.dili.card.rpc.resolver.AccountQueryRpcResolver;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.service.IAccountManageService;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.OperateType;

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
		//保存本地操作记录 TODO
    	BusinessRecordDo businessRecord = saveSerialRecord(cardRequestDto, OperateType.FROZEN_ACCOUNT);
		//远程冻结账户操作 TODO
    	accountManageRpc.frozen(cardRequestDto);
		//记录远程操作记录 TODO
		saveRemoteSerialRecord(cardRequestDto, businessRecord);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void unfrozen(CardRequestDto cardRequestDto) {
		//保存本地操作记录 TODO
    	BusinessRecordDo businessRecord = saveSerialRecord(cardRequestDto, OperateType.UNFROZEN_ACCOUNT);
		//远程冻结账户操作 TODO
    	accountManageRpc.unfrozen(cardRequestDto);
		//记录远程操作记录 TODO
		saveRemoteSerialRecord(cardRequestDto, businessRecord);
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
	
	/**
     * 构建操作流水 后期根据各业务代码调整优化(针对无资金操作的流水构建)
     * @param cardParam
     * @return
     */
    private SerialDto buildNoFundSerial(CardRequestDto cardParam, BusinessRecordDo businessRecord) {
        SerialDto serialDto = new SerialDto();
        serialDto.setSerialNo(businessRecord.getSerialNo());
        List<SerialRecordDo> serialRecordList = new ArrayList<>(1);
        SerialRecordDo serialRecord = new SerialRecordDo();
        serialService.copyCommonFields(serialRecord, businessRecord);
        serialRecord.setOperateTime(businessRecord.getOperateTime());
        serialRecordList.add(serialRecord);
        serialDto.setSerialRecordList(serialRecordList);
        return serialDto;
    }


}

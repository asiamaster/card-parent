package com.dili.card.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.card.dto.OpenCardDto;
import com.dili.card.dto.OpenCardResponseDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.rpc.OpenCardRpc;
import com.dili.card.rpc.SerialRecordRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.IOpenCardService;
import com.dili.ss.domain.BaseOutput;

/**
 * @description： 开卡service实现
 * 
 * @author ：WangBo
 * @time ：2020年6月19日下午5:54:23
 */
@Service("openCardService")
public class OpenCardServiceImpl implements IOpenCardService {
	@Resource
	private OpenCardRpc openCardRpc;
	@Resource
	private SerialRecordRpc serialRecordRpc;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public OpenCardResponseDto openMasterCard(OpenCardDto openCardInfo) {
		// 调用账户服务开卡
		BaseOutput<OpenCardResponseDto> baseOutPut = openCardRpc.openMasterCard(openCardInfo);
		OpenCardResponseDto openCardResponse = GenericRpcResolver.resolver(baseOutPut);
		//保存全局操作记录
		SerialDto serialDto=new SerialDto();
		GenericRpcResolver.resolver(serialRecordRpc.batchSave(serialDto));
		// 保存卡务柜台操作记录
		
		
		return openCardResponse;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public OpenCardResponseDto openSlaveCard(OpenCardDto openCardInfo) {
		BaseOutput<OpenCardResponseDto> baseOutPut = openCardRpc.openSlaveCard(openCardInfo);
		OpenCardResponseDto openCardResponse = GenericRpcResolver.resolver(baseOutPut);
		return openCardResponse;
	}
	
	private SerialDto buildSerialRecord(OpenCardDto openCardInfo,Long accountId) {
		SerialDto serial=new SerialDto();
		serial.setAccountId(accountId);
		return serial;
	}
}

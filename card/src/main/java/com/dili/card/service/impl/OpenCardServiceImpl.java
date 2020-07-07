package com.dili.card.service.impl;

import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.dili.card.dto.OpenCardDto;
import com.dili.card.dto.OpenCardResponseDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.entity.AccountCycleDo;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.rpc.OpenCardRpc;
import com.dili.card.rpc.SerialRecordRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.IOpenCardService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.BizNoType;
import com.dili.card.type.OperateType;
import com.dili.card.type.ServiceName;
import com.dili.ss.domain.BaseOutput;
import com.google.common.collect.Lists;

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
	@Resource
	IAccountCycleService accountCycleService;
	@Resource
	private UidRpcResovler uidRpcResovler;
	@Resource
	private ISerialService serialService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public OpenCardResponseDto openMasterCard(OpenCardDto openCardInfo) {
		// 调用账户服务开卡
		BaseOutput<OpenCardResponseDto> baseOutPut = openCardRpc.openMasterCard(openCardInfo);
		OpenCardResponseDto openCardResponse = GenericRpcResolver.resolver(baseOutPut, ServiceName.ACCOUNT);
		// 保存卡务柜台操作记录
		BusinessRecordDo buildBusinessRecordDo = buildBusinessRecordDo(openCardInfo, openCardResponse.getAccountId());
		serialService.saveBusinessRecord(buildBusinessRecordDo);
		// 保存全局操作记录
		SerialRecordDo serialRecord = buildSerialRecord(openCardInfo, openCardResponse.getAccountId());
		serialService.copyCommonFields(serialRecord, buildBusinessRecordDo);
		SerialDto serialDto = new SerialDto();
		serialDto.setSerialRecordList(Lists.newArrayList(serialRecord));
		serialService.saveSerialRecord(serialDto);

		return openCardResponse;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public OpenCardResponseDto openSlaveCard(OpenCardDto openCardInfo) {
		BaseOutput<OpenCardResponseDto> baseOutPut = openCardRpc.openSlaveCard(openCardInfo);
		OpenCardResponseDto openCardResponse = GenericRpcResolver.resolver(baseOutPut, ServiceName.ACCOUNT);
		// 保存卡务柜台操作记录
		BusinessRecordDo buildBusinessRecordDo = buildBusinessRecordDo(openCardInfo, openCardResponse.getAccountId());
		serialService.saveBusinessRecord(buildBusinessRecordDo);
		// 保存全局操作记录
		SerialRecordDo serialRecord = buildSerialRecord(openCardInfo, openCardResponse.getAccountId());
		serialService.copyCommonFields(serialRecord, buildBusinessRecordDo);
		SerialDto serialDto = new SerialDto();
		serialDto.setSerialRecordList(Lists.newArrayList(serialRecord));
		serialService.saveSerialRecord(serialDto);

		return openCardResponse;
	}

	/**
	 * 构建柜员操作日志
	 * 
	 * @param openCardInfo
	 * @param accountId
	 * @return
	 */
	private BusinessRecordDo buildBusinessRecordDo(OpenCardDto openCardInfo, Long accountId) {

		BusinessRecordDo serial = new BusinessRecordDo();
		serial.setAccountId(accountId);
		serial.setCardNo(openCardInfo.getCardNo());
		serial.setCustomerId(openCardInfo.getCustomerId());
		serial.setCustomerName(openCardInfo.getName());
		serial.setCustomerNo(openCardInfo.getCustomerNo());
		AccountCycleDo cycleDo = accountCycleService.findActiveCycleByUserId(openCardInfo.getCreatorId(),
				openCardInfo.getCreator(), openCardInfo.getCreatorCode());
		serial.setCycleNo(cycleDo.getCycleNo());
		serial.setFirmId(openCardInfo.getFirmId());
		serial.setOperatorId(openCardInfo.getCreatorId());
		serial.setOperatorName(openCardInfo.getCreator());
		serial.setOperatorNo(openCardInfo.getCreatorCode());
		serial.setOperateTime(LocalDateTime.now());
		serial.setNotes("开卡");
		serial.setSerialNo(uidRpcResovler.bizNumber(BizNoType.OPERATE_SERIAL_NO.getCode()));
		serial.setType(OperateType.ACCOUNT_TRANSACT.getCode());
		return serial;
	}

	/**
	 * 生成全局日志
	 * 
	 * @param openCardInfo
	 * @param accountId
	 */
	private SerialRecordDo buildSerialRecord(OpenCardDto openCardInfo, Long accountId) {
		SerialRecordDo record = new SerialRecordDo();
		record.setAccountId(accountId);
		record.setCardNo(openCardInfo.getCardNo());
		record.setCustomerId(openCardInfo.getCustomerId());
		record.setCustomerName(openCardInfo.getName());
		record.setCustomerNo(openCardInfo.getCustomerNo());
		record.setFirmId(openCardInfo.getFirmId());
		record.setSerialNo(uidRpcResovler.bizNumber(BizNoType.OPERATE_SERIAL_NO.getCode()));
		record.setNotes("开卡");
		record.setOperatorId(openCardInfo.getCreatorId());
		record.setOperatorName(openCardInfo.getCreator());
		record.setOperatorNo(openCardInfo.getCreatorCode());
		record.setTradeType(OperateType.ACCOUNT_TRANSACT.getCode());
		record.setType(OperateType.ACCOUNT_TRANSACT.getCode());
		record.setOperateTime(LocalDateTime.now());
		return record;
	}
}

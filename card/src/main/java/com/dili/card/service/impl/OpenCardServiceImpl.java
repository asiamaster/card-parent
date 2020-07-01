package com.dili.card.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.dili.card.type.BizNoType;
import com.dili.card.type.TradeType;
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
	@Resource
	IAccountCycleService accountCycleService;
	@Resource
	private UidRpcResovler uidRpcResovler;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public OpenCardResponseDto openMasterCard(OpenCardDto openCardInfo) {
		// 调用账户服务开卡
		BaseOutput<OpenCardResponseDto> baseOutPut = openCardRpc.openMasterCard(openCardInfo);
		OpenCardResponseDto openCardResponse = GenericRpcResolver.resolver(baseOutPut);
		// 保存全局操作记录
		SerialDto serialDto = new SerialDto();
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
	private BusinessRecordDo buildBusinessRecordDo(OpenCardDto openCardInfo, Long accountId) {
		BusinessRecordDo serial = new BusinessRecordDo();
//		serial.setAccountId(accountId);
//		serial.setCardNo(openCardInfo.getCardNo());
//		serial.setCustomerId(openCardInfo.getCustomerId());
//		AccountCycleDo cycleDo = accountCycleService.findActiveCycleByUserId(openCardInfo.getCreatorId(), openCardInfo.getCreator());
//		serial.setCycleNo(cycleDo.getCycleNo());
//		serial.setFirmId(openCardInfo.getFirmId());
//		serial.setOperatorId(openCardInfo.getCreatorId());
//		serial.setSerialNo(serialNo);
		return serial;
	}
	private SerialRecordDo buildSerialRecord(OpenCardDto openCardInfo, Long accountId) {
		SerialRecordDo record = new SerialRecordDo();
		record.setAccountId(accountId);
		record.setCardNo(openCardInfo.getCardNo());
		record.setCustomerId(openCardInfo.getCustomerId());
		record.setFirmId(openCardInfo.getFirmId());
		record.setOperatorId(openCardInfo.getCreatorId());
		record.setSerialNo(uidRpcResovler.bizNumber(BizNoType.OPERATE_SERIAL_NO.getCode()));
		record.setCustomerName(openCardInfo.getName());
//		record.setCustomerNo(customerNo);
		record.setNotes("办理主卡");
		record.setOperatorName(openCardInfo.getCreator());
//		record.setOperatorNo(openCardInfo.getc);
//		record.setTradeType(TradeType);
		
		return record;
	}
	public static void main(String[] args) {
		List<SerialRecordDo> serialRecordList = new ArrayList<>();
		serialRecordList.add(new SerialRecordDo());
		System.out.println(serialRecordList.size());
	}
}

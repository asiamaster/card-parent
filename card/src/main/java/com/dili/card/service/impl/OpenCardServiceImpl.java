package com.dili.card.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.assets.sdk.rpc.BusinessChargeItemRpc;
import com.dili.card.dto.OpenCardDto;
import com.dili.card.dto.OpenCardResponseDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.CreateTradeResponseDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.entity.AccountCycleDo;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.rpc.OpenCardRpc;
import com.dili.card.rpc.PayRpc;
import com.dili.card.rpc.SerialRecordRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.IOpenCardService;
import com.dili.card.service.IRuleFeeService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.BizNoType;
import com.dili.card.type.FundItem;
import com.dili.card.type.OperateState;
import com.dili.card.type.OperateType;
import com.dili.card.type.RuleFeeBusinessType;
import com.dili.card.type.SystemSubjectType;
import com.dili.card.type.TradeChannel;
import com.dili.card.type.TradeType;
import com.dili.rule.sdk.rpc.ChargeRuleRpc;
import com.dili.ss.domain.BaseOutput;
import com.esotericsoftware.minlog.Log;
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
	@Resource
	private ChargeRuleRpc chargeRuleRpc;
	@Resource
	BusinessChargeItemRpc businessChargeItemRpc;
	@Resource
	private PayRpc payRpc;
	@Resource
	IRuleFeeService ruleFeeService;

	@Override
	public String getOpenCostFee() {
		BigDecimal ruleFee = ruleFeeService.getRuleFee(RuleFeeBusinessType.CARD_OPEN_CARD,
				SystemSubjectType.CARD_OPEN_COST);
		return ruleFee.doubleValue() + "";
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public OpenCardResponseDto openMasterCard(OpenCardDto openCardInfo) {
		// 获取当前账务周期
		AccountCycleDo cycleDo = accountCycleService.findActiveCycleByUserId(openCardInfo.getCreatorId(),
				openCardInfo.getCreator(), openCardInfo.getCreatorCode());

		// 更新现金柜金额
		accountCycleService.increaseCashBox(cycleDo.getCycleNo(), openCardInfo.getCostFee());

		// 调用账户服务开卡
		BaseOutput<OpenCardResponseDto> baseOutPut = openCardRpc.openMasterCard(openCardInfo);
		OpenCardResponseDto openCardResponse = GenericRpcResolver.resolver(baseOutPut, "账户服务开卡");
		Long accountId = openCardResponse.getAccountId();

		// 保存卡务柜台开卡操作记录 状态处理中
		BusinessRecordDo buildBusinessRecordDo = buildBusinessRecordDo(openCardInfo, accountId, cycleDo.getCycleNo());
		serialService.saveBusinessRecord(buildBusinessRecordDo);

		// 调用支付系统向市场账户充值工本费
		rechargeCostFee(accountId, openCardResponse.getFundAccountId(), openCardInfo);
		// 保存全局操作交易记录 开卡工本费
		saveSerialRecord(openCardInfo, accountId);
		return openCardResponse;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public OpenCardResponseDto openSlaveCard(OpenCardDto openCardInfo) {
		BaseOutput<OpenCardResponseDto> baseOutPut = openCardRpc.openSlaveCard(openCardInfo);
		OpenCardResponseDto openCardResponse = GenericRpcResolver.resolver(baseOutPut, "账户服务开卡");
		AccountCycleDo cycleDo = accountCycleService.findActiveCycleByUserId(openCardInfo.getCreatorId(),
				openCardInfo.getCreator(), openCardInfo.getCreatorCode());
		// 保存卡务柜台操作记录
		BusinessRecordDo buildBusinessRecordDo = buildBusinessRecordDo(openCardInfo, openCardResponse.getAccountId(),
				cycleDo.getCycleNo());
		serialService.saveBusinessRecord(buildBusinessRecordDo);
		// 保存全局操作交易记录 开卡工本费
		saveSerialRecord(openCardInfo, openCardResponse.getAccountId());

		return openCardResponse;
	}

	/**
	 * 构建柜员操作日志
	 * 
	 * @param openCardInfo
	 * @param accountId
	 * @return
	 */
	private BusinessRecordDo buildBusinessRecordDo(OpenCardDto openCardInfo, Long accountId, Long cycleNo) {
		BusinessRecordDo serial = new BusinessRecordDo();
		serial.setAccountId(accountId);
		serial.setCardNo(openCardInfo.getCardNo());
		serial.setCustomerId(openCardInfo.getCustomerId());
		serial.setCustomerName(openCardInfo.getName());
		serial.setCustomerNo(openCardInfo.getCustomerCode());
		serial.setCycleNo(cycleNo);
		serial.setFirmId(openCardInfo.getFirmId());
		serial.setOperatorId(openCardInfo.getCreatorId());
		serial.setOperatorName(openCardInfo.getCreator());
		serial.setOperatorNo(openCardInfo.getCreatorCode());
		serial.setOperateTime(LocalDateTime.now());
		serial.setState(OperateState.PROCESSING.getCode());
		serial.setNotes("IC卡工本费，现金" + openCardInfo.getCostFee() + "元");
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
	private void saveSerialRecord(OpenCardDto openCardInfo, Long accountId) {
		SerialRecordDo record = new SerialRecordDo();
		record.setAccountId(accountId);
		record.setCardNo(openCardInfo.getCardNo());
		record.setCustomerId(openCardInfo.getCustomerId());
		record.setCustomerName(openCardInfo.getName());
		record.setCustomerNo(openCardInfo.getCustomerCode());
		record.setFirmId(openCardInfo.getFirmId());
		record.setSerialNo(uidRpcResovler.bizNumber(BizNoType.OPERATE_SERIAL_NO.getCode()));
		record.setNotes("开卡工本费转为市场收入");
		record.setOperatorId(openCardInfo.getCreatorId());
		record.setOperatorName(openCardInfo.getCreator());
		record.setOperatorNo(openCardInfo.getCreatorCode());
		record.setTradeType(OperateType.ACCOUNT_TRANSACT.getCode());
		record.setType(OperateType.ACCOUNT_TRANSACT.getCode());
		record.setFundItem(FundItem.IC_CARD_COST.getCode());
		record.setOperateTime(LocalDateTime.now());

		SerialDto serialDto = new SerialDto();
		serialDto.setSerialRecordList(Lists.newArrayList(record));
		serialService.saveSerialRecord(serialDto);
	}

	/**
	 * 调用支付系统向市场账户充值工本费
	 */
	private void rechargeCostFee(Long accountId, Long fundAccountId, OpenCardDto openCardInfo) {
		try {
			CreateTradeRequestDto createDto = new CreateTradeRequestDto();
			createDto.setAccountId(fundAccountId);
			createDto.setAmount(openCardInfo.getCostFee());
			createDto.setBusinessId(accountId);
			createDto.setDescription("开卡充值工本费");
			createDto.setType(TradeType.FEE.getCode());
			CreateTradeResponseDto createTradeResp = GenericRpcResolver.resolver(payRpc.preparePay(createDto),
					PayRpc.SERVICE_NAME);
			TradeRequestDto commitDto = new TradeRequestDto();
			commitDto.setTradeId(createTradeResp.getTradeId());
			commitDto.setAccountId(fundAccountId);
			commitDto.setBusinessId(accountId);
			commitDto.setChannelId(TradeChannel.CASH.getCode());
			commitDto.setPassword(openCardInfo.getLoginPwd());
			GenericRpcResolver.resolver(payRpc.commitTrade(commitDto), PayRpc.SERVICE_NAME);

			// TODO 超时异常重试 支付最好区分重复提交的异常 其它异常保存日志及记录人工或定时任务补偿
		} catch (Exception e) {
			Log.error("向市场账户充值开卡工本费出现异常！", e);
		}
	}
}

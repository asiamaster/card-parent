package com.dili.card.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.assets.sdk.rpc.BusinessChargeItemRpc;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.OpenCardDto;
import com.dili.card.dto.OpenCardResponseDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.CreateTradeResponseDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.entity.AccountCycleDo;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.CardManageRpc;
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
import com.dili.card.type.CardType;
import com.dili.card.type.CustomerState;
import com.dili.card.type.FundItem;
import com.dili.card.type.OperateState;
import com.dili.card.type.OperateType;
import com.dili.card.type.RuleFeeBusinessType;
import com.dili.card.type.SystemSubjectType;
import com.dili.card.type.TradeChannel;
import com.dili.card.type.TradeType;
import com.dili.card.util.CurrencyUtils;
import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.rule.sdk.rpc.ChargeRuleRpc;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.google.common.collect.Lists;

import io.seata.spring.annotation.GlobalTransactional;

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
	@Resource
	private CustomerRpc customerRpc;
	@Resource
	private CardManageRpc cardManageRpc;

	@Override
	public Long getOpenCostFee() {
		BigDecimal ruleFee = ruleFeeService.getRuleFee(RuleFeeBusinessType.CARD_OPEN_CARD,
				SystemSubjectType.CARD_OPEN_COST);
		return CurrencyUtils.yuan2Cent(ruleFee);
	}

	@Override
	@GlobalTransactional(rollbackFor = Exception.class)
	@Transactional(rollbackFor = Exception.class)
	public OpenCardResponseDto openCard(OpenCardDto openCardInfo) {
		// 校验父账号登录密码 TODO:等待客户端更新
		if(CardType.isSlave(openCardInfo.getCardType())) {
			CardRequestDto checkPwdParam = new CardRequestDto();
			checkPwdParam.setAccountId(openCardInfo.getParentAccountId());
			checkPwdParam.setLoginPwd(openCardInfo.getParentLoginPwd());
			GenericRpcResolver.resolver(cardManageRpc.checkPassword(checkPwdParam), ServiceName.ACCOUNT);
		}
		// 校验客户信息
		Customer customer = GenericRpcResolver.resolver(
				customerRpc.get(openCardInfo.getCustomerId(), openCardInfo.getFirmId()), ServiceName.CUSTOMER);
		if (!customer.getState().equals(CustomerState.VALID.getCode())) {
			throw new CardAppBizException(ResultCode.PARAMS_ERROR,
					"客户已" + CustomerState.getStateName(customer.getState()));
		}
		// 获取当前账务周期
		AccountCycleDo cycleDo = accountCycleService.findActiveCycleByUserId(openCardInfo.getCreatorId(),
				openCardInfo.getCreator(), openCardInfo.getCreatorCode());

		// 工本费更新现金柜金额
		accountCycleService.increaseCashBox(cycleDo.getCycleNo(), openCardInfo.getCostFee());

		// 调用账户服务开卡
		BaseOutput<OpenCardResponseDto> baseOutPut = openCardRpc.openCard(openCardInfo);
		OpenCardResponseDto openCardResponse = GenericRpcResolver.resolver(baseOutPut, ServiceName.ACCOUNT);
		Long accountId = openCardResponse.getAccountId();

		// 调用支付系统向市场账户充值工本费
		String tradeId = "";
		if (openCardInfo.getCostFee() > 0) {
			tradeId = rechargeCostFee(accountId, openCardResponse.getFundAccountId(), openCardInfo);
		}

		// 保存卡务柜台开卡操作记录 使用seate后状态默认为成功
		BusinessRecordDo busiRecord = buildBusinessRecordDo(openCardInfo, accountId, cycleDo.getCycleNo(), tradeId);
		serialService.saveBusinessRecord(busiRecord);

		// 保存全局操作交易记录 开卡工本费
		saveSerialRecord(openCardInfo, accountId);
		return openCardResponse;
	}

	/**
	 * 构建柜员操作日志
	 */
	private BusinessRecordDo buildBusinessRecordDo(OpenCardDto openCardInfo, Long accountId, Long cycleNo,
			String tradeId) {
		BusinessRecordDo serial = new BusinessRecordDo();
		serial.setAccountId(accountId);
		serial.setCardNo(openCardInfo.getCardNo());
		serial.setCustomerId(openCardInfo.getCustomerId());
		serial.setCustomerName(openCardInfo.getCustomerName());
		serial.setCustomerNo(openCardInfo.getCustomerCode());
		serial.setCycleNo(cycleNo);
		serial.setFirmId(openCardInfo.getFirmId());
		serial.setOperatorId(openCardInfo.getCreatorId());
		serial.setOperatorName(openCardInfo.getCreator());
		serial.setOperatorNo(openCardInfo.getCreatorCode());
		serial.setOperateTime(LocalDateTime.now());
		serial.setAmount(openCardInfo.getCostFee());
		serial.setState(OperateState.SUCCESS.getCode());
		serial.setNotes("开卡，工本费转为市场收入");
		serial.setSerialNo(uidRpcResovler.bizNumber(BizNoType.OPERATE_SERIAL_NO.getCode()));
		serial.setTradeType(TradeType.FEE.getCode());
		serial.setTradeNo(tradeId);
		serial.setType(OperateType.ACCOUNT_TRANSACT.getCode());
		return serial;
	}

	/**
	 * 生成全局日志
	 */
	private void saveSerialRecord(OpenCardDto openCardInfo, Long accountId) {
		SerialRecordDo record = new SerialRecordDo();
		record.setAccountId(accountId);
		record.setCardNo(openCardInfo.getCardNo());
		record.setCustomerId(openCardInfo.getCustomerId());
		record.setCustomerName(openCardInfo.getCustomerName());
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
		record.setFundItemName(FundItem.IC_CARD_COST.getName());
		record.setStartBalance(0L);
		record.setAmount(openCardInfo.getCostFee());
		record.setEndBalance(0L);
		record.setOperateTime(LocalDateTime.now());

		SerialDto serialDto = new SerialDto();
		serialDto.setSerialRecordList(Lists.newArrayList(record));
		serialService.saveSerialRecord(serialDto);
	}

	/**
	 * 调用支付系统向市场账户充值工本费
	 */
	private String rechargeCostFee(Long accountId, Long fundAccountId, OpenCardDto openCardInfo) {
		CreateTradeRequestDto createDto = new CreateTradeRequestDto();
		createDto.setAccountId(fundAccountId);
		createDto.setAmount(openCardInfo.getCostFee());
		createDto.setBusinessId(accountId);
		createDto.setDescription("开卡充值工本费");
		createDto.setType(TradeType.FEE.getCode());
		CreateTradeResponseDto createTradeResp = GenericRpcResolver.resolver(payRpc.preparePay(createDto),
				ServiceName.PAY);
		TradeRequestDto commitDto = new TradeRequestDto();
		commitDto.setTradeId(createTradeResp.getTradeId());
		commitDto.setAccountId(fundAccountId);
		commitDto.setBusinessId(accountId);
		commitDto.setChannelId(TradeChannel.CASH.getCode());
		commitDto.setPassword(openCardInfo.getLoginPwd());
		commitDto.addServiceFeeItem(openCardInfo.getCostFee(), FundItem.IC_CARD_COST);
		GenericRpcResolver.resolver(payRpc.commitTrade(commitDto), ServiceName.PAY);
		return createTradeResp.getTradeId();
	}
}

package com.dili.card.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.dili.assets.sdk.rpc.BusinessChargeItemRpc;
import com.dili.card.common.constant.Constant;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.config.OpenCardMQConfig;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.CustomerResponseDto;
import com.dili.card.dto.FundAccountDto;
import com.dili.card.dto.OpenCardDto;
import com.dili.card.dto.OpenCardMqDto;
import com.dili.card.dto.OpenCardResponseDto;
import com.dili.card.dto.PayCreateFundReponseDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardQuery;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.CreateTradeResponseDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.entity.AccountCycleDo;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.exception.ErrorCode;
import com.dili.card.rpc.CardManageRpc;
import com.dili.card.rpc.OpenCardRpc;
import com.dili.card.rpc.PayRpc;
import com.dili.card.rpc.SerialRecordRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.ICardStorageService;
import com.dili.card.service.ICustomerService;
import com.dili.card.service.IOpenCardService;
import com.dili.card.service.IRuleFeeService;
import com.dili.card.service.ISerialService;
import com.dili.card.service.ITypeMarketService;
import com.dili.card.type.BizNoType;
import com.dili.card.type.CardStatus;
import com.dili.card.type.CardType;
import com.dili.card.type.CustomerOrgType;
import com.dili.card.type.CustomerState;
import com.dili.card.type.FundItem;
import com.dili.card.type.OperateState;
import com.dili.card.type.OperateType;
import com.dili.card.type.RuleFeeBusinessType;
import com.dili.card.type.SystemSubjectType;
import com.dili.card.type.TradeChannel;
import com.dili.card.type.TradeType;
import com.dili.card.util.AssertUtils;
import com.dili.card.util.CurrencyUtils;
import com.dili.commons.rabbitmq.RabbitMQMessageService;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.rule.sdk.rpc.ChargeRuleRpc;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;
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

	private static final Logger log = LoggerFactory.getLogger(OpenCardServiceImpl.class);

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
	@Resource
	private RabbitMQMessageService rabbitMQMessageService;
	@Resource
	IAccountQueryService accountQueryService;
	@Resource
	ICardStorageService cardStorageService;
	@Autowired
	private DataDictionaryRpc dataDictionaryRpc;
	@Autowired
	private ICustomerService customerService;
	@Autowired
	private ITypeMarketService typeMarketService;

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
		if(openCardInfo.getCostFee() > 99900) {
			throw new CardAppBizException("工本费超过最大值999元，无法开卡");
		}
		// 二次校验新卡状态
		cardStorageService.checkAndGetByCardNo(openCardInfo.getCardNo(), openCardInfo.getCardType(),
				openCardInfo.getCustomerId());

		// 校验父账号登录密码
		if (CardType.isSlave(openCardInfo.getCardType())) {
			CardRequestDto checkPwdParam = new CardRequestDto();
			checkPwdParam.setAccountId(openCardInfo.getParentAccountId());
			checkPwdParam.setLoginPwd(openCardInfo.getParentLoginPwd());
			GenericRpcResolver.resolver(cardManageRpc.checkPassword(checkPwdParam), ServiceName.ACCOUNT);
		}
		// 校验客户信息
		CustomerExtendDto customer = GenericRpcResolver.resolver(
				customerRpc.get(openCardInfo.getCustomerId(), openCardInfo.getFirmId()), ServiceName.CUSTOMER);
		if (!customer.getCustomerMarket().getState().equals(CustomerState.VALID.getCode())) {
			throw new CardAppBizException(ResultCode.PARAMS_ERROR,
					"客户" + CustomerState.getStateName(customer.getCustomerMarket().getState()));
		}
		openCardInfo.setCustomerCharacterType(customerService.convertCharacterTypes(customer.getCharacterTypeList(), customer.getId()));
		openCardInfo.setCustomerSubTypes(String.join(",", customerService.getSubTypes(customer.getCharacterTypeList())));
		// 获取当前账务周期
		AccountCycleDo cycleDo = accountCycleService.findActiveCycleByUserId(openCardInfo.getCreatorId(),
				openCardInfo.getCreator(), openCardInfo.getCreatorCode());

		// 工本费更新现金柜金额
		accountCycleService.increaseCashBox(cycleDo.getCycleNo(), openCardInfo.getCostFee());

		// 创建资金账户
		if (CardType.isSlave(openCardInfo.getCardType())) {
			UserAccountCardResponseDto parentAccount = accountQueryService
					.getByAccountId(openCardInfo.getParentAccountId());
			openCardInfo.setParentFundAccountId(parentAccount.getFundAccountId());
		} else {
			openCardInfo.setParentFundAccountId(null);
		}
		FundAccountDto fundAccount = buildFundAccount(openCardInfo);
		PayCreateFundReponseDto payAccount = GenericRpcResolver.resolver(payRpc.createFundAccount(fundAccount),
				ServiceName.PAY);
		openCardInfo.setFundAccountId(payAccount.getAccountId());

		// 调用账户服务开卡
		BaseOutput<OpenCardResponseDto> baseOutPut = openCardRpc.openCard(openCardInfo);
		OpenCardResponseDto openCardResponse = GenericRpcResolver.resolver(baseOutPut, ServiceName.ACCOUNT);
		Long accountId = openCardResponse.getAccountId();

		String serialNo = uidRpcResovler.bizNumber(BizNoType.OPERATE_SERIAL_NO.getCode());
		// 调用支付系统向市场账户充值工本费
		TradeResponseDto tradeResponseDto = new TradeResponseDto();
		if (openCardInfo.getCostFee() > 0) {
			tradeResponseDto = rechargeCostFee(accountId, openCardResponse.getFundAccountId(), openCardInfo, serialNo);
			log.info("充值工本费支付返回:" + JSONObject.toJSONString(tradeResponseDto));
		}
		if (CardType.isSlave(openCardInfo.getCardType())) {
			// 单独查询账户余额
			CreateTradeRequestDto requestDto = new CreateTradeRequestDto();
			requestDto.setAccountId(openCardResponse.getFundAccountId());
			BalanceResponseDto resolver = GenericRpcResolver.resolver(payRpc.getAccountBalance(requestDto),
					ServiceName.PAY);
			tradeResponseDto.setBalance(resolver.getAvailableAmount());
			tradeResponseDto.setFrozenBalance(0L);
		} else {
			tradeResponseDto.setBalance(0L);
			tradeResponseDto.setFrozenBalance(0L);
		}

		// 保存卡务柜台开卡操作记录 使用seate后状态默认为成功,开卡期初期末默认为0
		BusinessRecordDo busiRecord = buildBusinessRecordDo(openCardInfo, accountId, cycleDo.getCycleNo(),
				tradeResponseDto, serialNo);
		serialService.saveBusinessRecord(busiRecord);
		openCardResponse.setSerialNo(busiRecord.getSerialNo());

		// 保存全局操作交易记录 开卡工本费
		saveSerialRecord(openCardInfo, accountId);

		// 发送MQ通知
		sendMQ(openCardInfo);
		return openCardResponse;
	}

	/**
	 * 发送MQ
	 *
	 * @param openCardInfo
	 */
	private void sendMQ(OpenCardDto openCardInfo) {
		OpenCardMqDto mqDto = new OpenCardMqDto();
		mqDto.setCustomerId(openCardInfo.getCustomerId());
		mqDto.setCustomerCode(openCardInfo.getCustomerCode());
		mqDto.setCustomerName(openCardInfo.getCustomerName());
		mqDto.setCardNo(openCardInfo.getCardNo());
		mqDto.setFirmId(openCardInfo.getFirmId());
		mqDto.setHoldName(openCardInfo.getHoldName());
		mqDto.setCreator(openCardInfo.getCreator());
		mqDto.setCreatorId(openCardInfo.getCreatorId());
		log.info("开卡MQ通知>>>>>EXCHANGE[{}]ROUTING[{}]{}", OpenCardMQConfig.EXCHANGE, OpenCardMQConfig.ROUTING,
				JSONObject.toJSONString(mqDto));
		rabbitMQMessageService.send(OpenCardMQConfig.EXCHANGE, OpenCardMQConfig.ROUTING,
				JSONObject.toJSONString(mqDto));
	}

	/**
	 * 构建柜员操作日志
	 */
	private BusinessRecordDo buildBusinessRecordDo(OpenCardDto openCardInfo, Long accountId, Long cycleNo,
			TradeResponseDto tradeResponseDto, String serialNo) {
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
		serial.setStartBalance(tradeResponseDto.getBalance() - tradeResponseDto.getFrozenBalance());
		serial.setAmount(openCardInfo.getCostFee());
		serial.setEndBalance(tradeResponseDto.getBalance() - tradeResponseDto.getFrozenBalance());
		serial.setCardCost(openCardInfo.getCostFee());
		serial.setState(OperateState.SUCCESS.getCode());
		serial.setNotes("开卡，工本费转为市场收入");
		serial.setSerialNo(serialNo);
		serial.setTradeType(TradeType.FEE.getCode());
		serial.setTradeNo(tradeResponseDto.getTradeId());
		serial.setType(OperateType.ACCOUNT_TRANSACT.getCode());
		serial.setTradeChannel(TradeChannel.CASH.getCode());
		serial.setHoldName(openCardInfo.getHoldName());
		Map<String, Object> attach = new HashMap<String, Object>();
		attach.put(Constant.BUSINESS_RECORD_ATTACH_CARDTYPE, openCardInfo.getCardType());
		serial.setAttach(JSONObject.toJSONString(attach));
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
		record.setCustomerType(openCardInfo.getCustomerSubTypes());
		record.setFirmId(openCardInfo.getFirmId());
		record.setSerialNo(uidRpcResovler.bizNumber(BizNoType.OPERATE_SERIAL_NO.getCode()));
		record.setNotes("开卡，工本费转为市场收入");
		record.setOperatorId(openCardInfo.getCreatorId());
		record.setOperatorName(openCardInfo.getCreator());
		record.setOperatorNo(openCardInfo.getCreatorCode());
		record.setTradeType(OperateType.ACCOUNT_TRANSACT.getCode());
		record.setType(OperateType.ACCOUNT_TRANSACT.getCode());
		record.setFundItem(FundItem.IC_CARD_COST.getCode());
		record.setFundItemName(FundItem.IC_CARD_COST.getName());
		record.setAmount(openCardInfo.getCostFee());
		record.setTradeChannel(TradeChannel.CASH.getCode());
		record.setOperateTime(LocalDateTime.now());
		record.setHoldName(openCardInfo.getHoldName());
		record.setHoldContactsPhone(openCardInfo.getHoldContactsPhone());
		record.setHoldCertificateNumber(openCardInfo.getHoldCertificateNumber());
		SerialDto serialDto = new SerialDto();
		serialDto.setSerialRecordList(Lists.newArrayList(record));
		serialService.saveSerialRecord(serialDto);
	}

	/**
	 * 构建资金账户数据
	 *
	 * @param openCardInfo
	 * @param accountId     业务主键
	 * @param fundAccountId 资金账号ID
	 * @return
	 */
	private FundAccountDto buildFundAccount(OpenCardDto openCardInfo) {
		FundAccountDto fundAccount = new FundAccountDto();
		fundAccount.setCustomerId(openCardInfo.getCustomerId());
		fundAccount.setType(CustomerOrgType.getPayCode(openCardInfo.getCustomerOrganizationType()));
		fundAccount.setType(1);
		fundAccount.setUseFor(1); // TODO 寿光只有一个交易账户，其它市场将允许多账户
		fundAccount.setName(openCardInfo.getCustomerName());
		fundAccount.setMobile(openCardInfo.getCustomerContactsPhone());
		fundAccount.setCode(openCardInfo.getCardNo());
		fundAccount.setPassword(openCardInfo.getLoginPwd());
		fundAccount.setParentId(openCardInfo.getParentFundAccountId());
		return fundAccount;
	}

	/**
	 * 调用支付系统向市场账户充值工本费
	 */
	private TradeResponseDto rechargeCostFee(Long accountId, Long fundAccountId, OpenCardDto openCardInfo,
			String serialNo) {
		CreateTradeRequestDto createDto = new CreateTradeRequestDto();
		createDto.setAccountId(fundAccountId);
		createDto.setAmount(openCardInfo.getCostFee());
		createDto.setBusinessId(accountId);
		createDto.setDescription("开卡充值工本费");
		createDto.setSerialNo(Constant.PAY_SERIAL_NO_PREFIX + serialNo);
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
		// 目前充值工本费支付返回为空
		GenericRpcResolver.resolver(payRpc.commitTrade(commitDto), ServiceName.PAY);
		TradeResponseDto tradeResponseDto = new TradeResponseDto();
		tradeResponseDto.setTradeId(createTradeResp.getTradeId());
		return tradeResponseDto;
	}

	@Override
	public CustomerResponseDto getCustomerInfoByCertificateNumber(String certificateNumber, Long firmId) {
		AssertUtils.notEmpty(certificateNumber, "请输入证件号!");
		CustomerExtendDto customer = GenericRpcResolver
				.resolver(customerRpc.getByCertificateNumber(certificateNumber, firmId), ServiceName.CUSTOMER);
		CustomerResponseDto response = new CustomerResponseDto();
		if (customer != null) {
			// 判断客户是否已禁用或注销
			if (!customer.getCustomerMarket().getState().equals(CustomerState.VALID.getCode())) {
				throw new CardAppBizException(ResultCode.PARAMS_ERROR,
						"客户" + CustomerState.getStateName(customer.getCustomerMarket().getState()));
			}
			BeanUtils.copyProperties(customer, response);
			response.setCustomerContactsPhone(customer.getContactsPhone());
			response.setCustomerTypeName(customerService.getSubTypeName(customer.getCharacterTypeList(), firmId));
			response.setCustomerType(String.join(",", customerService.getSubTypes(customer.getCharacterTypeList())));
			response.setId(customer.getId());
			response.setFirmId(firmId);
			log.info("开卡客户信息>{}",JSONObject.toJSON(response));
		} else {
			throw new CardAppBizException(ErrorCode.CUSTOMER_NOT_EXIST, "未找到客户信息!");
		}
		return response;
	}

//	/**
//	 * 从数据字典中获取客户身份类型
//	 */
//	private String getCustomerTypeName(String code, Long firmId) {
//		DataDictionaryValue ddv = DTOUtils.newInstance(DataDictionaryValue.class);
//		ddv.setDdCode(Constant.CUS_CUSTOMER_TYPE);
//		ddv.setCode(code);
//		ddv.setFirmId(firmId);
//		List<DataDictionaryValue> resolver = GenericRpcResolver.resolver(dataDictionaryRpc.listDataDictionaryValue(ddv),
//				"DataDictionaryRpc");
//		if (CollectionUtils.isEmpty(resolver)) {
//			throw new CardAppBizException("数据字典中没找到该客户类型" + code + "，是否已经删除!");
//		}
//
//		return resolver.get(0).getName();
//	}

	@Override
	public Integer checkCardNum(CustomerResponseDto customerResponseDto) {
		// 判断客户是否已办理过主卡，寿光每个人只能有一张交易主卡,其它市场允许办两张卡，则判断只能一张交易买家卡和一张交易卖家卡
		UserAccountCardQuery queryParam = new UserAccountCardQuery();
		queryParam.setCustomerIds(Lists.newArrayList(customerResponseDto.getId()));
		queryParam.setExcludeUnusualState(0); // 不排除任何状态
		queryParam.setFirmId(customerResponseDto.getFirmId());
		List<UserAccountCardResponseDto> accountList = accountQueryService.getList(queryParam);
		int nowCardNum = 0;
		String customerName = "";
		if (accountList != null && accountList.size() > 0) {
			// 判断已办主卡数量
			for (UserAccountCardResponseDto accountDto : accountList) {
				if (CardType.isMaster(accountDto.getCardType())
						&& accountDto.getCardState() != CardStatus.RETURNED.getCode()) {
					customerName = accountDto.getCustomerName();
					++nowCardNum;
				}
			}
			if (nowCardNum == 0) {
				return nowCardNum;
			}
			DataDictionaryValue ddv = DTOUtils.newInstance(DataDictionaryValue.class);
			ddv.setDdCode("max_card_num");
			ddv.setFirmId(customerResponseDto.getFirmId());
			List<DataDictionaryValue> resolver = GenericRpcResolver
					.resolver(dataDictionaryRpc.listDataDictionaryValue(ddv), "DataDictionaryRpc");
			log.info("{}开卡数量配置>{}", customerResponseDto.getFirmId(), JSONObject.toJSONString(resolver));
			Integer maxCardNum = 1;

			if (CollectionUtils.isEmpty(resolver)) {
				log.warn("FirmId{}在数据字典中没有配置开卡数量,使用默认值{}", customerResponseDto.getFirmId(), maxCardNum);
			}

			if (resolver != null && resolver.size() > 0) {
				maxCardNum = Integer.parseInt(resolver.get(0).getCode());
				log.warn("FirmId{}在数据字典中配置了多个开卡数量,以第一行数据作为判断依据", customerResponseDto.getFirmId());
			}

			if (nowCardNum >= maxCardNum) {
				String msg = String.format("客户%s已办理过%s张主卡,不能再办理新卡", customerName, nowCardNum);
				throw new CardAppBizException(msg);
			}
		}
		return nowCardNum;
	}

}

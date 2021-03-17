package com.dili.card.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.dto.*;
import com.dili.card.dto.pay.AccountAllPermission;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.entity.AccountCycleDo;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.AccountManageRpcResolver;
import com.dili.card.rpc.resolver.AccountQueryRpcResolver;
import com.dili.card.rpc.resolver.CustomerRpcResolver;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.IAccountManageService;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.*;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.logger.sdk.util.LoggerUtil;
import com.dili.ss.constant.ResultCode;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Autowired
    private CustomerRpcResolver customerRpcResolver;
    @Resource
    private IAccountCycleService accountCycleService;

	@Override
	@GlobalTransactional(rollbackFor = Exception.class)
	@Transactional(rollbackFor = Exception.class)
	public void frozen(CardRequestDto cardRequestDto) {
		LOGGER.info("冻结账户：" + cardRequestDto.getAccountId());
		//校验账户信息
		UserAccountCardResponseDto accountCard = this.validateCardAccount(cardRequestDto.getAccountId(), false, DisableState.ENABLED);

		//保存本地记录
		BusinessRecordDo businessRecord = this.saveLocalSerialRecord(cardRequestDto, accountCard, OperateType.FROZEN_ACCOUNT.getCode());

		//远程冻结卡账户操作
    	accountManageRpcResolver.frozen(cardRequestDto);

    	//挂失不处理资金账户
    	if (!Integer.valueOf(CardStatus.LOSS.getCode()).equals(accountCard.getCardState())) {

    		//远程解冻资金账户 必須是主副卡
    		payRpcResolver.freezeFundAccount(CreateTradeRequestDto.createCommon(accountCard.getFundAccountId(), accountCard.getAccountId()));

    		//更新最終记录
        	this.saveRemoteSerialRecord(businessRecord, FundItem.MANDATORY_FREEZE_ACCOUNT);
    	}
	}

	@Override
	@GlobalTransactional(rollbackFor = Exception.class)
	@Transactional(rollbackFor = Exception.class)
	public void unfrozen(CardRequestDto cardRequestDto) {
		LOGGER.info("解冻账户：" + cardRequestDto.getAccountId());

		//校验账户信息
		UserAccountCardResponseDto accountCard = this.validateCardAccount(cardRequestDto.getAccountId(), false, DisableState.DISABLED);

		CustomerExtendDto customer = customerRpcResolver.getWithNotNull(accountCard.getCustomerId(), accountCard.getFirmId());
		if (customer.getCustomerMarket().getState().equals(CustomerState.DISABLED.getCode())){
			throw new CardAppBizException("客户已被禁用");
		}
		//保存本地记录
		BusinessRecordDo businessRecord = this.saveLocalSerialRecord(cardRequestDto, accountCard, OperateType.UNFROZEN_ACCOUNT.getCode());

		//远程解冻账户操作
		accountManageRpcResolver.unfrozen(cardRequestDto);

		//挂失不处理资金账户
    	if (!Integer.valueOf(CardStatus.LOSS.getCode()).equals(accountCard.getCardState())) {

    		//远程解冻资金账户 必須是主副卡
    		payRpcResolver.unfreezeFundAccount(CreateTradeRequestDto.createCommon(accountCard.getFundAccountId(), accountCard.getAccountId()));

    		//更新最終记录
        	this.saveRemoteSerialRecord(businessRecord, FundItem.MANDATORY_UNFREEZE_ACCOUNT);
		}
	}

	@Override
	public Optional<String> saveCardPermission(AccountPermissionRequestDto requestDto) {
		UserAccountCardResponseDto cardInfo = accountQueryService.getByAccountId(requestDto.getAccountId());
		if (Objects.isNull(cardInfo)) {
			return Optional.of("卡账户数据不存在");
		}
		if (CardType.MASTER.getCode() != cardInfo.getCardType().intValue()) {
			return Optional.of("此功能只对主卡账户开放");
		}
		if (!DisableState.ENABLED.getCode().equals(cardInfo.getDisabledState())) {
			return Optional.of("卡账户已冻结，不支持办理此业务");
		}
		JSONObject params = new JSONObject();
		params.put("accountId", cardInfo.getFundAccountId());
		Set<Integer> permissionSets = new HashSet<>();
		String permissionValue = "空权限";
		if (CollectionUtil.isNotEmpty(requestDto.getPermission())) {
			permissionSets.addAll(requestDto.getPermission().stream().map(AccountAllPermission::getCode).collect(Collectors.toSet()));
			permissionValue = requestDto.getPermission().stream().map(AccountAllPermission::getName).collect(Collectors.joining(","));
		}
		params.put("permission", permissionSets);
		params.put("trade", requestDto.getTrade());
		params.put("withdraw", requestDto.getWithdraw());
		payRpcResolver.setPermission(params);
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		String content = String.format("%s 设置 客户[%s] 卡[%s]账户权限为 %s", userTicket.getRealName(), cardInfo.getCustomerName(), cardInfo.getCardNo(), permissionValue);
		LoggerUtil.buildBusinessLoggerContext(cardInfo.getAccountId(), cardInfo.getCardNo(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), content, "", LogOperationType.EDIT.getCode(), String.valueOf(OperateType.PERMISSION_SET.getCode()));
		return Optional.empty();
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
		UserAccountCardResponseDto accountCard = accountQueryRpcResolver.findSingleWithoutValidate(query);
		if (checkCardLoss && Integer.valueOf(CardStatus.RETURNED.getCode()).equals(accountCard.getCardState())) {
            throw new CardAppBizException("", String.format("该卡为%s状态,不能进行操作", CardStatus.getName(accountCard.getCardState())));
        }
		if (checkCardLoss && Integer.valueOf(CardStatus.LOSS.getCode()).equals(accountCard.getCardState())) {
            throw new CardAppBizException("", String.format("该卡为%s状态,不能进行操作", CardStatus.getName(accountCard.getCardState())));
        }
		if (!Integer.valueOf(disableState.getCode()).equals(accountCard.getDisabledState())) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, String.format("该卡账户为%s状态,不能进行操作", DisableState.getName(accountCard.getDisabledState())));
        }
		return accountCard;
	}

	/**
     * 保存本地操作记录
     */
	private BusinessRecordDo saveLocalSerialRecord(CardRequestDto cardParam, UserAccountCardResponseDto accountCard, int operateType) {
		 //账务周期
        AccountCycleDo accountCycle = accountCycleService.findLatestCycleByUserId(cardParam.getOpId());
		BusinessRecordDo businessRecord = serialService.createBusinessRecord(cardParam, accountCard, temp -> {
            temp.setType(operateType);
        }, accountCycle == null? 0L:accountCycle.getCycleNo());
		
		serialService.saveBusinessRecord(businessRecord);
		return businessRecord;
	}

	 /**
     * 保存远程流水记录
     */
	private void saveRemoteSerialRecord(BusinessRecordDo businessRecord, FundItem fundItem) {
        //成功则修改状态及期初期末金额，存储操作流水
        SerialDto serialDto = serialService.createAccountSerial(businessRecord, (serialRecord, feeType) -> {

        	serialRecord.setFundItem(fundItem.getCode());
            serialRecord.setFundItemName(fundItem.getName());

		});
        serialService.handleSuccess(serialDto, true);
    }

}

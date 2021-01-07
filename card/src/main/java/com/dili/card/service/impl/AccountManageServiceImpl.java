package com.dili.card.service.impl;

import javax.annotation.Resource;

import com.dili.card.rpc.resolver.CustomerRpcResolver;
import com.dili.card.type.CustomerState;
import com.dili.customer.sdk.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.UserAccountSingleQueryDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.entity.AccountCycleDo;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.AccountManageRpcResolver;
import com.dili.card.rpc.resolver.AccountQueryRpcResolver;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.IAccountManageService;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.CardStatus;
import com.dili.card.type.DisableState;
import com.dili.card.type.FundItem;
import com.dili.card.type.OperateType;
import com.dili.ss.constant.ResultCode;

import io.seata.spring.annotation.GlobalTransactional;

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
		BusinessRecordDo businessRecord = this.saveLocalSerialRecord(cardRequestDto, accountCard, OperateType.FROZEN_ACCOUNT);

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

		Customer customer = customerRpcResolver.getWithNotNull(accountCard.getCustomerId(), accountCard.getFirmId());
		if (customer.getState().equals(CustomerState.DISABLED.getCode())){
			throw new CardAppBizException("客户已被禁用");
		}
		//保存本地记录
		BusinessRecordDo businessRecord = this.saveLocalSerialRecord(cardRequestDto, accountCard, OperateType.UNFROZEN_ACCOUNT);

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
	private BusinessRecordDo saveLocalSerialRecord(CardRequestDto cardParam, UserAccountCardResponseDto accountCard, OperateType operateType) {
		 //账务周期
        AccountCycleDo accountCycle = accountCycleService.findLatestCycleByUserId(cardParam.getOpId());
		BusinessRecordDo businessRecord = serialService.createBusinessRecord(cardParam, accountCard, temp -> {
            temp.setType(operateType.getCode());
        }, accountCycle.getCycleNo());
		
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

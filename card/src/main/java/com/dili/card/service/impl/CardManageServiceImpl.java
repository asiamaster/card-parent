package com.dili.card.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.Constant;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.UserAccountSingleQueryDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.CreateTradeResponseDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.CardManageRpc;
import com.dili.card.rpc.resolver.CardManageRpcResolver;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.ICardManageService;
import com.dili.card.service.ICardStorageService;
import com.dili.card.service.IReturnCardService;
import com.dili.card.service.IRuleFeeService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.FundItem;
import com.dili.card.type.OperateType;
import com.dili.card.type.RuleFeeBusinessType;
import com.dili.card.type.SystemSubjectType;
import com.dili.card.type.TradeChannel;
import com.dili.card.type.TradeType;
import com.dili.card.util.CurrencyUtils;
import com.dili.card.validator.AccountValidator;
import com.dili.customer.sdk.rpc.CustomerEmployeeRpc;
import com.dili.ss.domain.BaseOutput;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * @description： 卡片退卡换卡等操作service实现
 *
 * @author ：WangBo
 * @time ：2020年4月28日下午5:09:47
 */
@Service("cardManageService")
public class CardManageServiceImpl implements ICardManageService {

    @Autowired
    private CardManageRpcResolver cardManageRpcResolver;
    @Resource
    private CardManageRpc cardManageRpc;
    @Resource
    private ISerialService serialService;
    @Resource
    private PayRpcResolver payRpcResolver;
    @Resource
    protected IAccountQueryService accountQueryService;
    @Autowired
    private IAccountCycleService accountCycleService;
    @Autowired
    private IReturnCardService returnCardService;
    @Autowired
    private IRuleFeeService ruleFeeService;
    @Resource
    private ICardStorageService cardStorageService;
    @Autowired
    private CustomerEmployeeRpc customerEmployeeRpc;

    /**
     * @param cardParam
     */
    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public String unLostCard(CardRequestDto cardParam) {
        UserAccountSingleQueryDto query = new UserAccountSingleQueryDto();
        query.setCardNo(cardParam.getCardNo());
        UserAccountCardResponseDto accountCard = accountQueryService.getForUnLostCard(query);
        BusinessRecordDo businessRecordDo = serialService.createBusinessRecord(cardParam, accountCard, temp -> {
            temp.setType(OperateType.LOSS_REMOVE.getCode());
        });
        serialService.saveBusinessRecord(businessRecordDo);
        BaseOutput<?> baseOutput = cardManageRpc.unLostCard(cardParam);
        if (!baseOutput.isSuccess()) {
            throw new CardAppBizException(baseOutput.getCode(), baseOutput.getMessage());
        }
        //远程解冻资金账户 必須是主副卡
        payRpcResolver.unfreezeFundAccount(CreateTradeRequestDto.createCommon(accountCard.getFundAccountId(), accountCard.getAccountId()));
        this.saveRemoteSerialRecord(businessRecordDo);
        return businessRecordDo.getSerialNo();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public String returnCard(CardRequestDto cardParam) {
        return returnCardService.handle(cardParam);
    }

    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String resetLoginPwd(CardRequestDto cardParam) {

        //获取卡信息
        UserAccountSingleQueryDto queryDto = new UserAccountSingleQueryDto();
        queryDto.setAccountId(cardParam.getAccountId());
        UserAccountCardResponseDto accountCard = accountQueryService.getForResetLoginPassword(queryDto);

        //校验卡信息与客户信息
        AccountValidator.validateMatchAccount(cardParam, accountCard);

        //保存本地操作记录
        BusinessRecordDo businessRecordDo = saveLocalSerialRecordNoFundSerial(cardParam, accountCard, OperateType.RESET_PWD);

        //远程账户重置密码操作
        cardManageRpcResolver.resetLoginPwd(cardParam);

        //远程支付重置密码操作
        payRpcResolver.resetPwd(CreateTradeRequestDto.createPwd(accountCard.getFundAccountId(), cardParam.getLoginPwd()));

        //记录远程操作记录
        this.saveRemoteSerialRecord(businessRecordDo);
        return businessRecordDo.getSerialNo();
    }

    @Override
    public String modifyLoginPwd(CardRequestDto cardParam) {
        //获取卡信息
        UserAccountCardResponseDto accountCard = accountQueryService.getByAccountId(cardParam.getAccountId());

        //校验卡信息与客户信息
        AccountValidator.validateMatchAccount(cardParam, accountCard);

        //保存本地操作记录
        BusinessRecordDo businessRecordDo = saveLocalSerialRecordNoFundSerial(cardParam, accountCard, OperateType.PWD_CHANGE);

        //远程账户修改密码操作
        GenericRpcResolver.resolver(cardManageRpc.modifyLoginPwd(cardParam), ServiceName.ACCOUNT);

        //远程支付重置密码操作
        payRpcResolver.resetPwd(CreateTradeRequestDto.createPwd(accountCard.getFundAccountId(), cardParam.getLoginPwd()));

        //记录远程操作记录
        this.saveRemoteSerialRecord(businessRecordDo);
        return businessRecordDo.getSerialNo();
    }


    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String unLockCard(CardRequestDto cardParam) {
        UserAccountSingleQueryDto query = new UserAccountSingleQueryDto();
        query.setCardNo(cardParam.getCardNo());
        query.setAccountId(cardParam.getAccountId());
        UserAccountCardResponseDto accountCard = accountQueryService.getForUnLockCard(query);
        BusinessRecordDo businessRecordDo = serialService.createBusinessRecord(cardParam, accountCard, temp -> {
            temp.setType(OperateType.LIFT_LOCKED.getCode());
        });
        serialService.saveBusinessRecord(businessRecordDo);
        BaseOutput<?> baseOutput = cardManageRpc.unLockCard(cardParam);
        if (!baseOutput.isSuccess()) {
            throw new CardAppBizException(baseOutput.getCode(), baseOutput.getMessage());
        }
        this.saveRemoteSerialRecord(businessRecordDo);
        return businessRecordDo.getSerialNo();
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public String reportLossCard(CardRequestDto cardParam) {
        UserAccountCardResponseDto userAccount = accountQueryService.getByCardNo(cardParam.getCardNo());
        AccountValidator.validateMatchAccount(cardParam, userAccount);
        BusinessRecordDo businessRecord = serialService.createBusinessRecord(cardParam, userAccount,
                record -> record.setType(OperateType.LOSS_CARD.getCode()));
        serialService.saveBusinessRecord(businessRecord);

        cardManageRpcResolver.reportLossCard(cardParam);

        payRpcResolver.freezeFundAccount(
                CreateTradeRequestDto.createCommon(
                        userAccount.getFundAccountId(), userAccount.getAccountId()));

        this.saveRemoteSerialRecord(businessRecord);
        return businessRecord.getSerialNo();
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public String changeCard(CardRequestDto requestDto) {
        UserAccountCardResponseDto userAccount = accountQueryService.getByCardNo(requestDto.getCardNo());
        AccountValidator.validateMatchAccount(requestDto, userAccount);
        //this.validateCanChange(requestDto, userAccount);

        cardStorageService.checkAndGetByCardNo(requestDto.getNewCardNo(), userAccount.getCardType(), userAccount.getCustomerId());

        Long serviceFee = requestDto.getServiceFee();
        BusinessRecordDo businessRecord = serialService.createBusinessRecord(requestDto, userAccount, record -> {
            record.setType(OperateType.CHANGE.getCode());
            record.setAmount(serviceFee);
            record.setCardCost(serviceFee);
            record.setTradeType(TradeType.FEE.getCode());
            record.setTradeChannel(TradeChannel.CASH.getCode());
            //记录老卡卡号，用于生成打印数据
            JSONObject obj = new JSONObject();
            obj.put(Constant.NEW_CARD_NO_PARAM, requestDto.getNewCardNo());
            record.setAttach(obj.toJSONString());
            record.setNotes("换卡，工本费转为市场收入");
            record.setNewCardNo(requestDto.getNewCardNo());
        });

        CreateTradeRequestDto tradeRequest = CreateTradeRequestDto.createTrade(
                TradeType.FEE.getCode(),
                userAccount.getAccountId(),
                userAccount.getFundAccountId(),
                serviceFee,
                businessRecord.getSerialNo(),
                String.valueOf(businessRecord.getCycleNo()));
        //创建交易
        CreateTradeResponseDto tradeRespDto = payRpcResolver.prePay(tradeRequest);
        String tradeNo = tradeRespDto.getTradeId();

        businessRecord.setTradeNo(tradeNo);
        serialService.saveBusinessRecord(businessRecord);
        //添加现金柜资金
        accountCycleService.increaseCashBox(businessRecord.getCycleNo(), requestDto.getServiceFee());

        cardManageRpcResolver.changeCard(requestDto);

        TradeRequestDto tradeRequestDto = TradeRequestDto.createTrade(userAccount, tradeNo, TradeChannel.CASH.getCode(), requestDto.getLoginPwd());
        tradeRequestDto.addServiceFeeItem(serviceFee, FundItem.IC_CARD_COST);
        payRpcResolver.trade(tradeRequestDto);

        SerialDto serialDto = serialService.createAccountSerial(businessRecord, (serialRecord, feeType) -> {
            serialRecord.setTradeType(OperateType.CHANGE.getCode());
            serialRecord.setType(OperateType.CHANGE.getCode());
            serialRecord.setFundItem(FundItem.IC_CARD_COST.getCode());
            serialRecord.setFundItemName(FundItem.IC_CARD_COST.getName());
            serialRecord.setAmount(requestDto.getServiceFee());
            serialRecord.setNotes("换卡，工本费转为市场收入");
        });
        //这里需要查询一次余额，因为直接从提交交易接口中拿不到余额
        BalanceResponseDto balance = payRpcResolver.findBalanceByFundAccountId(userAccount.getFundAccountId());
        serialDto.setStartBalance(balance.getAvailableAmount());
        serialDto.setEndBalance(balance.getAvailableAmount());
        serialService.handleSuccess(serialDto);

        this.changeEmployee(requestDto.getNewCardNo(), userAccount.getCustomerId(), userAccount.getAccountId(), userAccount.getFirmId());
        return businessRecord.getSerialNo();
    }

    @Override
    public Long getChangeCardCostFee() {
        BigDecimal ruleFee = ruleFeeService.getRuleFee(RuleFeeBusinessType.CARD_CHANGE_CARD,
                SystemSubjectType.CARD_CHANGE_COST);
        return CurrencyUtils.yuan2Cent(ruleFee);
    }

    /**
     * 保存本地操作记录
     */
    private BusinessRecordDo saveLocalSerialRecordNoFundSerial(CardRequestDto cardParam, UserAccountCardResponseDto accountCard, OperateType operateType) {
        BusinessRecordDo businessRecord = serialService.createBusinessRecord(cardParam, accountCard, temp -> {
            temp.setType(operateType.getCode());
        });
        serialService.saveBusinessRecord(businessRecord);
        return businessRecord;
    }

    /**
     * saveRemoteSerialRecord
     */
    private void saveRemoteSerialRecord(BusinessRecordDo businessRecord) {
        //成功则修改状态及期初期末金额，存储操作流水
        SerialDto serialDto = serialService.createAccountSerial(businessRecord, null);
        serialService.handleSuccess(serialDto, false);
    }

    private void changeEmployee(String newCardNo, Long customerId, Long accountId, Long firmId) {
        ThreadUtil.execute(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("customerId", customerId);
            params.put("cardNo", newCardNo);
            params.put("cardAccountId", accountId);
            params.put("marketId", firmId);
            customerEmployeeRpc.changeCard(params);
        });
    }
}


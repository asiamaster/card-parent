package com.dili.card.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.Constant;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UnfreezeFundDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.FreezeFundRecordDto;
import com.dili.card.dto.pay.FreezeFundRecordParam;
import com.dili.card.dto.pay.FundOpResponseDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.PayRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IFundService;
import com.dili.card.service.ISerialService;
import com.dili.card.service.recharge.AbstractRechargeManager;
import com.dili.card.service.recharge.RechargeFactory;
import com.dili.card.type.BizNoType;
import com.dili.card.type.CardType;
import com.dili.card.type.FeeType;
import com.dili.card.type.FundItem;
import com.dili.card.type.OperateType;
import com.dili.ss.domain.PageOutput;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 资金操作service实现类
 *
 * @author xuliang
 */
@Service
public class FundServiceImpl implements IFundService {

    @Resource
    private ISerialService serialService;
    @Autowired
    private IAccountQueryService accountQueryService;
    @Autowired
    private PayRpcResolver payRpcResolver;
    @Resource
    private UidRpcResovler uidRpcResovler;
    @Resource
    private PayRpc payRpc;
    @Autowired
    private RechargeFactory rechargeFactory;

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public void frozen(FundRequestDto requestDto) {
        UserAccountCardResponseDto accountCard = accountQueryService.getByAccountId(requestDto);
        if (!CardType.isMaster(accountCard.getCardType())) {
            throw new CardAppBizException("只有主卡可以冻结资金");
        }
        BalanceResponseDto balance = payRpcResolver.findBalanceByFundAccountId(accountCard.getFundAccountId());
        if (requestDto.getAmount() > balance.getAvailableAmount()) {
            throw new CardAppBizException("可用余额不足");
        }

        BusinessRecordDo businessRecord = serialService.createBusinessRecord(requestDto, accountCard, record -> {
            record.setType(OperateType.FROZEN_FUND.getCode());
            record.setAmount(requestDto.getAmount());
            record.setNotes(requestDto.getMark());
        });
        CreateTradeRequestDto createTradeRequestDto = CreateTradeRequestDto
                .createFrozenAmount(accountCard.getFundAccountId(), accountCard.getAccountId(), requestDto.getAmount());
        createTradeRequestDto.setExtension(this.serializeFrozenExtra(requestDto));
        createTradeRequestDto.setDescription(requestDto.getMark());
        FundOpResponseDto fundOpResponseDto = payRpcResolver.postFrozenFund(createTradeRequestDto);

        businessRecord.setTradeNo(String.valueOf(fundOpResponseDto.getFrozenId()));
        serialService.saveBusinessRecord(businessRecord);

        TradeResponseDto transaction = fundOpResponseDto.getTransaction();
        transaction.addVirtualPrincipalFundItem(-requestDto.getAmount());
        SerialDto serialDto = serialService.createAccountSerialWithFund(businessRecord, transaction,
                (serialRecord, feeType) -> {
                    if (Integer.valueOf(FeeType.ACCOUNT.getCode()).equals(feeType)) {
                        serialRecord.setFundItem(FundItem.MANDATORY_FREEZE_FUND.getCode());
                        serialRecord.setFundItemName(FundItem.MANDATORY_FREEZE_FUND.getName());
                    }
                    serialRecord.setNotes(requestDto.getMark());
                }, true);
        serialService.handleSuccess(serialDto);
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public void unfrozen(UnfreezeFundDto unfreezeFundDto) {
        UserAccountCardResponseDto accountInfo = accountQueryService.getByAccountId(unfreezeFundDto.getAccountId());
        List<SerialRecordDo> serialList = new ArrayList<SerialRecordDo>();
        for (Long frozenId : unfreezeFundDto.getFrozenIds()) {
            // 对应支付的frozenId
            UnfreezeFundDto dto = new UnfreezeFundDto();
            dto.setFrozenId(frozenId);
            FundOpResponseDto payResponse = GenericRpcResolver.resolver(payRpc.unfrozenFund(dto), ServiceName.PAY);
            // 构建全局操作记录
            SerialRecordDo serialRecord = buildSerialRecord(accountInfo, unfreezeFundDto, payResponse);
            serialList.add(serialRecord);
        }
        SerialDto serialDto = new SerialDto();
        // 保存全局操作记录
        serialDto.setSerialRecordList(serialList);
        serialService.saveSerialRecord(serialDto);
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public String recharge(FundRequestDto requestDto) {
        AbstractRechargeManager rechargeManager = rechargeFactory.getRechargeManager(requestDto.getTradeChannel());
        return rechargeManager.doRecharge(requestDto);
    }

    @Override
    public PageOutput<List<FreezeFundRecordDto>> frozenRecord(FreezeFundRecordParam queryParam) {
        // 从支付查询 默认查询从当日起一年内的未解冻记录
        PageOutput<List<FreezeFundRecordDto>> pageOutPut = GenericRpcResolver
                .resolver(payRpc.listFrozenRecord(queryParam), "pay-service");
        for (FreezeFundRecordDto record : pageOutPut.getData()) {
            if (StringUtils.isNoneBlank(record.getExtension())) {
                // 在冻结资金时会以json格式存入当时的操作人及其编号
                JSONObject jsonObject = JSONObject.parseObject(record.getExtension());
                record.setOpNo(jsonObject.getString(Constant.OP_NO));
                record.setOpName(jsonObject.getString(Constant.OP_NAME));
            }
        }
        return pageOutPut;
    }

    /**
     * 生成全局日志
     *
     * @param accountInfo
     * @param unfreezeFundDto
     */
    private SerialRecordDo buildSerialRecord(UserAccountCardResponseDto accountInfo,
                                             UnfreezeFundDto unfreezeFundDto, FundOpResponseDto payResponse) {
        SerialRecordDo record = new SerialRecordDo();
        record.setAccountId(accountInfo.getAccountId());
        record.setCardNo(accountInfo.getCardNo());
        record.setCustomerId(accountInfo.getCustomerId());
        record.setCustomerName(accountInfo.getCustomerName());
        record.setCustomerNo(accountInfo.getCustomerCode());
        record.setFirmId(unfreezeFundDto.getFirmId());
        record.setSerialNo(uidRpcResovler.bizNumber(BizNoType.OPERATE_SERIAL_NO.getCode()));
        record.setNotes(unfreezeFundDto.getRemark());
        record.setOperatorId(unfreezeFundDto.getOpId());
        record.setOperatorName(unfreezeFundDto.getOpName());
        record.setOperatorNo(unfreezeFundDto.getOpNo());
        record.setTradeType(OperateType.UNFROZEN_FUND.getCode());
        record.setType(OperateType.UNFROZEN_FUND.getCode());
        record.setFundItem(FundItem.MANDATORY_UNFREEZE_FUND.getCode());
        record.setFundItemName(FundItem.MANDATORY_UNFREEZE_FUND.getName());
        record.setOperateTime(LocalDateTime.now());
        // 计算期初期末
        Long balance = payResponse.getTransaction().getBalance();
        Long frozenBalance = payResponse.getTransaction().getFrozenBalance();
        Long frozenAmount = payResponse.getTransaction().getFrozenAmount(); // 解冻金额该值为负数，冻结金额为正数
        record.setStartBalance(balance - frozenBalance);
        record.setEndBalance(balance - frozenBalance - frozenAmount);
        record.setAmount(Math.abs(frozenAmount));//操作记录对于可用余额要显示+
        return record;
    }

    private String serializeFrozenExtra(FundRequestDto requestDto) {
        JSONObject extra = new JSONObject();
        extra.put(Constant.OP_NAME, requestDto.getOpName());
        extra.put(Constant.OP_NO, requestDto.getOpNo());
        return extra.toJSONString();
    }
}

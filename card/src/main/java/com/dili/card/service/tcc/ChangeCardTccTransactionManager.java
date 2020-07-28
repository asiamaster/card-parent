package com.dili.card.service.tcc;

import com.dili.card.common.constant.Constant;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.rpc.resolver.CardManageRpcResolver;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IPayService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.FundItem;
import com.dili.card.type.OperateType;
import com.dili.card.type.TradeChannel;
import com.dili.card.type.TradeType;
import com.dili.tcc.AbstractTccTransactionManager;
import com.dili.tcc.core.TccContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 换卡tcc
 * @Auther: miaoguoxin
 * @Date: 2020/7/28 14:29
 */
@Component
public class ChangeCardTccTransactionManager extends AbstractTccTransactionManager<Boolean, CardRequestDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeCardTccTransactionManager.class);

    @Autowired
    private IAccountQueryService accountQueryService;
    @Autowired
    private ISerialService serialService;
    @Autowired
    private IPayService payService;
    @Autowired
    private CardManageRpcResolver cardManageRpcResolver;
    @Autowired
    private IAccountCycleService accountCycleService;

    @Override
    protected void prepare(CardRequestDto requestDto) {
        UserAccountCardResponseDto userAccount = accountQueryService.getByCardNo(requestDto.getCardNo());
        Long serviceFee = requestDto.getServiceFee();
        BusinessRecordDo businessRecord = serialService.createBusinessRecord(requestDto, userAccount, record -> {
            record.setType(OperateType.CHANGE.getCode());
            record.setAmount(serviceFee);
            record.setTradeType(TradeType.FEE.getCode());
            record.setTradeChannel(TradeChannel.CASH.getCode());
        });
        CreateTradeRequestDto tradeRequest = CreateTradeRequestDto.createTrade(
                TradeType.FEE.getCode(),
                userAccount.getAccountId(),
                userAccount.getFundAccountId(),
                serviceFee,
                businessRecord.getSerialNo(),
                String.valueOf(businessRecord.getCycleNo()));
        //创建交易
        String tradeId = payService.createTrade(tradeRequest);

        businessRecord.setTradeNo(tradeId);
        serialService.saveBusinessRecord(businessRecord);

        TccContextHolder.get().addAttr(Constant.TRADE_ID_KEY, tradeId);
        TccContextHolder.get().addAttr(Constant.BUSINESS_RECORD_KEY, businessRecord);
        TccContextHolder.get().addAttr(Constant.USER_ACCOUNT, userAccount);
    }

    @Override
    protected Boolean confirm(CardRequestDto requestDto) {
        BusinessRecordDo businessRecord = TccContextHolder.get().getAttr(Constant.BUSINESS_RECORD_KEY, BusinessRecordDo.class);

        String tradeNo = TccContextHolder.get().getAttr(Constant.TRADE_ID_KEY, String.class);
        UserAccountCardResponseDto userAccount = TccContextHolder.get().getAttr(Constant.USER_ACCOUNT, UserAccountCardResponseDto.class);
        TradeRequestDto tradeRequestDto = TradeRequestDto.createTrade(userAccount, tradeNo, TradeChannel.CASH.getCode(), requestDto.getLoginPwd());
        tradeRequestDto.addServiceFeeItem(requestDto.getServiceFee(), FundItem.IC_CARD_COST);
        payService.commitTrade(tradeRequestDto);

        cardManageRpcResolver.changeCard(requestDto);

        accountCycleService.increaseCashBox(businessRecord.getCycleNo(), requestDto.getServiceFee());
        try {
            SerialDto serialDto = serialService.createAccountSerial(businessRecord, null);
            serialService.handleSuccess(serialDto, false);
        } catch (Exception e) {
            LOGGER.error("changeCard", e);
        }
        return true;
    }

    @Override
    protected void cancel(CardRequestDto requestDto) {

    }
}

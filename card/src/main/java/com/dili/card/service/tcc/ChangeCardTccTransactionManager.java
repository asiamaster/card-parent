package com.dili.card.service.tcc;

import com.dili.card.common.constant.Constant;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.CardManageRpcResolver;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IPayService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.CardStatus;
import com.dili.card.type.FundItem;
import com.dili.card.type.OperateType;
import com.dili.card.type.TradeChannel;
import com.dili.card.type.TradeType;
import com.dili.card.validator.AccountValidator;
import com.dili.ss.constant.ResultCode;
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
        UserAccountCardResponseDto userAccount = accountQueryService.getByAccountIdForGenericOp(requestDto.getAccountId());
        AccountValidator.validateMatchAccount(requestDto, userAccount);
        this.validateCanChange(requestDto, userAccount);

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

        accountCycleService.increaseCashBox(businessRecord.getCycleNo(), requestDto.getServiceFee());

        businessRecord.setTradeNo(tradeId);
        serialService.saveBusinessRecord(businessRecord);

        //换卡操作放到try阶段执行，是为了防止confirm阶段因为各种意外情况失败
        //先确保换卡操作能够成功，然后confirm阶段只是对资金进行操作，不影响主业务流程
        cardManageRpcResolver.changeCard(requestDto);

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

    /**
     * 校验
     * @author miaoguoxin
     * @date 2020/7/29
     */
    private void validateCanChange(CardRequestDto requestDto, UserAccountCardResponseDto userAccount) {
        if (userAccount.getCardState() != CardStatus.NORMAL.getCode()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "该卡不是正常状态，不能进行该操作");
        }
        if (userAccount.getCardNo().equals(requestDto.getNewCardNo())) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "新老卡号不能相同");
        }
    }
}

package com.dili.card.service.recharge;

import com.dili.card.common.annotation.TradeChannelMark;
import com.dili.card.common.constant.Constant;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.type.FeeType;
import com.dili.card.type.FundItem;
import com.dili.card.type.TradeChannel;
import com.dili.card.type.TradeType;
import com.dili.tcc.core.TccContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 现金充值
 * @Auther: miaoguoxin
 * @Date: 2020/7/2 10:29
 */
@Component
@TradeChannelMark(TradeChannel.CASH)
public class CashRechargeService extends AbstractRechargeManager {

    @Autowired
    private IAccountCycleService accountCycleService;

    @Override
    public Long getRechargeAmount(FundRequestDto requestDto) {
        return requestDto.getAmount();
    }

    @Override
    public String buildBusinessRecordNote(FundRequestDto requestDto) {
        return "现金存款";
    }

    @Override
    public String buildSerialRecordNote(FundRequestDto requestDto) {
        return "现金存款";
    }

    @Override
    public FundItem getPrincipalFundItem(FundRequestDto fundRequestDto) {
        return FundItem.CASH_CHARGE;
    }


    @Override
    public FundItem getServiceCostItem(FundRequestDto fundRequestDto) {
        return null;
    }

    @Override
    public boolean canAddEmptyFundItem(FundRequestDto fundRequestDto) {
        return false;
    }

    @Override
    public TradeType getTradeType(FundRequestDto fundRequestDto) {
        return TradeType.DEPOSIT;
    }


    @Override
    protected void afterCommitRecharge(FundRequestDto requestDto) {
        BusinessRecordDo businessRecordDo = TccContextHolder.get().getAttr(Constant.BUSINESS_RECORD_KEY, BusinessRecordDo.class);
        //添加现金资金池
        accountCycleService.increaseCashBox(businessRecordDo.getCycleNo(), requestDto.getAmount());
    }
}

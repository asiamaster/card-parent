package com.dili.card.service.recharge;

import cn.hutool.core.util.NumberUtil;
import com.dili.card.common.annotation.TradeChannelMark;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.type.FundItem;
import com.dili.card.type.TradeChannel;
import org.springframework.stereotype.Service;

/**
 * 网银充值（提现失败的时候进行逆向操作）
 * @Auther: miaoguoxin
 * @Date: 2020/7/2 10:33
 */
@Service
@TradeChannelMark(TradeChannel.E_BANK)
public class EBankRechargeService extends AbstractRechargeManager {

    @Override
    public Long getRechargeAmount(FundRequestDto requestDto) {
        return NumberUtil.add(requestDto.getAmount(), requestDto.getServiceCost()).longValue();
    }

    @Override
    public String buildBusinessRecordNote(FundRequestDto requestDto) {
        return "网银充值";
    }

    @Override
    public String buildSerialRecordNote(FundRequestDto requestDto) {
        return "网银充值";
    }

    @Override
    public FundItem getPrincipalFundItem(FundRequestDto fundRequestDto) {
         return FundItem.CASH_CHARGE;
    }


    @Override
    public FundItem getServiceCostItem(FundRequestDto fundRequestDto) {
        return FundItem.EBANK_SERVICE;
    }

    @Override
    public boolean canAddEmptyFundItem(FundRequestDto fundRequestDto) {
        return fundRequestDto.getServiceCost() == null;
    }

}

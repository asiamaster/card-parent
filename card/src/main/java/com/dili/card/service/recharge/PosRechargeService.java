package com.dili.card.service.recharge;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.annotation.TradeChannelMark;
import com.dili.card.common.constant.Constant;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.type.BankCardType;
import com.dili.card.type.FundItem;
import com.dili.card.type.TradeChannel;
import com.dili.card.type.TradeType;
import com.dili.card.util.CurrencyUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

/**
 * pos鸡充值
 * @Auther: miaoguoxin
 * @Date: 2020/7/2 10:31
 */
@Component
@TradeChannelMark(TradeChannel.POS)
public class PosRechargeService extends AbstractRechargeManager {


    @Override
    public String buildBusinessRecordNote(FundRequestDto requestDto) {
        Long serviceCost = requestDto.getServiceCost();
        String yuan = CurrencyUtils.toYuanWithStripTrailingZeros(serviceCost == null ? 0L : serviceCost);
        JSONObject extra = requestDto.getExtra();
        if (extra == null) {
            extra = new JSONObject();
        }
        return String.format("pos存款, 手续费%s元，凭证号：%s", yuan, extra.getString(Constant.POS_CERT_NUM));
    }

    @Override
    public String buildSerialRecordNote(FundRequestDto requestDto) {
        JSONObject extra = requestDto.getExtra();
        if (extra == null) {
            extra = new JSONObject();
        }
        String bankTypeName = BankCardType.getNameByCode(extra.getInteger(Constant.BANK_TYPE));
        return String.format("pos存款:%s,凭证号:%s", bankTypeName, extra.getString(Constant.POS_CERT_NUM));
    }

    @Override
    public FundItem getPrincipalFundItem(FundRequestDto fundRequestDto) {
        return FundItem.POS_CHARGE;
    }


    @Override
    public FundItem getServiceCostItem(FundRequestDto fundRequestDto) {
        return FundItem.POS_SERVICE;
    }

    @Override
    public boolean canAddEmptyFundItem(FundRequestDto fundRequestDto) {
        return NumberUtils.toLong(fundRequestDto.getServiceCost() + "") <= 0L;
    }

    @Override
    public TradeType getTradeType(FundRequestDto fundRequestDto) {
        return TradeType.DEPOSIT;
    }
}

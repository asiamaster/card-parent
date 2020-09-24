package com.dili.card.service.print;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.Constant;
import com.dili.card.dto.PrintDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.type.OperateType;
import com.dili.card.type.PrintTemplate;
import com.dili.card.type.TradeChannel;
import com.dili.card.util.CurrencyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/9/7 14:55
 * @Description:
 */
@Service
public class RechargePrintServiceImpl extends PrintServiceImpl {
    @Override
    public String getPrintTemplate(BusinessRecordDo recordDo) {
        if (TradeChannel.CASH.getCode() == recordDo.getTradeChannel()) {
            return PrintTemplate.CASH_RECHARGE.getType();
        }
        if (TradeChannel.E_BANK.getCode() == recordDo.getTradeChannel()) {
            return PrintTemplate.E_BANK_RECHARGE.getType();
        }
        if (TradeChannel.POS.getCode() == recordDo.getTradeChannel()) {
            return PrintTemplate.POS_RECHARGE.getType();
        }
        throw new CardAppBizException("未找到合适的票据模板");
    }

    @Override
    public void createSpecial(PrintDto printDto, BusinessRecordDo recordDo, boolean reprint) {
        long totalAmount = 0;
        if (recordDo.getServiceCost() != null) {
            //网银相当于退款，所以是加钱
            totalAmount = (TradeChannel.E_BANK.getCode() == recordDo.getTradeChannel()) ?
                    recordDo.getAmount() + recordDo.getServiceCost() :
                    recordDo.getAmount() - recordDo.getServiceCost();
        } else {
            totalAmount = recordDo.getAmount();
        }
        printDto.setTotalAmount(CurrencyUtils.cent2TenNoSymbol(totalAmount));
        String s = Convert.digitToChinese(Double.valueOf(printDto.getTotalAmount()));
        printDto.setTotalAmountWords(s);

        String json = recordDo.getAttach();
        if (StringUtils.isBlank(json)) {
            return;
        }
        JSONObject extra = JSON.parseObject(json);
        printDto.setBankType(extra.getInteger(Constant.BANK_TYPE));
        printDto.setPosCertNum(extra.getString(Constant.POS_CERT_NUM));
        printDto.setPosType(extra.getInteger(Constant.POS_TYPE));
    }

    @Override
    public Integer support() {
        return OperateType.ACCOUNT_CHARGE.getCode();
    }
}

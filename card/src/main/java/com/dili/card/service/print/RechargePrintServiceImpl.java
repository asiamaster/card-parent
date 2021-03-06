package com.dili.card.service.print;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.ReqParamExtra;
import com.dili.card.dto.PrintDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.type.OperateType;
import com.dili.card.type.PrintTemplate;
import com.dili.card.type.TradeChannel;
import com.dili.card.util.CurrencyUtils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;

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
        long totalAmount = NumberUtil.sub(recordDo.getAmount(),recordDo.getServiceCost()).longValue();

        printDto.setTotalAmount(CurrencyUtils.cent2TenNoSymbol(totalAmount));
        String s = Convert.digitToChinese(Double.valueOf(printDto.getTotalAmount()));
        printDto.setTotalAmountWords(s);
        //由于需要展示最终的余额，因此这里要扣除手续费
        //根据需求实时获取最新余额 2020-10-19
        /*BigDecimal balance;
        if (TradeChannel.E_BANK.getCode() == recordDo.getTradeChannel()){
            balance = NumberUtil.sub(recordDo.getTotalBalance() + recordDo.getServiceCost());
        }else {
            balance = NumberUtil.sub(recordDo.getTotalBalance() - recordDo.getServiceCost());
        }
        printDto.setBalance(CurrencyUtils.cent2TenNoSymbol(balance.longValue()));*/

        String json = recordDo.getAttach();
        if (StringUtils.isBlank(json)) {
            return;
        }
        JSONObject extra = JSON.parseObject(json);
        printDto.setBankType(extra.getInteger(ReqParamExtra.BANK_TYPE));
        printDto.setPosCertNum(extra.getString(ReqParamExtra.POS_CERT_NUM));
        printDto.setPosType(extra.getString(ReqParamExtra.POS_TYPE));
    }

    @Override
    public Integer support() {
        return OperateType.ACCOUNT_CHARGE.getCode();
    }

    /* if (recordDo.getServiceCost() != null) {
            //网银相当于退款，所以是加钱
            totalAmount = (TradeChannel.E_BANK.getCode() == recordDo.getTradeChannel()) ?
                    recordDo.getAmount() + recordDo.getServiceCost() :
                    recordDo.getAmount() - recordDo.getServiceCost();
        } else {
            totalAmount = recordDo.getAmount();
        }*/
}

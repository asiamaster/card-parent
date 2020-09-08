package com.dili.card.service.print;

import com.dili.card.dto.PrintDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.type.OperateType;
import com.dili.card.type.PrintTemplate;
import com.dili.card.type.TradeChannel;
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

    }

    @Override
    public Integer support() {
        return OperateType.ACCOUNT_CHARGE.getCode();
    }
}

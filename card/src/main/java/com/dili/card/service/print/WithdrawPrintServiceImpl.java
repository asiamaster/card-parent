package com.dili.card.service.print;

import cn.hutool.core.convert.Convert;
import com.dili.card.dto.PrintDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.type.OperateType;
import com.dili.card.type.PrintTemplate;
import com.dili.card.type.TradeChannel;
import com.dili.card.util.CurrencyUtils;
import org.springframework.stereotype.Service;

/**
 * 提现
 */
@Service
public class WithdrawPrintServiceImpl extends PrintServiceImpl {


    @Override
    public String getPrintTemplate(BusinessRecordDo recordDo) {
        if (Integer.valueOf(TradeChannel.CASH.getCode()).equals(recordDo.getTradeChannel())) {//现金模板
            return PrintTemplate.CASH_WITHDRAW.getType();
        }
        if (Integer.valueOf(TradeChannel.E_BANK.getCode()).equals(recordDo.getTradeChannel())) {//现金模板
            return PrintTemplate.E_BANK_WITHDRAW.getType();
        }
        throw new CardAppBizException("未找到合适的票据模板");
    }

    @Override
    public void createSpecial(PrintDto printDto, BusinessRecordDo recordDo, boolean reprint) {
        Long totalAmount = recordDo.getServiceCost() != null ? recordDo.getAmount() + recordDo.getServiceCost() : recordDo.getAmount();
        printDto.setTotalAmount(CurrencyUtils.toYuanWithStripTrailingZeros(totalAmount));
        printDto.setTotalAmountWords(Convert.digitToChinese(Double.valueOf(printDto.getTotalAmount())));
    }

    @Override
    public Integer support() {
        return OperateType.ACCOUNT_WITHDRAW.getCode();
    }
}

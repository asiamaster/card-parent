package com.dili.card.service.print;

import com.dili.card.dto.PrintDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.type.OperateType;
import com.dili.card.util.CurrencyUtils;
import org.springframework.stereotype.Service;

/**
 * 提现
 */
@Service
public class WithdrawPrintServiceImpl extends PrintServiceImpl {


    @Override
    public String getPrintTemplate(BusinessRecordDo recordDo) {
        return "WithdrawPrintDocument";
    }

    @Override
    public void createSpecial(PrintDto printDto, BusinessRecordDo recordDo, boolean reprint) {
        Long totalAmount = recordDo.getServiceCost() != null ? recordDo.getAmount() + recordDo.getServiceCost() : recordDo.getAmount();
        printDto.setTotalAmount(CurrencyUtils.toYuanWithStripTrailingZeros(totalAmount));
    }

    @Override
    public Integer support() {
        return OperateType.ACCOUNT_WITHDRAW.getCode();
    }
}

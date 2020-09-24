package com.dili.card.service.print;

import org.springframework.stereotype.Service;

import com.dili.card.dto.PrintDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.type.OperateType;
import com.dili.card.type.PrintTemplate;
import com.dili.card.util.CurrencyUtils;

import cn.hutool.core.convert.Convert;

/**
 * 换卡打印
 * @Auther: miaoguoxin
 * @Date: 2020/9/7 09:24
 */
@Service
public class ReturnCardPrintServiceImpl extends PrintServiceImpl{
    @Override
    public String getPrintTemplate(BusinessRecordDo recordDo) {
        return PrintTemplate.RETURN_CARD.getType();
    }

    @Override
    public void createSpecial(PrintDto printDto, BusinessRecordDo recordDo, boolean reprint) {
    	Long totalAmount = recordDo.getAmount() != null ? recordDo.getAmount() : 0L;
        printDto.setTotalAmount(CurrencyUtils.cent2TenNoSymbol(totalAmount));
        printDto.setTotalAmountWords(Convert.digitToChinese(Double.valueOf(printDto.getTotalAmount())));
    }

    @Override
    public Integer support() {
        return OperateType.REFUND_CARD.getCode();
    }
}

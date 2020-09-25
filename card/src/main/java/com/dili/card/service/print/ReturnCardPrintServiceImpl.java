package com.dili.card.service.print;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.Constant;
import com.dili.card.dto.PrintDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.type.CardType;
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
    	Long totalAmount = 0L;
        printDto.setTotalAmount(CurrencyUtils.cent2TenNoSymbol(totalAmount));
        printDto.setTotalAmountWords(Convert.digitToChinese(Double.valueOf(printDto.getTotalAmount())));
        String attach = recordDo.getAttach();
        if (StringUtils.isNotBlank(attach)) {
			Integer cardType = JSONObject.parseObject(attach).getInteger(Constant.BUSINESS_RECORD_ATTACH_CARDTYPE);
			if(CardType.isSlave(cardType)) {
				printDto.setBalance("");
			}
		}
    }

    @Override
    public Integer support() {
        return OperateType.REFUND_CARD.getCode();
    }
}

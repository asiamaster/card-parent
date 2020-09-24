package com.dili.card.service.print;

import com.dili.card.dto.PrintDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.type.OperateType;
import com.dili.card.type.TradeChannel;
import com.dili.card.util.CurrencyUtils;
import com.dili.card.util.DateUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 打印基础实现类
 */
public abstract class PrintServiceImpl implements IPrintService{
    @Override
    public Map<String, Object> create(BusinessRecordDo recordDo, boolean reprint) {
        Map<String, Object> result = new ConcurrentHashMap<>();
        result.put("template", getPrintTemplate(recordDo));
        PrintDto printDto = new PrintDto();
        printDto.setName(OperateType.getName(recordDo.getType()));
        printDto.setOperateTime(DateUtil.formatDateTime(recordDo.getOperateTime(), "yyyy-MM-dd HH:mm:ss"));
        printDto.setReprint(reprint ? "补打" : "");
        printDto.setCustomerName(recordDo.getCustomerName());
        printDto.setCardNo(recordDo.getCardNo());
        printDto.setAmount(CurrencyUtils.cent2TenNoSymbol(recordDo.getAmount()));
        printDto.setBalance(CurrencyUtils.cent2TenNoSymbol(recordDo.getEndBalance()));
        printDto.setTradeChannel(recordDo.getTradeChannel() != null ? TradeChannel.getNameByCode(recordDo.getTradeChannel()) : "");
        printDto.setDeposit(CurrencyUtils.cent2TenNoSymbol(recordDo.getDeposit()));
        printDto.setCardCost(CurrencyUtils.cent2TenNoSymbol(recordDo.getCardCost()));
        printDto.setServiceCost(CurrencyUtils.cent2TenNoSymbol(recordDo.getServiceCost()));
        printDto.setOperatorNo(recordDo.getOperatorNo());
        printDto.setOperatorName(recordDo.getOperatorName());
        printDto.setNotes(recordDo.getNotes());
        createSpecial(printDto, recordDo, reprint);
        result.put("data", printDto);
        return result;
    }
}

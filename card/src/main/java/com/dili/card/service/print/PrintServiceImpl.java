package com.dili.card.service.print;

import com.dili.card.dto.PrintDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.UserAccountSingleQueryDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.rpc.resolver.AccountQueryRpcResolver;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.type.OperateType;
import com.dili.card.type.TradeChannel;
import com.dili.card.util.CurrencyUtils;
import com.dili.card.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 打印基础实现类
 */
public abstract class PrintServiceImpl implements IPrintService{

    @Autowired
    private PayRpcResolver payRpcResolver;
    @Autowired
    private AccountQueryRpcResolver accountQueryRpcResolver;
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
        //根据需求实时获取最新余额 2020-10-19
        printDto.setBalance(CurrencyUtils.cent2TenNoSymbol(queryTotalBalance(recordDo.getAccountId())));
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

    /**
     * 根据卡账户ID查询余额
     * @param accountId
     * @return
     */
    private Long queryTotalBalance(Long accountId) {
        UserAccountSingleQueryDto userAccountSingleQuery = new UserAccountSingleQueryDto();
        userAccountSingleQuery.setAccountId(accountId);
        UserAccountCardResponseDto account = accountQueryRpcResolver.findSingleWithoutValidate(userAccountSingleQuery);
        if (account == null) {
            return 0L;
        }
        BalanceResponseDto balanceResponse = payRpcResolver.findBalanceByFundAccountId(account.getFundAccountId());
        return balanceResponse != null ? balanceResponse.getBalance() : 0L;
    }
}

package com.dili.card.service;

import com.dili.card.dto.pay.*;

/**
 * 用于处理支付对接的service接口
 * @author xuliang
 */
public interface IPayService {

    /**
     * 创建交易
     * @param createTradeRequest
     * @return
     */
    String createTrade(CreateTradeRequestDto createTradeRequest);

    /**
     * 提现提交
     * @param withdrawRequest
     * @return
     */
    TradeResponseDto commitWithdraw(TradeRequestDto withdrawRequest);

    /**
    * 提交交易
    * @author miaoguoxin
    * @date 2020/7/8
    */
    TradeResponseDto commitTrade(TradeRequestDto requestDto);

    /**
     * 余额查询
     * @param balanceRequestDto
     * @return
     */
    BalanceResponseDto queryBalance(BalanceRequestDto balanceRequestDto);

    /**
    * 冻结资金
    * @author miaoguoxin
    * @date 2020/6/30
    */
    Long frozenFund(Long fundAccountId, Long amount);

}

package com.dili.card.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.card.dto.pay.BalanceRequestDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.CreateTradeResponseDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.service.IPayService;

/**
 * 用于处理支付对接的service实现类
 *
 * @author xuliang
 */
@Service
public class PayServiceImpl implements IPayService {

    @Autowired
    private PayRpcResolver payRpcResolver;
    //请求uri 目前只有一个
    private static final String URI = "/payment/api/gateway.do";
    //请求中Content-Type值
    private static final String CONTENT_TYPE = "application/json";


    @Override
    public String createTrade(CreateTradeRequestDto createTradeRequest) {
        CreateTradeResponseDto createTradeResponse = payRpcResolver.prePay(createTradeRequest);
        return createTradeResponse.getTradeId();
    }

    @Override
    public TradeResponseDto commitWithdraw(TradeRequestDto withdrawRequest) {
        return payRpcResolver.trade(withdrawRequest);
    }

    @Override
    public TradeResponseDto commitTrade(TradeRequestDto requestDto) {
        return payRpcResolver.trade(requestDto);
    }


    @Override
    public BalanceResponseDto queryBalance(BalanceRequestDto balanceRequestDto) {
        return payRpcResolver.findBalanceByFundAccountId(balanceRequestDto.getAccountId());
    }
}

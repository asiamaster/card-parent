package com.dili.card.rpc.resolver;

import com.dili.card.dto.pay.*;
import com.dili.card.rpc.PayRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/30 14:02
 */
@Component("payRpcResolver")
public class PayRpcResolver {
    @Autowired
    private PayRpc payRpc;

    private static final String SERVICE_NAME = "pay-service";
    /**
    *  提交提现
    * @author miaoguoxin
    * @date 2020/7/1
    */
    public TradeResponseDto trade(TradeRequestDto withdrawRequest){
        return GenericRpcResolver.resolver(payRpc.commitTrade(withdrawRequest), SERVICE_NAME);
    }

    /**
    * 创建交易
    * @author miaoguoxin
    * @date 2020/7/1
    */
    public CreateTradeResponseDto prePay(CreateTradeRequestDto createTradeRequest){
       return GenericRpcResolver.resolver(payRpc.preparePay(createTradeRequest), SERVICE_NAME);
    }
    /**
     *  查询余额
     * @author miaoguoxin
     * @date 2020/6/30
     */
    public BalanceResponseDto findBalanceByFundAccountId(Long fundAccountId) {
        CreateTradeRequestDto requestDto = new CreateTradeRequestDto();
        requestDto.setAccountId(fundAccountId);
        return GenericRpcResolver.resolver(payRpc.getAccountBalance(requestDto), SERVICE_NAME);
    }

    /**
     *  冻结资金操作
     * @author miaoguoxin
     * @date 2020/6/30
     */
    public FundOpResponseDto postFrozenFund(CreateTradeRequestDto requestDto) {
        return GenericRpcResolver.resolver(payRpc.frozenFund(requestDto), SERVICE_NAME);
    }
    
    /**
     *  冻结账户操作
     */
    public void freezeFundAccount(CreateTradeRequestDto requestDto) {
        GenericRpcResolver.resolver(payRpc.freeze(requestDto), SERVICE_NAME);
    }
    
    /**
     *  解冻账户操作
     */
    public void unfreezeFundAccount(CreateTradeRequestDto requestDto) {
        GenericRpcResolver.resolver(payRpc.unfreeze(requestDto), SERVICE_NAME);
    }
}


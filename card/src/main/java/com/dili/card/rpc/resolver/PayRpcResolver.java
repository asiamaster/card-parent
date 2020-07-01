package com.dili.card.rpc.resolver;

import com.dili.card.dto.pay.*;
import com.dili.card.rpc.PayRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/30 14:02
 */
@Component
public class PayRpcResolver {
    @Autowired
    private PayRpc payRpc;

    /**
    *  提交提现
    * @author miaoguoxin
    * @date 2020/7/1
    */
    public WithdrawResponseDto withdraw(WithdrawRequestDto withdrawRequest){
        return GenericRpcResolver.resolver(payRpc.withdraw(withdrawRequest));
    }

    /**
    * 创建交易
    * @author miaoguoxin
    * @date 2020/7/1
    */
    public CreateTradeResponseDto prePay(CreateTradeRequestDto createTradeRequest){
       return GenericRpcResolver.resolver(payRpc.preparePay(createTradeRequest));
    }
    /**
     *  查询余额
     * @author miaoguoxin
     * @date 2020/6/30
     */
    public BalanceResponseDto findBalanceByFundAccountId(Long fundAccountId) {
        CreateTradeRequestDto requestDto = new CreateTradeRequestDto();
        requestDto.setAccountId(fundAccountId);
        return GenericRpcResolver.resolver(payRpc.getAccountBalance(requestDto));
    }

    /**
     *  冻结资金操作
     * @author miaoguoxin
     * @date 2020/6/30
     */
    public Long postFrozenFund(Long fundAccountId, Long amount) {
        CreateTradeRequestDto requestDto = new CreateTradeRequestDto();
        requestDto.setAccountId(fundAccountId);
        requestDto.setAmount(amount);
        return GenericRpcResolver.resolver(payRpc.frozenFund(requestDto));
    }
}


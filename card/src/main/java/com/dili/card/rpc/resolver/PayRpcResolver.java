package com.dili.card.rpc.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dili.card.common.constant.ServiceName;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.CreateTradeResponseDto;
import com.dili.card.dto.pay.FundOpResponseDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.rpc.PayRpc;
import com.dili.ss.domain.BaseOutput;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/30 14:02
 */
@Component("payRpcResolver")
public class PayRpcResolver {
    @Autowired
    private PayRpc payRpc;

    /**
     *  提交交易
     * @author miaoguoxin
     * @date 2020/7/1
     */
    public TradeResponseDto trade(TradeRequestDto withdrawRequest) {
        BaseOutput<TradeResponseDto> tradeResponseDtoBaseOutput = payRpc.commitTrade(withdrawRequest);
        return GenericRpcResolver.resolver(tradeResponseDtoBaseOutput, ServiceName.PAY);
    }

    /**
     * 创建交易
     * @author miaoguoxin
     * @date 2020/7/1
     */
    public CreateTradeResponseDto prePay(CreateTradeRequestDto createTradeRequest) {
        return GenericRpcResolver.resolver(payRpc.preparePay(createTradeRequest), ServiceName.PAY);
    }

    /**
     *  查询余额
     * @author miaoguoxin
     * @date 2020/6/30
     */
    public BalanceResponseDto findBalanceByFundAccountId(Long fundAccountId) {
        CreateTradeRequestDto requestDto = new CreateTradeRequestDto();
        requestDto.setAccountId(fundAccountId);
        return GenericRpcResolver.resolver(payRpc.getAccountBalance(requestDto), ServiceName.PAY);
    }

    /**
     *  查询余额（扩展）
     * @author miaoguoxin
     * @date 2020/6/30
     */
    public BalanceResponseDto findBalanceByFundAccountIdEx(Long fundAccountId) {
        CreateTradeRequestDto requestDto = new CreateTradeRequestDto();
        requestDto.setAccountId(fundAccountId);
        return GenericRpcResolver.resolver(payRpc.getAccountBalanceEx(requestDto), ServiceName.PAY);
    }

    /**
     *  冻结资金操作
     * @author miaoguoxin
     * @date 2020/6/30
     */
    public FundOpResponseDto postFrozenFund(CreateTradeRequestDto requestDto) {
        return GenericRpcResolver.resolver(payRpc.frozenFund(requestDto), ServiceName.PAY);
    }

    /**
     *  冻结账户操作
     */
    public void freezeFundAccount(CreateTradeRequestDto requestDto) {
        GenericRpcResolver.resolver(payRpc.freeze(requestDto), ServiceName.PAY);
    }

    /**
     *  解冻账户操作
     */
    public void unfreezeFundAccount(CreateTradeRequestDto requestDto) {
        GenericRpcResolver.resolver(payRpc.unfreeze(requestDto), ServiceName.PAY);
    }

    /**
     *  注销账户操作
     */
    public void unregister(CreateTradeRequestDto requestDto) {
        GenericRpcResolver.resolver(payRpc.unregister(requestDto), ServiceName.PAY);
    }

    /**
     *  密码重置
     */
    public void resetPwd(CreateTradeRequestDto requestDto) {
        GenericRpcResolver.resolver(payRpc.resetPwd(requestDto), ServiceName.PAY);
    }
}


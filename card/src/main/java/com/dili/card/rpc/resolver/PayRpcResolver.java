package com.dili.card.rpc.resolver;

import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.PayRpc;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
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
     *  查询余额
     * @author miaoguoxin
     * @date 2020/6/30
     */
    public BalanceResponseDto findBalanceByFundAccountId(Long fundAccountId) {
        CreateTradeRequestDto requestDto = new CreateTradeRequestDto();
        requestDto.setAccountId(fundAccountId);
        BaseOutput<BalanceResponseDto> balance = payRpc.getAccountBalance(requestDto);
        if (!balance.isSuccess()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, balance.getMessage());
        }
        return balance.getData();
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
        BaseOutput<Long> result = payRpc.frozenFund(requestDto);
        if (!result.isSuccess()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, result.getMessage());
        }
        return result.getData();
    }
}


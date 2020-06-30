package com.dili.card.service;

import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.WithdrawRequestDto;
import com.dili.card.dto.pay.WithdrawResponseDto;
import com.dili.ss.domain.BaseOutput;

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
    BaseOutput<WithdrawResponseDto> commitWithdraw(WithdrawRequestDto withdrawRequest);
}
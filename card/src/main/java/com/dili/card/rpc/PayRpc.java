package com.dili.card.rpc;

import com.dili.card.config.PayServiceFeignConfig;
import com.dili.card.dto.UnfreezeFundDto;
import com.dili.card.dto.pay.*;
import com.dili.ss.domain.BaseOutput;
import com.dili.tcc.common.Tcc;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @description：
 *          支付服务调用
 * @author ：WangBo
 * @time ：2020年6月22日下午5:52:52
 */
@FeignClient(value = "pay-service", configuration = PayServiceFeignConfig.class)
public interface PayRpc {

    /**
    * 提交交易(充值、提现)
    * @author miaoguoxin
    * @date 2020/7/1
    */
    @Tcc
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.trade.service:commit", method = RequestMethod.POST)
    BaseOutput<TradeResponseDto> commitTrade(TradeRequestDto requestDto);

    /**
    * 创建交易（预支付）
    * @author miaoguoxin
    * @date 2020/7/1
    */
    @Tcc
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.trade.service:prepare", method = RequestMethod.POST)
    BaseOutput<CreateTradeResponseDto> preparePay(CreateTradeRequestDto createTradeRequest);


    /**
     * 冻结资金
     * @param requestDto
     * @return
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.fund.service:freeze", method = RequestMethod.POST)
    BaseOutput<FundOpResponseDto> frozenFund(CreateTradeRequestDto requestDto);

    /**
     * 解冻资金
     * @param frozenId
     * @return
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.fund.service:unfreeze", method = RequestMethod.POST)
    BaseOutput<UnfreezeFundDto> unfrozenFund(UnfreezeFundDto unfreezeFundDto);

    /**
     * 查询余额
     * @author miaoguoxin
     * @date 2020/6/30
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.fund.service:query", method = RequestMethod.POST)
    BaseOutput<BalanceResponseDto> getAccountBalance(CreateTradeRequestDto requestDto);
}

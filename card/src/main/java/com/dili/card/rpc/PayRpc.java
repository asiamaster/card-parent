package com.dili.card.rpc;

import com.dili.card.config.PayServiceFeignConfig;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.ss.domain.BaseOutput;
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
     * 冻结资金
     * @param requestDto
     * @return
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.fund.service:freeze", method = RequestMethod.POST)
    BaseOutput<Long> frozenFund(CreateTradeRequestDto requestDto);

    /**
     * 查询余额
     * @author miaoguoxin
     * @date 2020/6/30
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.fund.service:query", method = RequestMethod.POST)
    BaseOutput<BalanceResponseDto> getAccountBalance(CreateTradeRequestDto requestDto);
}

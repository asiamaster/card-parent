package com.dili.card.rpc;

import com.dili.card.config.PayServiceFeignConfig;
import com.dili.card.dto.UnfreezeFundDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.CreateTradeResponseDto;
import com.dili.card.dto.pay.FreezeFundRecordDto;
import com.dili.card.dto.pay.FreezeFundRecordParam;
import com.dili.card.dto.pay.FundOpResponseDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @description： 支付服务调用
 *
 * @author ：WangBo
 * @time ：2020年6月22日下午5:52:52
 */
@FeignClient(value = "pay-service", configuration = PayServiceFeignConfig.class
        /*, url = "http://10.28.1.188:8080"*/)
public interface PayRpc {
    static final String SERVICE_NAME = "pay-service";

    /**
     * 提交交易(充值、提现)
     *
     * @author miaoguoxin
     * @date 2020/7/1
     */
    //@Tcc
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.trade.service:commit", method = RequestMethod.POST)
    BaseOutput<TradeResponseDto> commitTrade(TradeRequestDto requestDto);

    /**
     * 创建交易（预支付）
     *
     * @author miaoguoxin
     * @date 2020/7/1
     */
    //@Tcc
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.trade.service:prepare", method = RequestMethod.POST)
    BaseOutput<CreateTradeResponseDto> preparePay(CreateTradeRequestDto createTradeRequest);

    /**
     * 冻结资金
     *
     * @param requestDto
     * @return
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.fund.service:freeze", method = RequestMethod.POST)
    BaseOutput<FundOpResponseDto> frozenFund(CreateTradeRequestDto requestDto);

    /**
     * 解冻资金
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.fund.service:unfreeze", method = RequestMethod.POST)
    BaseOutput<UnfreezeFundDto> unfrozenFund(UnfreezeFundDto unfreezeFundDto);

    /**
     * 查询人工冻结及解冻记录
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.fund.service:listFrozen", method = RequestMethod.POST)
    PageOutput<List<FreezeFundRecordDto>> listFrozenRecord(FreezeFundRecordParam param);

    /**
     * 查询余额
     *
     * @author miaoguoxin
     * @date 2020/6/30
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.fund.service:query", method = RequestMethod.POST)
    BaseOutput<BalanceResponseDto> getAccountBalance(CreateTradeRequestDto requestDto);

    /**
     * 注销资金账户
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.account.service:unregister", method = RequestMethod.POST)
    BaseOutput<?> unregister(CreateTradeRequestDto requestDto);

    /**
     * 解冻资金账户
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.account.service:unfreeze", method = RequestMethod.POST)
    BaseOutput<?> unfreeze(CreateTradeRequestDto requestDto);

    /**
     * 冻结资金账户
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.account.service:freeze", method = RequestMethod.POST)
    BaseOutput<?> freeze(CreateTradeRequestDto requestDto);
    
    /**
     * 密码重置
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.permission.service:resetPwd", method = RequestMethod.POST)
    BaseOutput<?> resetPwd(CreateTradeRequestDto requestDto);
}

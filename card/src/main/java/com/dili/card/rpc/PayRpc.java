package com.dili.card.rpc;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.config.PayServiceFeignConfig;
import com.dili.card.dto.FirmWithdrawAuthRequestDto;
import com.dili.card.dto.FundAccountDto;
import com.dili.card.dto.PayCreateFundReponseDto;
import com.dili.card.dto.UnfreezeFundDto;
import com.dili.card.dto.pay.*;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.fasterxml.jackson.databind.deser.Deserializers.Base;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @description： 支付服务调用
 *
 * @author ：WangBo
 * @time ：2020年6月22日下午5:52:52
 */
@FeignClient(value = "pay-service", configuration = PayServiceFeignConfig.class, url = "${payService.url:}")
public interface PayRpc {

    /**
     * 提交交易(充值、提现)
     *
     * @author miaoguoxin
     * @date 2020/7/1
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.trade.service:commit", method = RequestMethod.POST)
    BaseOutput<TradeResponseDto> commitTrade(TradeRequestDto requestDto);

    /**
     * 冲正
     * @author miaoguoxin
     * @date 2020/12/4
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.trade.service:correct", method = RequestMethod.POST)
    BaseOutput<TradeResponseDto> reverse(PayReverseRequestDto requestDto);

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
    BaseOutput<FundOpResponseDto> unfrozenFund(UnfreezeFundDto unfreezeFundDto);

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
     * 查询余额（扩展）
     * @author miaoguoxin
     * @date 2020/7/1
     */
    //@Tcc
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.fund.service:queryEx", method = RequestMethod.POST)
    BaseOutput<BalanceResponseDto> getAccountBalanceEx(CreateTradeRequestDto createTradeRequest);

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

    /**
     * 创建资金账户服务
     * @param type
     * @return
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.account.service:register", method = RequestMethod.POST)
    BaseOutput<PayCreateFundReponseDto> createFundAccount(FundAccountDto type);


    /**
     * 查询通道流水
     * @return
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.channel.service:listTrades", method = RequestMethod.POST)
    PageOutput<List<PipelineRecordResponseDto>> pipelineList(PipelineRecordParam param);

    /**
     * 查询客户总资产及明细
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.fund.service:customer", method = RequestMethod.POST)
    BaseOutput<CustomerBalanceResponseDto> getAccountFundByCustomerId(FundAccountDto type);


    ///
    ///支付与银行接口相关
    ///

    /**
     * 根据银行卡号查询银行信息
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.channel.service:bankCard", method = RequestMethod.POST)
    BaseOutput<PayBankDto> getBankInfo(PayBankDto payBankDto);


    /**
     * 查询市场支持的银行渠道
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.channel.service:listChannels", method = RequestMethod.POST)
    BaseOutput<List<PayBankDto>> getBankChannels(PayBankDto payBankDto);

    /**
     * 根据关键字搜索开户行
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.channel.service:listBanks", method = RequestMethod.POST)
    BaseOutput<List<PayBankDto>> searchOpeningBank(PayBankDto payBankDto);

    /**
     * 查询商户账号
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.account.service:merchant", method = RequestMethod.POST)
    BaseOutput<MerAccountResponseDto> getMerAccount(JSONObject params);

    /**
     * 查询卡账户权限信息
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.permission.service:load", method = RequestMethod.POST)
    BaseOutput<AccountPermissionResponseDto> loadPermission(JSONObject params);

    /**
     * 设置卡账户权限
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.permission.service:set", method = RequestMethod.POST)
    BaseOutput setPermission(Map<String, Object> params);

    /**
     * 校验支付密码
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.permission.service:password", method = RequestMethod.POST)
    BaseOutput<?> checkTradePwd(FirmWithdrawAuthRequestDto params);
    
    /**
     * 获取账户限额设置
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.sentinel.service:loadGlobal", method = RequestMethod.POST)
    BaseOutput<PayGlobalConfigDto> getPayGlobal(Map<String, Object> params);
    
    /**
     * 账户限额设置
     */
    @RequestMapping(value = "/payment/api/gateway.do?service=payment.sentinel.service:setGlobal", method = RequestMethod.POST)
    BaseOutput<PayGlobalConfigDto> setPayGlobal(PayGlobalConfigDto payGlobalConfigDto);
}

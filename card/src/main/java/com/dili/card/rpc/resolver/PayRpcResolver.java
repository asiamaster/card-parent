package com.dili.card.rpc.resolver;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.CreateTradeResponseDto;
import com.dili.card.dto.pay.FundOpResponseDto;
import com.dili.card.dto.pay.PayGlobalConfigDto;
import com.dili.card.dto.pay.PwdFreeProtocolQueryDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.PayRpc;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/30 14:02
 */
@Component("payRpcResolver")
public class PayRpcResolver {
   private static Logger log = LoggerFactory.getLogger(GenericRpcResolver.class);

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

    /**
     *  设置卡账户权限
     */
    public void setPermission(Map<String, Object> params) {
        GenericRpcResolver.resolver(payRpc.setPermission(params), ServiceName.PAY);
    }

    /**
     *  获取账户限额设置
     */
    public PayGlobalConfigDto getPayGlobal(String mchId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mchId", mchId);
        return GenericRpcResolver.resolver(payRpc.getPayGlobal(params), ServiceName.PAY);
    }

    /**
     *  账户限额设置
     */
    public PayGlobalConfigDto setPayGlobal(PayGlobalConfigDto payGlobalConfigDto) {
        return GenericRpcResolver.resolver(payRpc.setPayGlobal(payGlobalConfigDto), ServiceName.PAY);
    }

    /**
     * 获取免密协议号
     * @author miaoguoxin
     * @date 2021/5/8
     */
    public String getPwdFreeProtocolNo(PwdFreeProtocolQueryDto queryDto) {
        BaseOutput<String> result = payRpc.getPwdFreeProtocol(queryDto);
        if ("200".equals(result.getCode())) {
            return result.getData();
        } else {
            //当前未开通免密，但允许开通
            if ("509002 ".equals(result.getCode())){
                return null;
            }
            log.error("{}远程服务返回了一个错误![{}]", ServiceName.PAY, JSONObject.toJSONString(result));
            throw new CardAppBizException(result.getCode(), result.getMessage());
        }
    }
}


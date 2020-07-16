package com.dili.card.rpc.resolver;

import com.alibaba.fastjson.JSON;
import com.dili.card.BaseTest;
import com.dili.card.dto.pay.BalanceResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/30 15:02
 * @Description:
 */
class PayRpcResolverTest extends BaseTest {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @SpyBean
    private PayRpcResolver payRpcResolver;

    @Test
    void testFindBalanceByFundAccountId() {
//        doReturn(new AccountFundResponseDto())
//                .when(payRpcResolver)
//                .findBalanceByFundAccountId(100252L);
        BalanceResponseDto responseDto = payRpcResolver.findBalanceByFundAccountId(100252L);
        LOGGER.info("获取到的结果:{}", JSON.toJSONString(responseDto));
    }

    @Test
    void postFrozenFund() {
    }
}

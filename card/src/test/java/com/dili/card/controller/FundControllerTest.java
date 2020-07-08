package com.dili.card.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dili.card.BaseTest;
import com.dili.card.common.constant.CacheKey;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.entity.AccountCycleDo;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.impl.AccountCycleServiceImpl;
import com.dili.card.type.TradeChannel;
import com.dili.ss.domain.BaseOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/8 11:22
 * @Description:
 */
class FundControllerTest extends BaseTest {
    @MockBean
    private IAccountCycleService accountCycleService;
    @Autowired
    private FundController fundController;
    @Autowired
    private MockMvc mockMvc;

    private String token;

    @BeforeEach
    public void before() throws Exception {
        this.token = this.createToken();
    }

    @Test
    void testRecharge() throws Exception {
        FundRequestDto fundRequestDto = this.createRechargeRequest();
        AccountCycleDo accountCycle = this.createAccountCycle();
        doReturn(accountCycle).when(accountCycleService)
                .findActiveCycleByUserId(fundRequestDto.getOpId(),
                        fundRequestDto.getOpName(),
                        fundRequestDto.getOpNo());

        MvcResult mvcResult = mockMvc.perform(post("/fund/recharge.action")
                .contentType(MediaType.APPLICATION_JSON)
                .header("duplicate_commit_token",this.token)
                .content(JSON.toJSONString(fundRequestDto))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        String resultContent = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        BaseOutput<?> out = JSON.parseObject(resultContent, new TypeReference<>() {
        });
        assertTrue(out.isSuccess());
        LOGGER.info("返回结果：{}",JSON.toJSONString(out));
    }

    private FundRequestDto createRechargeRequest() {
        FundRequestDto fundRequestDto = new FundRequestDto();
        fundRequestDto.setAccountId(174L);
        fundRequestDto.setCardNo("888889690048");
        fundRequestDto.setCustomerId(105L);
        fundRequestDto.setAmount(1L);
        fundRequestDto.setOpId(73L);
        fundRequestDto.setOpName("王波");
        fundRequestDto.setOpNo("");
        fundRequestDto.setFirmId(1L);
        fundRequestDto.setFirmName("集团");
        fundRequestDto.setAmount(1L);
        fundRequestDto.setTradePwd("123456");
        fundRequestDto.setTradeChannel(TradeChannel.CASH.getCode());

        return fundRequestDto;
    }

    private AccountCycleDo createAccountCycle() {
        AccountCycleDo accountCycleDo = new AccountCycleDo();
        accountCycleDo.setCycleNo(202007020001L);
        return accountCycleDo;
    }

    private String createToken() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/appCommon/duplicateToken.action")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String resultContent = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        BaseOutput<String> out = JSON.parseObject(resultContent, new TypeReference<>() {
        });
        assertTrue(out.isSuccess());
        return out.getData();
    }
}

package com.dili.card.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dili.card.BaseTest;
import com.dili.card.dto.CardRequestDto;
import com.dili.ss.domain.BaseOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/14 09:29
 * @Description:
 */
class CardManageControllerTest extends BaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestTemplate restTemplate;

    private String sessionId;
    @BeforeEach
    public void setUp(){
        this.sessionId = "ba2a48fd-4773-4c70-bce5-310ccceeb9f6";
    }

    @Test
    void changeCard() {
        CardRequestDto cardParam = new CardRequestDto();
        cardParam.setLoginPwd("123456");
    }

    @Test
    void testReportLoss() throws Exception {
        CardRequestDto cardParam = new CardRequestDto();
        cardParam.setLoginPwd("123456");
        cardParam.setCustomerId(105L);
        cardParam.setAccountId(174L);
        cardParam.setCardNo("888889690048");
        MvcResult mvcResult = mockMvc.perform(post("/card/reportLossCard.action")
                .contentType(MediaType.APPLICATION_JSON)
                //先登录uap获取sessionId
                .header("UAP_SessionId",this.sessionId)
                .content(JSON.toJSONString(cardParam))
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

}

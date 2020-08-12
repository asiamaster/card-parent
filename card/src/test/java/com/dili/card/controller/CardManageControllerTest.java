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
        this.sessionId = "70dd86ff-316f-4b18-8654-4d716033791c";
    }

    @Test
    void changeCard() throws Exception {
        CardRequestDto cardParam = new CardRequestDto();
        cardParam.setLoginPwd("12345");
        cardParam.setCustomerId(226L);
        cardParam.setAccountId(21L);
        cardParam.setCardNo("888800033481");
        cardParam.setNewCardNo("888810032608");
        cardParam.setServiceFee(1L);
        MvcResult mvcResult = mockMvc.perform(post("/card/changeCard.action")
                .contentType(MediaType.APPLICATION_JSON)
                //先登录uap获取sessionId
                .header("UAP_SessionId",this.sessionId)
                .content(JSON.toJSONString(cardParam))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void testReportLoss() throws Exception {
        CardRequestDto cardParam = new CardRequestDto();
        cardParam.setLoginPwd("123456");
        cardParam.setCustomerId(215L);
        cardParam.setAccountId(11L);
        cardParam.setCardNo("888889688962");
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

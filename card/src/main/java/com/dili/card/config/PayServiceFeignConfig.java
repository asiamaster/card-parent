package com.dili.card.config;

import com.alibaba.fastjson.JSONObject;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import feign.RequestInterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

/**
 * 支付服务的Feign配置
 * ps：不要在该类上面加诸如 @Configuration，否则会变成全局配置
 * @Auther: miaoguoxin
 * @Date: 2020/6/29 17:01
 */
public class PayServiceFeignConfig {
	
	private static final Logger log = LoggerFactory.getLogger(PayServiceFeignConfig.class);


    private static final String APPID = "1010";
    private static final String TOKEN = "abcd1010";

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            template.header("appid", APPID);
            template.header("token", TOKEN);
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            if (userTicket == null) {
            	log.info(JSONObject.toJSONString(userTicket));
                return;
            }
            Long firmId = userTicket.getFirmId();
            template.header("mchId", firmId + "");
        };
    }
}

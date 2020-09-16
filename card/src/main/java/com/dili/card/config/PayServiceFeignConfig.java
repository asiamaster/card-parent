package com.dili.card.config;

import com.dili.uap.sdk.session.SessionContext;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * 支付服务的Feign配置
 * ps：不要在该类上面加诸如 @Configuration，否则会变成全局配置
 * @Auther: miaoguoxin
 * @Date: 2020/6/29 17:01
 */
public class PayServiceFeignConfig {

    private static final String APPID = "100101";
    private static final String TOKEN = "abcd1234";

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            template.header("appid", APPID);
            template.header("token", TOKEN);
            SessionContext sessionContext = FeignGlobalConfig.getSessionContext();
            if (sessionContext == null) {
                return;
            }
            Long firmId = sessionContext.getUserTicket().getFirmId();
            template.header("mchid", firmId + "");
        };
    }
}

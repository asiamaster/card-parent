package com.dili.card.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.Constant;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import feign.Request;
import feign.RequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

/**
 * 这个是feign的全局配置
 * @Auther: miaoguoxin
 * @Date: 2020/7/10 10:27
 */
@Configuration
@ConditionalOnProperty(value = "feign.global.config.enabled", havingValue = "true", matchIfMissing = true)
public class FeignGlobalConfig {
    private static Logger LOGGER = LoggerFactory.getLogger(FeignGlobalConfig.class);

    /**
     * 统一添加市场id的拦截器
     * @author miaoguoxin
     * @date 2020/7/10
     */
    @Bean(name = "contextRequestInterceptor")
    public RequestInterceptor contextRequestInterceptor() {
        return template -> {
            Charset utf8 = StandardCharsets.UTF_8;
            byte[] bodyBytes = template.requestBody().asBytes();
            if (bodyBytes == null) {
                return;
            }
            //只做json的请求添加参数
            boolean json = this.isJsonContentType(template.headers());
            if (!json) {
                return;
            }
            String bodyStr = new String(bodyBytes, utf8);
            SessionContext sessionContext = this.getSessionContext();
            if (sessionContext == null) {
                return;
            }

            Long firmId = sessionContext.getUserTicket().getFirmId();
            JSONObject jsonObject = JSON.parseObject(bodyStr);
            jsonObject.putIfAbsent("marketId", firmId);
            Request.Body mutatedBody = Request.Body.encoded(jsonObject.toJSONString().getBytes(utf8), utf8);
            template.body(mutatedBody);
        };
    }

    private SessionContext getSessionContext() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        Object attribute = requestAttributes.getAttribute(Constant.SESSION, RequestAttributes.SCOPE_REQUEST);
        if (!(attribute instanceof SessionContext)) {
            return null;
        }
        return (SessionContext) attribute;
    }

    private boolean isJsonContentType(Map<String, Collection<String>> headers) {
        Collection<String> contentTypes = headers.get("Content-Type");
        if (CollectionUtils.isEmpty(contentTypes)) {
            return false;
        }
        return contentTypes.stream().anyMatch(s -> s.contains("json"));
    }

}

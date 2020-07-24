package com.dili.card.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.Constant;
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

            //兼容性
            try {
                Request.Body mutatedBody;
                Long firmId = sessionContext.getUserTicket().getFirmId();
                if (this.isArrayJson(bodyStr)){
                    JSONArray array = JSON.parseArray(bodyStr);
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        jsonObject.putIfAbsent("marketId", firmId);
                        jsonObject.putIfAbsent("firmId", firmId);
                    }
                    mutatedBody = Request.Body.encoded(array.toJSONString().getBytes(utf8), utf8);
                }else {
                    JSONObject jsonObject = JSON.parseObject(bodyStr);
                    //服务的市场字段名不一样
                    jsonObject.putIfAbsent("marketId", firmId);
                    jsonObject.putIfAbsent("firmId", firmId);
                    mutatedBody = Request.Body.encoded(jsonObject.toJSONString().getBytes(utf8), utf8);
                }
                template.body(mutatedBody);
            } catch (Exception e) {
                LOGGER.error("设置市场id异常:", e);
            }
        };
    }

    private SessionContext getSessionContext() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        Object attribute = requestAttributes.getAttribute(Constant.SESSION, RequestAttributes.SCOPE_SESSION);
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

    private boolean isArrayJson(String json){
        return json.startsWith("[") && json.endsWith("]");
    }
}

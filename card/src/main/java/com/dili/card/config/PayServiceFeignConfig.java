package com.dili.card.config;

import com.dili.card.common.constant.Constant;
import com.dili.feign.support.UapCookieUtils;
import feign.RequestInterceptor;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
            Long firmId = UapCookieUtils.getFirmId();
            if (firmId == null) {
                return;
            }
            // 设置有主子商户的市场的收益帐户,各业务通过request设置
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            Object incomeAccountId = request.getAttribute(Constant.CARD_INCOME_ACCOUNT);
            if(incomeAccountId != null) {
            	log.info("设置支付收益账户>{}", incomeAccountId);
            	template.header("mchId", incomeAccountId+"");
//            	request.setAttribute(Constant.CARD_INCOME_ACCOUNT,null);
            }else {
            	template.header("mchId", firmId + "");
            }
        };
    }
//    
//    @Bean
//    public RequestInterceptor requestInterceptor() {
//        RequestInterceptor requestInterceptor = (requestTemplate) -> {
//            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
//                    .getRequestAttributes();
//            HttpServletRequest request = attributes.getRequest();
//            Enumeration<String> headerNames = request.getHeaderNames();
//            try {
//                if (headerNames != null) {
//                    while (headerNames.hasMoreElements()) {
//                        String name = headerNames.nextElement();
//                        String values = request.getHeader(name);
//                        requestTemplate.header(name, values);
//                    }
//                }
//				logger.info("接口路径："+request.getRequestURL().toString());
//                StringBuffer body = new StringBuffer();
//				Enumeration<String> bodyNames = request.getParameterNames();
//				if (bodyNames != null) {
//					Map map=new HashMap();
//					while (bodyNames.hasMoreElements()) {
//						String name = bodyNames.nextElement();
//						String values = request.getParameter(name);
//						requestTemplate.query(name, values);
//						map.put(name,values);
//					}
//					logger.info("传入参数："+map);
//				}
//				} catch (Exception e) {
//                e.printStackTrace();
//            }
//        };
//        return requestInterceptor;
//    }
}

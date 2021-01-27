package com.dili;

import com.dili.ss.dto.DTOScan;
import com.dili.ss.retrofitful.annotation.RestfulScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 由MyBatis Generator工具自动生成
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.dili.card.dao", "com.dili.ss.dao"})
@ComponentScan(basePackages={"com.dili.ss","com.dili.card", "com.dili.uap.sdk", "com.dili.commons","com.dili.logger.sdk"})
@RestfulScan({"com.dili.uap.sdk.rpc"})
@DTOScan(value={"com.dili.ss", "com.dili.card.domain"})
@EnableDiscoveryClient
@EnableFeignClients({"com.dili","com.diligrp.message"})
@EnableScheduling
@RefreshScope
@EnableAsync
public class Application extends SpringBootServletInitializer {

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}

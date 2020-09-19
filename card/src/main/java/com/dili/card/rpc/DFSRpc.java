package com.dili.card.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.dili.ss.domain.BaseOutput;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;

/**
 * 文件上传
 */
@FeignClient(name = "dili-dfs", configuration = DFSRpc.FileConfiguration.class)
public interface DFSRpc {

    /**
     * 文件上传
     */
    @PostMapping(value = "/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    BaseOutput<String> upload(@RequestPart("file") MultipartFile multipartFile, @RequestParam("accesstoken") String accesstoken);
    
    class FileConfiguration{

        @Bean
        public Encoder encoder(){
            return new SpringFormEncoder();
        }
    }
}

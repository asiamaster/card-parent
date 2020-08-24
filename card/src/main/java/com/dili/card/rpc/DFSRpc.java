package com.dili.card.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.dili.ss.domain.BaseOutput;

/**
 * 文件上传
 */
@FeignClient(name = "dili-dfs")
public interface DFSRpc {

    /**
     * 根据业务类型获取业务号
     * @param type
     * @return
     */
    @PostMapping(value = "/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    BaseOutput<String> upload(@RequestPart("file") MultipartFile multipartFile, @RequestParam("accesstoken") String accesstoken);
}

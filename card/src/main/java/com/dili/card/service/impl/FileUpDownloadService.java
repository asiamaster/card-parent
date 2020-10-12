package com.dili.card.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dili.card.common.constant.ServiceName;
import com.dili.card.config.DFSProperties;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.DFSRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.IFileUpDownloadService;
import com.dili.ss.constant.ResultCode;

import feign.Response;

@Service
public class FileUpDownloadService implements IFileUpDownloadService{
	
	private static final Logger log = LoggerFactory.getLogger(FileUpDownloadService.class);

	
	@Autowired
	private DFSRpc dfsRpc;
	@Autowired
	private DFSProperties dfsProperties;
	
	@Override
	public String upload(MultipartFile multipartFile) {
		return GenericRpcResolver.resolver(dfsRpc.upload(multipartFile, dfsProperties.getAccessToken()), ServiceName.DFS);
	}
	
	@Override
	public byte[] download(String fileId) {
		if (StringUtils.isBlank(fileId)) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "fileId missed");
		}
		Response response = null;
		InputStream inputStream = null;
		ByteArrayOutputStream swapStream = null;
		try {
			response = dfsRpc.download(fileId);
			Response.Body body = response.body();
			inputStream = body.asInputStream();
			swapStream = new ByteArrayOutputStream(); 
			byte[] buff = new byte[1024]; 
			int rc = 0; 
			while ((rc = inputStream.read(buff, 0, 1024)) > 0) {
				swapStream.write(buff, 0, rc); 
			} 
			return swapStream.toByteArray();
		} catch (Exception e) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "文件下载失败");
		}finally {
			try {
				if (swapStream != null) {
					swapStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				log.error("文件下载关闭文件流出错", e);
			}
		}
	}
}

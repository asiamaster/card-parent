package com.dili.card.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileUpDownloadService {
	/**
	 * 文件上传  返回文件唯一标识
	 */
	String upload(MultipartFile multipartFile);
	
	/**
	 * 文件下载  返回文件字节流  参数是上传文件时候的返回值
	 */
	byte[] download(String fileId);
}

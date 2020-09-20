package com.dili.card.rpc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
@FeignClient(name = "dili-dfs", configuration = DFSRpc.FeignFileUploadConfiguration.class)
public interface DFSRpc {

	/**
	 * 文件上传
	 */
	@PostMapping(value = "/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	BaseOutput<String> upload(@RequestPart("file") MultipartFile multipartFile,
			@RequestParam("accessToken") String accesstoken);

	/**
	 * 文件上传需要的这个类
	 */
	class FeignFileUploadConfiguration {

		@Bean
		public Encoder encoder() {
			return new SpringFormEncoder();
		}
	}

	/**
	 * 手动实现文件包装类
	 */
	class ByteMultipartFile implements MultipartFile {

		String name;

		String originalFilename;

		String contentType;

		byte[] bytes;

		public ByteMultipartFile() {
		}

		public ByteMultipartFile(String name, String originalFilename, String contentType, byte[] bytes) {
			this.name = name;
			this.originalFilename = originalFilename;
			this.contentType = contentType;
			this.bytes = bytes;
		}

		public static ByteMultipartFile getInstance(String name, String originalFilename, String contentType,
				byte[] bytes) {
			return new ByteMultipartFile(name, originalFilename, contentType, bytes);
		}

		@Override
		public boolean isEmpty() {
			return bytes.length == 0;
		}

		@Override
		public long getSize() {
			return bytes.length;
		}

		@Override
		public InputStream getInputStream() {
			return new ByteArrayInputStream(bytes);
		}

		@Override
		public void transferTo(File destination) throws IOException {
			OutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(destination);
				outputStream.write(bytes);
			} finally {
				if (outputStream != null) {
					outputStream.close();
				}
			}
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getOriginalFilename() {
			return originalFilename;
		}

		@Override
		public String getContentType() {
			return contentType;
		}

		@Override
		public byte[] getBytes() throws IOException {
			return bytes;
		}

	}
}

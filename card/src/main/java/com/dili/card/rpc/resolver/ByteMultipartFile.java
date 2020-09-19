package com.dili.card.rpc.resolver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传类
 */
public class ByteMultipartFile implements MultipartFile {
	
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

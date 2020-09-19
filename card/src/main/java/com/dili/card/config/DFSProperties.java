package com.dili.card.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * 文件相关参数
 */
@Component
@ConfigurationProperties(prefix = "file.dfs")
public class DFSProperties {
	
	private String accessToken;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
}

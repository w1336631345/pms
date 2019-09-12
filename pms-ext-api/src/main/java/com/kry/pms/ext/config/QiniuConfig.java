package com.kry.pms.ext.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.qiniu.util.Auth;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "qiniu.oss")
@Getter
@Setter
public class QiniuConfig {

	private String accessKey;

	private String secretKey;

	private String bucket;

	private String path;

	private String imageHost;

	public String uploadToken() {
		return Auth.create(accessKey, secretKey).uploadToken(bucket);
	}

	public String accessToken(String baseUrl) {
		return Auth.create(accessKey, secretKey).privateDownloadUrl(baseUrl);
	}
}
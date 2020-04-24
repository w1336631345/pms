package com.kry.pms.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aliyun.sms")
@Getter
@Setter
public class AliyunSmsConfig {
    private String accessKeyId;
    private String accessSecret;
    private String action;
    private String regionId;
    private String version;
    private String domain;
}

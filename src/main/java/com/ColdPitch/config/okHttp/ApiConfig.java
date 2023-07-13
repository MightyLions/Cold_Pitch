package com.ColdPitch.config.okHttp;

import com.ColdPitch.core.factory.YamlLoadFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * api key 관리를 위한 파일
 */
@Configuration
@PropertySource(value = {"classpath:secret.yml"}, factory = YamlLoadFactory.class)
@ConfigurationProperties(prefix = "api")
@Getter
@Setter
@ToString
@Component
public class ApiConfig {
    private String companyServiceKey;
    private String sentimentKeyId;
    private String sentimentKey;
}

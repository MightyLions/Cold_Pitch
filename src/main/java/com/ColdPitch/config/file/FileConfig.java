package com.ColdPitch.config.file;

import com.ColdPitch.core.factory.YamlLoadFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "file")
@PropertySource(value = {"file.yml"}, factory = YamlLoadFactory.class)
@Getter
@Setter
public class FileConfig {
    private String path;

}

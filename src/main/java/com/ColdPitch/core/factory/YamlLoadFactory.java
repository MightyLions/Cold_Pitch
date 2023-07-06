package com.ColdPitch.core.factory;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Properties;

public class YamlLoadFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        YamlPropertiesFactoryBean ypfb = new YamlPropertiesFactoryBean();
        ypfb.setResources(resource.getResource());

        Properties properties = ypfb.getObject();

        return new PropertiesPropertySource(resource.getResource().getFilename(), properties);
    }
}

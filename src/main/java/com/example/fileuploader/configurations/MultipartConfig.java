package com.example.fileuploader.configurations;

import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

@Configuration
@EnableConfigurationProperties(MultipartProperties.class)
public class MultipartConfig {
    private final MultipartProperties multipartProperties;

    public MultipartConfig(MultipartProperties multipartProperties) {
        this.multipartProperties = multipartProperties;
    }

    @Bean
    public MultipartElementConfig multipartConfigElement() {
        MultipartConfigElement multipartConfigElement = multipartProperties.createMultipartConfig();
        return new MultipartElementConfig(multipartConfigElement.getLocation(), multipartConfigElement.getMaxFileSize(),
                multipartConfigElement.getMaxRequestSize(), multipartConfigElement.getFileSizeThreshold());
    }
}

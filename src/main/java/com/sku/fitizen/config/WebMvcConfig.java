package com.sku.fitizen.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 컨테이너 내부 디렉토리 매핑
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:/app/files/");
    }
}
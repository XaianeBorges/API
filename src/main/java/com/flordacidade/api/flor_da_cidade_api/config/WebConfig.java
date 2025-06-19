package com.flordacidade.api.flor_da_cidade_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.curso.dir:uploads/cursos}")
    private String cursoUploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resourcePath = "file:" + cursoUploadDir.replace("../", "") + "/";
        registry.addResourceHandler("/uploads/cursos/**")
                .addResourceLocations(resourcePath);
    }
}
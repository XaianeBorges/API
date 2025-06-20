package com.flordacidade.api.flor_da_cidade_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins:http://localhost:5173}")
    private String allowedOrigins;

    // Esta variável agora irá carregar o caminho './uploads/banners'
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapeia a URL exata que o front-end está pedindo...
        registry.addResourceHandler("/uploads/banners/**")
                // ...para a pasta exata definida em 'file.upload-dir'.
                // O "file:" é crucial para indicar que é um caminho no sistema de arquivos.
                .addResourceLocations("file:" + uploadDir + "/");
    }
}
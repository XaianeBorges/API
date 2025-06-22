package com.flordacidade.api.flor_da_cidade_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Carrega o valor de 'file.upload-dir' do application.properties.
    // Atualmente: ./uploads/banners
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Quando uma requisição chegar para /uploads/banners/QUALQUER_COISA...
        registry.addResourceHandler("/uploads/banners/**")
                // Sirva os arquivos da pasta definida em 'uploadDir'.
                // O "file:" indica que é um caminho no sistema de arquivos.
                // O "/" no final de uploadDir + "/" garante que é um diretório.
                .addResourceLocations("file:" + uploadDir + "/");
    }
}
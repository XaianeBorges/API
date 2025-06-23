package com.flordacidade.api.flor_da_cidade_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir.banners}")
    private String bannerUploadDir;

    @Value("${file.upload-dir.hortas}")
    private String hortaImageUploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resolvedBannerPath = Paths.get(bannerUploadDir).toAbsolutePath().normalize().toString();
        if (!resolvedBannerPath.endsWith("/")) {
            resolvedBannerPath += "/";
        }
        registry.addResourceHandler("/uploads/banners/**")
                .addResourceLocations("file:" + resolvedBannerPath);

        String resolvedHortaImagePath = Paths.get(hortaImageUploadDir).toAbsolutePath().normalize().toString();
        if (!resolvedHortaImagePath.endsWith("/")) {
            resolvedHortaImagePath += "/";
        }
        registry.addResourceHandler("/uploads/imagem/**")
                .addResourceLocations("file:" + resolvedHortaImagePath);
    }
}
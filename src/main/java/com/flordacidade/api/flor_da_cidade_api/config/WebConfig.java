package com.flordacidade.api.flor_da_cidade_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Carrega o valor de 'file.upload-dir.banners' do application.properties.
    @Value("${file.upload-dir.banners}")
    private String bannerUploadDir;

    // Carrega o valor de 'file.upload-dir.hortas' do application.properties.
    // (Lembre-se que em application.properties está como ./uploads/imagem)
    @Value("${file.upload-dir.hortas}")
    private String hortaImageUploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Normaliza os caminhos para garantir que terminem com '/' e sejam absolutos
        // para o resource location. O prefixo "file:" é crucial.

        // Para Banners
        String resolvedBannerPath = Paths.get(bannerUploadDir).toAbsolutePath().normalize().toString();
        // Garante que o caminho do resource location termine com uma barra
        if (!resolvedBannerPath.endsWith("/")) {
            resolvedBannerPath += "/";
        }

        // Quando uma requisição chegar para /uploads/banners/QUALQUER_COISA...
        registry.addResourceHandler("/uploads/banners/**")
                // Sirva os arquivos da pasta definida em 'bannerUploadDir'.
                .addResourceLocations("file:" + resolvedBannerPath);

        // Para Imagens de Hortas (que estão em ./uploads/imagem fisicamente)
        String resolvedHortaImagePath = Paths.get(hortaImageUploadDir).toAbsolutePath().normalize().toString();
        // Garante que o caminho do resource location termine com uma barra
        if (!resolvedHortaImagePath.endsWith("/")) {
            resolvedHortaImagePath += "/";
        }

        // Quando uma requisição chegar para /uploads/imagem/QUALQUER_COISA...
        // Este é o URL pelo qual o frontend vai acessar as imagens das hortas.
        registry.addResourceHandler("/uploads/imagem/**")
                // Sirva os arquivos da pasta definida em 'hortaImageUploadDir'.
                .addResourceLocations("file:" + resolvedHortaImagePath);
    }
}
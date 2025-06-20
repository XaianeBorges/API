
package com.flordacidade.api.flor_da_cidade_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Injeta a URL permitida pelo CORS a partir do application.properties
    @Value("${cors.allowed-origins:http://localhost:5173}")
    private String allowedOrigins;

    // Injeta o diretório raiz de uploads a partir do application.properties.
    // Se a propriedade não for encontrada, usa './uploads' como valor padrão.
    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    /**
     * Configura o CORS (Cross-Origin Resource Sharing) para a aplicação.
     * Permite que o front-end acesse este back-end.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica a todos os endpoints
                .allowedOrigins(allowedOrigins) // Usa a URL definida no .properties
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }

    /**
     * Configura o mapeamento de recursos estáticos (imagens, etc.).
     * Isso permite que os arquivos salvos no disco sejam acessíveis via URL.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapeia qualquer requisição que comece com /uploads/**...
        registry.addResourceHandler("/uploads/**")
                // ...para a pasta definida em 'file.upload-dir' no sistema de arquivos.
                .addResourceLocations("file:" + uploadDir + "/");
    }
}

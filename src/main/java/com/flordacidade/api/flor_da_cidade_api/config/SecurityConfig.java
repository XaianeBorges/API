package com.flordacidade.api.flor_da_cidade_api.config;

// --- IMPORTS NECESSÁRIOS ---
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;
// --- FIM DOS IMPORTS ---

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Configuração de CORS para permitir requisições do front-end
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    // A URL do seu front-end Vite
                    configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://127.0.0.1:5173"));
                    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    configuration.setAllowedHeaders(List.of("*"));
                    configuration.setAllowCredentials(true);
                    return configuration;
                }))
                // Desabilita CSRF, pois estamos construindo uma API REST
                .csrf(AbstractHttpConfigurer::disable)
                // Configura as regras de autorização para os endpoints
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso PÚBLICO ao endpoint de login
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        // Permite acesso PÚBLICO a todos os endpoints de técnico
                        .requestMatchers("/api/tecnicos/**").permitAll()
                        // Por simplicidade, permite todas as outras requisições por enquanto
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
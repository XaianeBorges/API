package com.flordacidade.api.flor_da_cidade_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable) // Desabilita CSRF, comum para APIs stateless com tokens
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints públicos existentes
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .requestMatchers("/api/tecnicos/**").permitAll()
                        .requestMatchers("/api/cursos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/hortas/solicitacoes/pendentes").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/hortas/status/**").permitAll()
                        .requestMatchers("/uploads/banners/**").permitAll()

                        // --- PERMISSÃO TEMPORÁRIA PARA DESENVOLVIMENTO ---
                        // Permite o método PATCH para o endpoint de alteração de status de hortas
                        // sem autenticação. REMOVA OU PROTEJA ADEQUADAMENTE PARA PRODUÇÃO.
                        .requestMatchers(HttpMethod.PATCH, "/api/hortas/{id}/status").permitAll()
                        // Se você tiver outros endpoints PATCH ou POST para /api/hortas que também
                        // precisam ser testados sem token agora, adicione-os aqui temporariamente.
                        // Ex: .requestMatchers(HttpMethod.POST, "/api/hortas").permitAll()

                        // Todas as outras requisições não listadas acima exigirão autenticação
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // URLs permitidas para o frontend
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173", // Web Emprel (se existir)
                "http://localhost:5175"  // Web Admin
        ));
        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        // Cabeçalhos permitidos (usar "*" é amplo, mas comum para desenvolvimento)
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // Permitir credenciais (cookies, authorization headers)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuração CORS para todas as rotas da API (/**)
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
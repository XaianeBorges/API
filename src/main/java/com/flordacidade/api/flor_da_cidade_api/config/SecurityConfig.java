// Caminho do arquivo: com/flordacidade/api/flor_da_cidade_api/config/SecurityConfig.java

package com.flordacidade.api.flor_da_cidade_api.config;

import com.flordacidade.api.flor_da_cidade_api.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder; // TEMPORÁRIO
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/logout").permitAll()
                        
                        // Rotas para recursos estáticos e uploads
                        .requestMatchers("/uploads/banners/**").permitAll()
                        .requestMatchers("/uploads/imagem/**").permitAll()

                        // Rotas públicas de consulta (GET)
                        .requestMatchers(HttpMethod.GET, "/api/hortas/public/ativas").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/regioes").permitAll()
                        .requestMatchers("/api/cursos/**").permitAll() // Permite todos os métodos para /cursos
                        .requestMatchers(HttpMethod.GET, "/api/hortas").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/hortas/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/hortas/solicitacoes/pendentes").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/hortas/status/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/hortas/download").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/hortas/tipo").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/hortas/unidade-ensino").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/hortas/areas-classificacao").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/hortas/atividades-produtivas").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/usuarios").permitAll()

                        // =======================================================================
                        // INÍCIO DA ALTERAÇÃO: Permissão para o fluxo de inscrição pública
                        .requestMatchers(HttpMethod.POST, "/api/pessoas").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/inscricoes").permitAll()
                        // FIM DA ALTERAÇÃO
                        // =======================================================================

                        // Rotas que exigem autenticação
                        .requestMatchers(HttpMethod.POST, "/api/hortas").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/hortas/{id}").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/hortas/{id}/status").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/hortas/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/tecnicos").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/tecnicos/{id}").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/tecnicos").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/tecnicos/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/tecnicos/{id}").authenticated()
                        
                        // Regra geral: todas as outras rotas da API devem ser autenticadas
                        .requestMatchers("/api/**").authenticated()

                        // Permite qualquer outra requisição que não se encaixe nas regras acima (ex: páginas estáticas)
                        .anyRequest().permitAll());
                        
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173", "http://localhost:5175", "http://localhost:5174"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
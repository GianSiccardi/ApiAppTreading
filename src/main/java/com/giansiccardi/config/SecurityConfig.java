package com.giansiccardi.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.sessionManagement(managment->managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(Authorize->Authorize.requestMatchers("/auth/register","/auth/login").permitAll()
                    .anyRequest()
                    .authenticated()
            )
            .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
            .csrf(csrf->csrf.disable())
            .cors(cors->cors.configurationSource(corsConfigurationSource()));
    return httpSecurity.build();
}


    private CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration cfg = new CorsConfiguration();
            cfg.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:3000")); // Orígenes permitidos
            cfg.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Métodos permitidos
            cfg.setAllowCredentials(true); // Permitir el uso de credenciales (cookies, autorizaciones)
            cfg.setAllowedHeaders(Collections.singletonList("*")); // Permitir todos los encabezados
            cfg.setExposedHeaders(Arrays.asList("Authorization")); // Exponer ciertos encabezados
            cfg.setMaxAge(3600L); // Cachear la respuesta de CORS por 1 hora
            return cfg;
        };
    }
}



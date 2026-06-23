package com.app.confeitaria.docelivery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Origens permitidas — React Web + Emulador Android + Expo Go
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://localhost:5175",
                "http://localhost:5176",
                "http://localhost:8081",
                "http://127.0.0.1:8081",
                "http://10.0.2.2:8080",   // emulador Android → localhost da máquina
                "http://10.0.2.2:8081",
                "http://localhost:19000", // Expo Go
                "http://localhost:19006"
        ));
        configuration.setAllowedOriginPatterns(Arrays.asList("*")); // fallback para dispositivos físicos

        // Métodos permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // Cabeçalhos permitidos (Incluindo o x-user-id que causou o erro)
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Cache-Control",
                "x-user-id"
        ));

        // Permitir envio de credenciais (Cookies/Auth Headers)
        configuration.setAllowCredentials(true);

        // Permite que o Front-end leia cabeçalhos específicos na resposta
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
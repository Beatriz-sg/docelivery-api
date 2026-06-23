package com.app.confeitaria.docelivery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Define o caminho físico da pasta no seu Windows
        String uploadDir = "C:/docelivery-storage/";

        // Converte para o formato que o Spring entende (file:///C:/...)
        String absolutePath = Paths.get(uploadDir).toUri().toString();

        // 1. Mantém o mapeamento existente de uploads
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(absolutePath)
                .setCachePeriod(0);

        // 2. Mapeia /imagens/**
        registry.addResourceHandler("/imagens/**")
                .addResourceLocations(absolutePath)
                .setCachePeriod(0);

        // 3. Mapeia /uploads/fotos/** para a subpasta fotos
        String fotoDir = Paths.get(uploadDir, "fotos").toUri().toString();
        registry.addResourceHandler("/uploads/fotos/**")
                .addResourceLocations(fotoDir)
                .setCachePeriod(0);

        System.out.println("LOG DOCELIVERY: Servindo arquivos de: " + absolutePath);
        System.out.println("LOG DOCELIVERY: Servindo fotos de: " + fotoDir);
    }
}
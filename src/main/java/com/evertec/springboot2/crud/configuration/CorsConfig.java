package com.evertec.springboot2.crud.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @SuppressWarnings("null")
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/tareas/**")
                .allowedOrigins("https://ambitious-ocean-092d6240f.5.azurestaticapps.net")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
        
        registry.addMapping("/auth")
                .allowedOrigins("https://ambitious-ocean-092d6240f.5.azurestaticapps.net")
                .allowedMethods("POST")
                .allowedHeaders("*")
                .allowCredentials(true);
        
        registry.addMapping("/validateAuth")
                .allowedOrigins("https://ambitious-ocean-092d6240f.5.azurestaticapps.net")
                .allowedMethods("POST")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

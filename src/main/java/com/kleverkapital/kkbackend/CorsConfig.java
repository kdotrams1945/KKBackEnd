package com.kleverkapital.kkbackend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
               // https://kkbackend-production-d38e.up.railway.app/swagger-ui/index.html#/bmi-calculator/calculateBMI
                registry.addMapping("/**") // Adjust if needed
                        .allowedOrigins("https://kk-dashboard-production.up.railway.app/",
                                "http://kk-dashboard-production.up.railway.app/")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
package com.example.integratedbackend;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.TimeZone;

@SpringBootApplication
public class IntegratedBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntegratedBackendApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/v1/**").allowedOrigins("http://localhost", "http://ip23nw2.sit.kmutt.ac.th");
                registry.addMapping("/v2/**").allowedOrigins("http://localhost", "http://ip23nw2.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th");
            }
        };
    }

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }


}

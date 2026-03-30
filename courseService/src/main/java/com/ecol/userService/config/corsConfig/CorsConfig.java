package com.ecol.userService.config.corsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import java.util.Arrays;
import java.util.List;
@Configuration
public class CorsConfig {
    @Value("${frontend.allowed-domains}")
    private String allowedDomains;

    public void addCorsMappings(CorsRegistry registry) {
        List<String> origins = Arrays.stream(allowedDomains.split(","))
                .map(String::trim)
                .toList();
        registry.addMapping("/**")
                .allowedOrigins(origins.toArray(new String[0]))
                .allowedMethods("POST", "GET", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}

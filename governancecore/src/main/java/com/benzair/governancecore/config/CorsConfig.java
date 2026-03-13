package com.benzair.governancecore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // The following line means: apply these CORS rules to all endpoints starting with /api/.
        registry.addMapping("/api/**")
                // This means: only allow requests coming from this frontend.
                .allowedOrigins("http://localhost:5173")
                // This allows the frontend to call the API using these methods.
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // The frontend can send any headers.
                .allowedHeaders("*");
    }
}

/*
Why do I need this configuration?

Because ports differ:

5173 (frontend) ≠ 8080 (backend)

the browser treats it as cross-origin.
Hence, we must configure CORS.
*/
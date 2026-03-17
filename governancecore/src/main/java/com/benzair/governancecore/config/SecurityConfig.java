package com.benzair.governancecore.config;

import org.checkerframework.checker.units.qual.A;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

// This class tells Spring Security how to protect our API and how to check users using tokens (JWT).
// This class is like the security guard at the entrance of a building, checking IDs and deciding who can enter which rooms.
@Configuration // This annotation tells Spring that this class contains configuration settings.
@EnableWebSecurity // This annotation enables Spring Security's web security features like authentication and authorization.
@EnableMethodSecurity // This annotation allows us to use method-level security annotations like @PreAuthorize in our controllers and services.
public class SecurityConfig {

    // SecurityFilterChain: A list of security steps that every request must go through.
    // A pipeline of checks before a request reaches the controller.

    // HttpSecurity: A builder (tool) used to define all your security rules.
    // This is where I configure security behavior.
    @Bean // This annotation tells Spring to create and manage this bean (object) in the application context, making it available for dependency injection.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // http.sessionManagement(...) Tells Spring:
                // Do NOT remember users using sessions.
                // Meaning:
                // no login stored on the server
                // every request must bring its token
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Let Spring Security reuse the CORS configuration already defined in CorsConfig.
                .cors(Customizer.withDefaults()) 
                // Disable CSRF protection since we are not using cookies for authentication.
                .csrf(csrf -> csrf.disable())
                // Define which routes require authentication and which don't.
                .authorizeHttpRequests(auth -> auth // "The request is trying to access a protected route"
                        // "Spring Security checks the route and applies the corresponding rule"
                        // Only authenticated users can create, update, delete assets and access their profile.
                        .requestMatchers(HttpMethod.GET, "/api/v1/assets/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/assets/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/assets/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/assets/**").authenticated()
                        .requestMatchers("/api/v1/auth/me").authenticated()
                        .anyRequest().permitAll()) // All other routes are public and can be accessed without authentication.
                // Tell Spring Security to use JWT tokens for authentication.
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        // Build the security filter chain and return it to Spring Security.
        return http.build();
    }
}

/*
The flow:
So in practice:

the request arrives with a token.
Spring Security checks whether that token is valid.
if valid, the user is treated as authenticated.
then Spring Security applies the access rules from SecurityConfig.
if the request matches a protected route, the user is either allowed or blocked.
*/

// In projects, we usually use the SecurityConfig class to define the security rules for our API, while the JwtAuthenticationFilter class is responsible for checking the validity of the JWT token in each request.
package com.benzair.governancecore.config;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

// This class tells Spring Security how to protect our API and how to interpret
// the roles that arrive inside JWT access tokens.
@Configuration // This annotation tells Spring that this class contains configuration settings.
@EnableWebSecurity // This tells Spring to use the configuration in this class to set up web security for the application.
@EnableMethodSecurity // This annotation allows us to use method-level security annotations like @PreAuthorize later.
public class SecurityConfig {

    // These are the business roles our application actually cares about.
    // We ignore technical Keycloak roles like offline_access and uma_authorization.
    private static final Set<String> BUSINESS_ROLES = Set.of("ADMIN", "ANALYST", "AUDITOR");

    // SecurityFilterChain: A list of security steps that every request must go through.
    // HttpSecurity: The builder used to define those security rules.
    @Bean // This annotation tells Spring to create and manage this bean in the application context.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Do NOT remember users using server-side sessions.
                // Every request must bring its own JWT token.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Reuse the CORS configuration already defined in CorsConfig.
                .cors(cors -> {})
                // Disable CSRF because this API is using bearer tokens, not cookie-based login.
                .csrf(csrf -> csrf.disable())
                // Define who can access which routes.
                .authorizeHttpRequests(auth -> auth
                        // User must be logged in and must have one of these roles.
                        .requestMatchers(HttpMethod.GET, "/api/v1/assets/**").hasAnyRole("ADMIN", "ANALYST", "AUDITOR")
                        // Only ADMIN and ANALYST can create, update, or delete assets.
                        .requestMatchers(HttpMethod.POST, "/api/v1/assets/**").hasAnyRole("ADMIN", "ANALYST")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/assets/**").hasAnyRole("ADMIN", "ANALYST")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/assets/**").hasAnyRole("ADMIN", "ANALYST")
                        // The /auth/me endpoint is protected and requires a valid authenticated user.
                        .requestMatchers("/api/v1/auth/me").authenticated()
                        // Any route not listed above stays public for now.
                        .anyRequest().permitAll())
                // Tell Spring Security to use JWT tokens and our custom role-mapping logic.
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        // Build the security filter chain and return it to Spring Security.
        return http.build();
    }

    // JwtAuthenticationConverter is the translator between the JWT and Spring Security.
    // It tells Spring how to extract authorities from the token.
    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::extractRealmRoles);
        return converter;
    }

    // This method reads the "realm_access.roles" claim from the JWT,
    // keeps only our business roles, and converts them into Spring Security authorities.
    private Collection<GrantedAuthority> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null || realmAccess.get("roles") == null) {
            return List.of();
        }

        Object rolesClaim = realmAccess.get("roles");
        // We only continue if the claim really is a collection of values.
        if (!(rolesClaim instanceof Collection<?> roles)) {
            return List.of();
        }

        return roles.stream()
                .filter(String.class::isInstance) // Keep only items that are strings.
                .map(String.class::cast) // Convert those items into String values.
                .filter(BUSINESS_ROLES::contains) // Keep only ADMIN, ANALYST, and AUDITOR.
                .map(role -> "ROLE_" + role) // Prefix with ROLE_ to match Spring Security conventions.
                .map(SimpleGrantedAuthority::new) // Turn each role into a GrantedAuthority object.
                .collect(Collectors.toList());
    }
}

/*
The flow:
1. The request arrives with a JWT token.
2. Spring Security validates whether that token is authentic and trusted.
3. Our converter extracts the business roles from realm_access.roles.
4. Spring Security turns those roles into authorities like ROLE_ANALYST.
5. The access rules above decide whether the request is allowed or denied.
*/

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

@Configuration
@EnableWebSecurity // this annotation enables Spring Security's web security support and provides the Spring MVC integration.
@EnableMethodSecurity // this annotation enables method-level security annotations like @PreAuthorize.
public class SecurityConfig {

    // Define the set of business roles that we want to extract from the JWT token and use for authorization.
    private static final Set<String> BUSINESS_ROLES = Set.of("ADMIN", "ANALYST", "AUDITOR");

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                // Route authorization rules: this part says who must be logged in to access certain endpoints.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/v1/assets/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/assets/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/assets/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/assets/**").authenticated()
                        .requestMatchers("/api/v1/auth/me").authenticated()
                        .anyRequest().permitAll())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    // This bean defines how to convert a JWT token into an Authentication object that Spring Security can use for authorization decisions.
    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        // We create a JwtAuthenticationConverter and set a custom converter for extracting granted authorities from the JWT token.
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        // The custom converter is defined as a method reference to the extractRealmRoles method, which will extract the roles from the "realm_access" claim in the JWT token
        //  and convert them into Spring Security authorities.
        converter.setJwtGrantedAuthoritiesConverter(this::extractRealmRoles);
        // Finally, we return the configured converter.
        return converter;
    }

    // This method extracts the roles from the "realm_access" claim in the JWT token
    // and converts them into Spring Security authorities.
    private Collection<GrantedAuthority> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null || realmAccess.get("roles") == null) {
            return List.of();
        }
        // We check if the "roles" claim is a collection of strings.
        // If not, we return an empty list of authorities.
        Object rolesClaim = realmAccess.get("roles");
        if (!(rolesClaim instanceof Collection<?> roles)) {
            return List.of();
        }
        // We filter the roles to include only those that are in our defined set of business roles,
        // and we prefix them with "ROLE_" to conform to Spring Security's convention for roles.
        return roles.stream() // Start looping through the roles one by one.
                .filter(String.class::isInstance) // Keep only items that are strings.
                .map(String.class::cast) // Convert the remaining items to strings.
                .filter(BUSINESS_ROLES::contains) // Keep only roles that we defined above in the BUSINESS_ROLES set.
                .map(role -> "ROLE_" + role) // Prefix the role with "ROLE_" to follow Spring Security's convention for role names.
                .map(SimpleGrantedAuthority::new) // Convert the role string into a SimpleGrantedAuthority object, which is what Spring Security uses to represent authorities.
                .collect(Collectors.toList()); // Collect the resulting stream of authorities into a list and return it.
    }
}

// Basically, this code configures Spring Security to use JWT tokens for authentication and authorization.
// It defines which endpoints require authentication
// and how to extract roles from the JWT token to determine the user's authorities.
// The roles are prefixed with "ROLE_" to conform to Spring Security's convention.
package com.benzair.governancecore.authsubdomain.presentationlayer;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// This controller provides an endpoint to get information about the currently authenticated user based on the JWT token they present.

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @GetMapping("/me") // This endpoint will return the details of the currently authenticated user.
    public AuthUserResponseModel getCurrentUser(Authentication authentication) {
        // We extract the JWT token from the Authentication object.
        // The JwtAuthenticationConverter we defined in SecurityConfig will have already processed the token
        // and set it as the principal of the Authentication object.
        Jwt jwt = (Jwt) authentication.getPrincipal();

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.replace("ROLE_", ""))
                .toList();

        return AuthUserResponseModel.builder()
                .username(jwt.getClaimAsString("preferred_username"))
                .email(jwt.getClaimAsString("email"))
                .roles(roles)
                .build();
    }
}

// How the controller works:

// Authentication authentication:
// Spring injects the authenticated request context

// Jwt jwt = (Jwt) authentication.getPrincipal():
// gets the decoded token object

// authentication.getAuthorities():
// gets the roles after your JWT converter already mapped them

// it strips the ROLE_ prefix before returning them to the frontend
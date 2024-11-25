package com.ideas2it.sample.infrastructure.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

public class JwtHelper {

    public static List<String> extractRolesFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
        }
        throw new RuntimeException("Unable to retrieve roles from the security context");
    }

    public static boolean isAdmin() {
        List<String> roles = extractRolesFromToken();
        return roles.contains("ADMIN");
    }

    public static boolean isUser() {
        List<String> roles = extractRolesFromToken();
        return roles.contains("USER");
    }
}

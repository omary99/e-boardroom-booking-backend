package com.boardroom.boardroom_booking.Configuration.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserUtil {

    public CurrentUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) return null;

        Object principal = authentication.getPrincipal();

        // If your jwtFilter sets CustomUserDetails as principal
        if (principal instanceof CustomUserDetails customUser) {
            CurrentUser user = new CurrentUser();
            user.setUserId(customUser.getUser().getId());
            user.setUsername(customUser.getUsername());
            user.setEmail(customUser.getUser().getEmail());
            user.setFirstName(customUser.getUser().getFirstName());
            //user.setLastName(customUser.getUser().getLastName());
            user.setRoles(customUser.getAuthorities().toString());
            //user.setCurrentLocation(customUser.getUser().getCurrentLocation() != null ? customUser.getUser().getCurrentLocation().getId() : null);
            return user;
        }

        return null;
    }

    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            System.out.println("token again : "+jwtAuth.getToken());
            return jwt.getClaimAsString("userId");  // <-- replace with your exact claim key
        }

        return null;
    }

    private Jwt getJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthToken) {
            return jwtAuthToken.getToken();
        }
        return null;
    }
}

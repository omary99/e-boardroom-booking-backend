package com.boardroom.boardroom_booking.Configuration.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static final String SECRET = "ThisIsA32ByteLongSecretKeyForJWT123!";  // Use a strong, secure key
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    private static final long ACCESS_EXPIRATION_TIME = 30 * 60 * 1000; // 5 minutes
    private static final long REFRESH_EXPIRATION_TIME = 3600 * 1000; // 1 hour

    // Generate token with minimal claims (for size optimization)
    public String generateToken(CustomUserDetails userDetails, boolean isRefreshToken) {
        long expirationTime = isRefreshToken ? REFRESH_EXPIRATION_TIME : ACCESS_EXPIRATION_TIME;
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", roles)
                .claim("userId",userDetails.getUser().getId())
                .claim("email",userDetails.getUser().getEmail())
                //.claim("currentLocation",userDetails.getUser().getCurrentLocation() != null ? userDetails.getUser().getCurrentLocation().getId() : null)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_TIME))  // 1 hour expiry
                .signWith(SECRET_KEY)
                .compact();
    }

    // Extract roles from token
    public String extractRoles(String token) {
//        return Jwts.builder()
//                .signWith(SECRET_KEY)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .get("roles", String.class);

        return Jwts.parser()
                .verifyWith((SecretKey) SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("roles", String.class);
    }

    // Generate token using username only (Refresh case)
    public String generateTokenFromUsername(String username, String roles, boolean isRefreshToken) {
        long expirationTime = isRefreshToken ? REFRESH_EXPIRATION_TIME : ACCESS_EXPIRATION_TIME;

        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith((SecretKey) SECRET_KEY).build().parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false; // Token expired
        } catch (Exception e) {
            return false; // Invalid token
        }
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = Jwts.parser()
                .verifyWith((SecretKey) SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expiration.before(new Date());
    }

    // Get token expiration time (for response)
    public long getExpirationTime(boolean isRefreshToken) {
        return isRefreshToken ? REFRESH_EXPIRATION_TIME / 1000 : ACCESS_EXPIRATION_TIME / 1000; // Convert to seconds
    }
}

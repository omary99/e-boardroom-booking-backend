//package com.boardroom.boardroom_booking.security;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.util.Date;
//import java.util.List;
//
//@Component
//public class JwtUtil {
//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration}")
//    private long expiration;
//
//    private SecretKey getSigningKey() {
//        return Keys.hmacShaKeyFor(secret.getBytes());
//    }
//
//    // Generate token with roles
//    public String generateToken(String email, String role) {
//        return Jwts.builder()
//                .setSubject(email)
//                .claim("role", role) // store the role in the token
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + expiration))
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public String extractEmail(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//    public String extractRole(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .get("role", String.class);
//    }
//}

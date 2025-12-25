//package com.boardroom.boardroom_booking.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.List;
//
//@Component
//public class JwtFilter extends OncePerRequestFilter {
//    private final JwtUtil jwtUtil;
//
//    public JwtFilter(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            String email = null;
//
//            try {
//                email = jwtUtil.extractEmail(token);
//                System.out.println("JWT parsed successfully. Email: " + email);
//            } catch (Exception e) {
//                System.out.println("Invalid or expired JWT: " + e.getMessage());
//            }
//
//            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                // Extract role from JWT
//                String role = jwtUtil.extractRole(token);
//
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(
//                                email,
//                                null,
//                                List.of(new SimpleGrantedAuthority("ROLE_" + role))
//                        );
//
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//
//        } else {
//            System.out.println("No JWT token found in request headers.");
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}

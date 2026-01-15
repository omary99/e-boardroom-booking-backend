package com.boardroom.boardroom_booking.controller;

import com.boardroom.boardroom_booking.Configuration.security.CustomUserDetails;
import com.boardroom.boardroom_booking.Configuration.security.JwtUtil;
import com.boardroom.boardroom_booking.DTO.AuthRequest;
import com.boardroom.boardroom_booking.DTO.AuthResponse;
import com.boardroom.boardroom_booking.audit.service.AuditService;
import com.boardroom.boardroom_booking.beans.AuthResult;
import com.boardroom.boardroom_booking.model.User;
import com.boardroom.boardroom_booking.repository.UserRepository;
import com.boardroom.boardroom_booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/v1")
public class AuthController {
    private final UserRepository userRepository;

    @Autowired
    private UserService userDetails;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuditService auditService;

//    @Autowired
//    private UserRepository userDetails;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager, JwtUtil jwtUtil, AuditService auditService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.auditService = auditService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // For simplicity, not checking duplicate username here
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        System.out.println("New: "+request.getUsername());
        System.out.println("New: "+request.getPassword());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        var userDetails = new CustomUserDetails(
                userRepository.findByUsername(request.getUsername()).orElseThrow()
        );
        String token = jwtUtil.generateToken(userDetails,false);
        String refreshToken = jwtUtil.generateToken(userDetails, true);
        UserDetails user = (UserDetails) authentication.getPrincipal();
        //String roles = user.getAuthorities().toString(); // Convert roles to string
        String roles = jwtUtil.extractRoles(token);
        long expiresIn = jwtUtil.getExpirationTime(false); // Ensure this method returns the e
        System.out.println("token: "+token);
        auditService.log("USER-MANAGEMENT-SERVICE","Login","User "+user.getUsername()+" logged in",user.getUsername());
        return ResponseEntity.ok(new AuthResponse(token,refreshToken,"bearer",expiresIn, userDetails.getUsername(),roles));
    }

//    @GetMapping("/auth/login")
//    public ResponseEntity<AuthResponse> getLogin(@RequestParam String username, @RequestParam String password) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(username, password)
//        );
//
//        var userDetails = new CustomUserDetails(
//                userRepository.findByUsername(username).orElseThrow()
//        );
//        String token = jwtUtil.generateToken(userDetails,false);
//        String refreshToken = jwtUtil.generateToken(userDetails, true);
//        UserDetails user = (UserDetails) authentication.getPrincipal();
//        //String roles = user.getAuthorities().toString(); // Convert roles to string
//        String roles = jwtUtil.extractRoles(token);
//        long expiresIn = jwtUtil.getExpirationTime(false); // Ensure this method returns the e
//        System.out.println("token: "+token);
//        auditService.log("USER-MANAGEMENT-SERVICE","Login","User "+user.getUsername()+" logged in",user.getUsername());
//        return ResponseEntity.ok(new AuthResponse(token,refreshToken,"bearer",expiresIn, userDetails.getUsername(),roles));
//    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing or invalid token");
        }

        String refreshToken = authHeader.substring(7);

        if (!jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Expired or invalid refresh token");
        }

        // Extract username and generate new access token
        String username = jwtUtil.extractUsername(refreshToken);
        String roles = jwtUtil.extractRoles(refreshToken);
        String newAccessToken = jwtUtil.generateTokenFromUsername(username,roles ,true);

        return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken,"bearer", jwtUtil.getExpirationTime(false), username, roles));
    }

    @GetMapping("/auth/userinfo")
    public AuthResult<?> user(Principal principal) {

        System.out.println("======START Authenticate Create User Principal========");
        System.out.println("principal: "+principal.toString());
        UserDetails userPrincipal = userDetails.findByUsername(principal.getName());
        System.out.println("======FINISH Authenticate Create User Principal========");
        System.out.println("======PRINT User Principal========");
        System.out.println("======PRINT User Principal========");

        return new AuthResult<>(userPrincipal, "", false, userPrincipal.getAuthorities());


    }
}

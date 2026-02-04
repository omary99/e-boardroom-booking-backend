package com.boardroom.boardroom_booking.controller;

import com.boardroom.boardroom_booking.Configuration.security.CustomUserDetails;
import com.boardroom.boardroom_booking.Configuration.security.JwtUtil;
import com.boardroom.boardroom_booking.DTO.AuthRequest;
import com.boardroom.boardroom_booking.DTO.AuthResponse;
import com.boardroom.boardroom_booking.DTO.RegisterRequest;
import com.boardroom.boardroom_booking.DTO.UserInfoDTO;
import com.boardroom.boardroom_booking.audit.service.AuditService;
import com.boardroom.boardroom_booking.beans.AuthResult;
import com.boardroom.boardroom_booking.model.Department;
import com.boardroom.boardroom_booking.model.Role;
import com.boardroom.boardroom_booking.model.User;
import com.boardroom.boardroom_booking.repository.DepartmentRepository;
import com.boardroom.boardroom_booking.repository.UserRepository;
import com.boardroom.boardroom_booking.service.DepartmentService;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class AuthController {
    private final UserRepository userRepository;

    @Autowired
    private UserService userService;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuditService auditService;
    private final DepartmentService departmentService;


    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager, JwtUtil jwtUtil, AuditService auditService, DepartmentService departmentService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.auditService = auditService;
        this.departmentService = departmentService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String email = request.getEmail().trim();

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Email already exists"));
        }

        // Map DTO to User entity
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setMiddleName(request.getMiddleName());
        user.setSurname(request.getSurname());
        user.setGender(request.getGender());
        user.setEmail(email);
        user.setUsername(email); // username = email
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(generateFullName(user));

        // Fetch and set department
        if (request.getDepartmentId() != null) {
            Department department = departmentService.getDepartmentById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            user.setDepartment(department);
        }

        User savedUser = userRepository.save(user); // save and keep reference

        // Return the saved user along with a success message
        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully!",
                "user", savedUser
        ));
    }



//
//    @PostMapping("/auth/register")
//    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
//        String email = request.getEmail().trim();
//
//        if (userRepository.existsByEmail(email)) {
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body("Email already exists");
//        }
//
//        // Map DTO to User entity
//        User user = new User();
//        user.setFirstName(request.getFirstName());
//        user.setMiddleName(request.getMiddleName());
//        user.setSurname(request.getSurname());
//        user.setGender(request.getGender());
//        user.setEmail(email);
//        user.setUsername(email); // username = email
//        user.setPhoneNumber(request.getPhoneNumber());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setFullName(generateFullName(user)); // your helper method
//
//        // Fetch and set department
//        if (request.getDepartmentId() != null) {
//            Department department = departmentService.getDepartmentById(request.getDepartmentId())
//                    .orElseThrow(() -> new RuntimeException("Department not found"));
//            user.setDepartment(department);
//        }
//
//        userRepository.save(user);
//
//        return ResponseEntity.ok(Map.of("message", "User registered successfully!"));
//    }

    // Helper method
    private String generateFullName(User user) {
        StringBuilder fullName = new StringBuilder();
        if (user.getFirstName() != null) fullName.append(user.getFirstName());
        if (user.getMiddleName() != null && !user.getMiddleName().isBlank())
            fullName.append(" ").append(user.getMiddleName());
        if (user.getSurname() != null) fullName.append(" ").append(user.getSurname());
        return fullName.toString();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        System.out.println("New: " + request.getUsername());
        System.out.println("New: " + request.getPassword());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        var userDetails = new CustomUserDetails(
                userRepository.findByUsername(request.getUsername()).orElseThrow()
        );
        String token = jwtUtil.generateToken(userDetails, false);
        String refreshToken = jwtUtil.generateToken(userDetails, true);
        UserDetails user = (UserDetails) authentication.getPrincipal();
        //String roles = user.getAuthorities().toString(); // Convert roles to string
        String roles = jwtUtil.extractRoles(token);
        long expiresIn = jwtUtil.getExpirationTime(false); // Ensure this method returns the e
        System.out.println("token: " + token);
        auditService.log("USER-MANAGEMENT-SERVICE", "Login", "User " + user.getUsername() + " logged in", user.getUsername());
//        I commented hapa
//        return ResponseEntity.ok(new AuthResponse(token, refreshToken, "bearer", expiresIn, userDetails.getUsername(), roles));
        System.out.println("token: "+token);

        Optional<User> userOpt = userRepository.findByUsername(user.getUsername());

        auditService.log("USER-MANAGEMENT-SERVICE","Login","User "+user.getUsername()+" logged in",user.getUsername());
        return ResponseEntity.ok(new AuthResponse(token,refreshToken,"bearer",expiresIn, userDetails.getUsername(),roles,userOpt.get().getId()));
    }

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
        String newAccessToken = jwtUtil.generateTokenFromUsername(username, roles, true);

//        I commented hapa
//        return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken, "bearer", jwtUtil.getExpirationTime(false), username, roles));
        Optional<User> userOpt = userRepository.findByUsername(username);

        return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken,"bearer", jwtUtil.getExpirationTime(false), username, roles,userOpt.get().getId()));
    }

   @GetMapping("/auth/userinfo")
    public AuthResult<?> user(Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        return new AuthResult<>(
                new UserInfoDTO(
                        user.getId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getDepartment() != null ? user.getDepartment().getId() : null,
                        user.getDepartment() != null ? user.getDepartment().getName() : null,
                        user.getRolez().stream().map(Role::getName).toList()
                ),
                "",
                false,
                userDetails.getAuthorities()
        );
    }


}

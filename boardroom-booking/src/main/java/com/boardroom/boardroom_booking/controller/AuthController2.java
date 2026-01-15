//package com.boardroom.boardroom_booking.controller;
//
//import com.boardroom.boardroom_booking.DTO.LoginRequest;
//import com.boardroom.boardroom_booking.DTO.RegisterRequest;
//import com.boardroom.boardroom_booking.model.Department;
//import com.boardroom.boardroom_booking.model.User;
//import com.boardroom.boardroom_booking.repository.DepartmentRepository;
//import com.boardroom.boardroom_booking.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    private final DepartmentRepository departmentRepository;
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public AuthController(
//            DepartmentRepository departmentRepository,
//            UserRepository userRepository,
//            PasswordEncoder passwordEncoder
//    ) {
//        this.departmentRepository = departmentRepository;
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
//
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("User not found");
//        }
//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Invalid password");
//        }
//
//        return ResponseEntity.ok(Map.of(
//                "message", "Login successful!",
//                "userId", user.getId(),
//                "name", user.getFullName(),
//                "departmentId", user.getDepartment() != null ? user.getDepartment().getId() : null,
//                "departmentName", user.getDepartment() != null ? user.getDepartment().getName() : null,
//                "role", user.getRole()
//        ));
//    }
//
//
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
//        System.out.println("📩 Received register request: " + registerRequest);
//
//        if (registerRequest.getDepartmentId() == null) {
//            return ResponseEntity.badRequest().body(Map.of("error", "Department ID is required"));
//        }
//
//        if (userRepository.existsByEmail(registerRequest.getEmail())) {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(Map.of("error", "User with this email is already used."));
//        }
//
//        Department department = departmentRepository.findById(registerRequest.getDepartmentId())
//                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + registerRequest.getDepartmentId()));
//
//        User user = new User();
//        user.setFullName(registerRequest.getFullName());
//        user.setEmail(registerRequest.getEmail());
//        user.setPhoneNumber(registerRequest.getPhoneNumber());
//
//        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
//
//        user.setDepartment(department);
//
//        userRepository.save(user);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
//                "message", "User registered successfully",
//                "userId", user.getId(),
//                "department", department.getName(),
//                "departmentId", department.getId(),
//                "role", user.getRole()
//        ));
//    }
//
//}

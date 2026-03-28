package com.salonbooking.salon_booking_api.controller;

import com.salonbooking.salon_booking_api.config.JwtUtil;
import com.salonbooking.salon_booking_api.model.Admin;
import com.salonbooking.salon_booking_api.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AdminRepository adminRepository;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private PasswordEncoder passwordEncoder;

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        // Find admin by username
        Admin admin = adminRepository.findByUsername(username)
                .orElse(null);

        if (admin == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Invalid username or password"));
        }

        // Check password against BCrypt hash
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Invalid username or password"));
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(username);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "username", username,
                "message", "Login successful"
        ));
    }

    // GET /api/auth/verify — check if token is still valid
    @GetMapping("/verify")
    public ResponseEntity<?> verify() {
        return ResponseEntity.ok(Map.of("valid", true));
    }

//    @GetMapping("/setup")
//    public ResponseEntity<?> setup() {
//        // Delete existing admin
//        adminRepository.findByUsername("admin").ifPresent(adminRepository::delete);
//
//        // Create fresh admin with properly hashed password
//        Admin admin = new Admin();
//        admin.setUsername("admin");
//        admin.setPassword(passwordEncoder.encode("admin123"));
//        adminRepository.save(admin);
//
//        return ResponseEntity.ok(Map.of(
//                "message", "Admin created successfully",
//                "username", "admin",
//                "password", "admin123"
//        ));
//    }
}
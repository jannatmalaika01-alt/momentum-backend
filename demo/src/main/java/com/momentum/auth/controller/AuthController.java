package com.momentum.auth.controller;

import com.momentum.auth.dto.LoginRequest;
import com.momentum.auth.dto.SignupRequest;
import com.momentum.auth.dto.AuthResponse;
import com.momentum.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody SignupRequest signupRequest) {
        AuthResponse response = authService.registerUser(signupRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Auth service is running");
    }
@GetMapping("/verify-email")
public ResponseEntity<AuthResponse> verifyEmail(@RequestParam String token) {
    System.out.println("=== VERIFICATION REQUEST RECEIVED ===");
    System.out.println("Token: " + token);
    
    AuthResponse response = authService.verifyEmail(token);
    
    System.out.println("Response: " + response.isSuccess() + " - " + response.getMessage());
    System.out.println("===============================");
    
    return ResponseEntity.ok(response);
}

}
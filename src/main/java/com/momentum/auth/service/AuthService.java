package com.momentum.auth.service;

import com.momentum.auth.dto.LoginRequest;
import com.momentum.auth.dto.SignupRequest;
import com.momentum.auth.dto.AuthResponse;
import com.momentum.auth.dto.UserResponse;
import com.momentum.auth.entity.User;
import com.momentum.auth.entity.EmailVerificationToken;
import com.momentum.auth.repository.UserRepository;
import com.momentum.auth.repository.EmailVerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    private EmailService emailService;

    public AuthResponse registerUser(SignupRequest signupRequest) {
        // Check if passwords match
        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            return new AuthResponse(false, "Passwords do not match", null, null);
        }

        // Check if email already exists
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return new AuthResponse(false, "Email already registered", null, null);
        }

        // Create new user
        User user = new User();
        user.setFullName(signupRequest.getFullName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setEmailVerified(false); // Email not verified initially

        User savedUser = userRepository.save(user);

        // Generate and save email verification token
        String verificationToken = generateEmailVerificationToken(savedUser);
        
        // Send verification email
        emailService.sendVerificationEmail(savedUser.getEmail(), verificationToken);

        // Generate auth token
        String authToken = generateToken();

        UserResponse userResponse = new UserResponse(
            savedUser.getId(),
            savedUser.getFullName(),
            savedUser.getEmail()
        );

        return new AuthResponse(true, "User registered successfully. Please check your email for verification.", authToken, userResponse);
    }

    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        // Find user by email
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return new AuthResponse(false, "Invalid email or password", null, null);
        }

        // Check if email is verified
        if (!user.isEmailVerified()) {
            return new AuthResponse(false, "Please verify your email before logging in", null, null);
        }

        // Generate token
        String token = generateToken();

        UserResponse userResponse = new UserResponse(
            user.getId(),
            user.getFullName(),
            user.getEmail()
        );

        return new AuthResponse(true, "Login successful", token, userResponse);
    }

    public AuthResponse verifyEmail(String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token)
                .orElse(null);
        
        if (verificationToken == null) {
            return new AuthResponse(false, "Invalid verification token", null, null);
        }
        
        if (verificationToken.isExpired()) {
            return new AuthResponse(false, "Verification token has expired", null, null);
        }
        
        if (verificationToken.isVerified()) {
            return new AuthResponse(false, "Email already verified", null, null);
        }
        
        // Mark email as verified
        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);
        
        // Mark token as verified
        verificationToken.setVerified(true);
        emailVerificationTokenRepository.save(verificationToken);
        
        return new AuthResponse(true, "Email verified successfully!", null, null);
    }

    private String generateToken() {
        // For production, use JWT tokens with proper security
        return "token-" + UUID.randomUUID().toString();
    }

    private String generateEmailVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // 24 hours expiry
        verificationToken.setVerified(false);
        
        emailVerificationTokenRepository.save(verificationToken);
        return token;
    }
}
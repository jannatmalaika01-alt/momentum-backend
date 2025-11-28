package com.momentum.auth.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    // For now, we'll just print to console
    // In production, you'd integrate with an email service like SendGrid, AWS SES, etc.
    public void sendVerificationEmail(String toEmail, String verificationToken) {
        String verificationUrl = "http://localhost:8080/api/auth/verify-email?token=" + verificationToken;
        
        System.out.println("=== EMAIL VERIFICATION ===");
        System.out.println("To: " + toEmail);
        System.out.println("Subject: Verify Your Email Address");
        System.out.println("Message: Please click the link to verify your email: " + verificationUrl);
        System.out.println("==========================");
    }
    
    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        String resetUrl = "http://localhost:8080/api/auth/reset-password?token=" + resetToken;
        
        System.out.println("=== PASSWORD RESET EMAIL ===");
        System.out.println("To: " + toEmail);
        System.out.println("Subject: Password Reset Request");
        System.out.println("Message: Click here to reset your password: " + resetUrl);
        System.out.println("============================");
    }
}
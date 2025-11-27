package com.momentum.auth.repository;

import com.momentum.auth.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    
    // This method automatically finds a token by its string value
    Optional<EmailVerificationToken> findByToken(String token);
    
    // This method finds if a token exists for a specific user
    Optional<EmailVerificationToken> findByUserId(Long userId);
}
package com.israel.ecotracker.repository;

import com.israel.ecotracker.domain.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findByEmailAndOtp(String email, String otp);

    Optional<OtpToken> findByOtp(String otp);
    void deleteByEmail(String email);
}
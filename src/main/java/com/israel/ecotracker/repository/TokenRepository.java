package com.israel.ecotracker.repository;

import com.israel.ecotracker.domain.PasswordResetToken;
import com.israel.ecotracker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUser(User user);
}
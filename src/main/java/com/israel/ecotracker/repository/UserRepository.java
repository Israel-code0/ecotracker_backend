package com.israel.ecotracker.repository;

import com.israel.ecotracker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Custom query method to find a user by email
    Optional<User> findByEmail(String email);
}